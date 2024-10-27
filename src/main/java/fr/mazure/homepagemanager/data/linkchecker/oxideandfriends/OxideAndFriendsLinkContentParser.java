package fr.mazure.homepagemanager.data.linkchecker.oxideandfriends;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.YouTubeHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *  Data extractor for Oxide and Friends blog
 */
public class OxideAndFriendsLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Oxide and Friends";

    private final String _data;
    private String _title;
    private Optional<TemporalAccessor> _date;
    private Optional<Duration> _duration;
    private Optional<ExtractedLinkData> _otherLink;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"text-sans-3xl 800:text-sans-3xl 1000:text-sans-4xl text-default my-3\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<div class=\"block uppercase text-mono-sm text-tertiary\"><span class=\"inline-block\">",
                         "</span>",
                         s_sourceName,
                         "date");
    private static final TextParser s_durationParser
        = new TextParser("<span class=\"ml-2 inline-block\">",
                         "</span>",
                         s_sourceName,
                         "duration");

    private static final DateTimeFormatter s_dateformatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    private static final List<AuthorData> s_authors = new ArrayList<>(Arrays.asList(
        WellKnownAuthors.buildAuthor("Bryan", "Cantrill"),
        WellKnownAuthors.buildAuthor("Adam", "Leventhal")
    ));

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public OxideAndFriendsLinkContentParser(final String url,
                                            final String data,
                                            final CachedSiteDataRetriever retriever) {
        super(url, retriever);
        _data = data;
        _title = null;
        _date = null;
        _duration = null;
        _otherLink = null;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://oxide.computer/podcasts/oxide-and-friends/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        if (_title == null) {
            _title = HtmlHelper.cleanContent(s_titleParser.extract(_data));
        }
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        if (_date == null) {
            _date = Optional.of(LocalDate.parse(s_dateParser.extract(_data), s_dateformatter));
        }
        return _date;
    }

    @Override
    public Optional<Duration> getDuration() throws ContentParserException {
        if (_duration == null) {
            final String timeString = s_durationParser.extract(_data);
            final String[] parts = timeString.split(":");
            final int hours = Integer.parseInt(parts[0]);
            final int minutes = Integer.parseInt(parts[1]);
            final int seconds = Integer.parseInt(parts[2]);
            _duration = Optional.of(Duration.ofHours(hours)
                                            .plusMinutes(minutes)
                                            .plusSeconds(seconds));
        }
        return _duration;
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return s_authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return new ArrayList<>(0);
    }

    private Optional<ExtractedLinkData> getOtherLink() throws ContentParserException {

        if (_otherLink == null) {
            // get YouTube link
            final YouTubeHelper helper = new YouTubeHelper();
            final Optional<String> youtubeLink = helper.getVideoURL("Oxide Computer Company", getTitle(), getRetriever());
            if (youtubeLink.isEmpty()) {
                _otherLink = Optional.empty();
                return _otherLink;
            }

            // get YouTube payload
            getRetriever().retrieve(youtubeLink.get(), this::consumeYouTubeData, false);
        }

        return _otherLink;
    }

    private void consumeYouTubeData(final FullFetchedLinkData siteData) {
        final String payload = HtmlHelper.slurpFile(siteData.dataFileSection().get());

        // extract the link data
        try {
            final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(siteData.url(), payload, getRetriever());
            final ExtractedLinkData linkData = new ExtractedLinkData(parser.getTitle(),
                                                                     new String[] { },
                                                                     siteData.url(),
                                                                     Optional.empty(),
                                                                     Optional.empty(),
                                                                     new LinkFormat[] { LinkFormat.MP4 },
                                                                     new Locale[] { parser.getLanguage() },
                                                                     parser.getDuration(),
                                                                     Optional.empty());
            _otherLink = Optional.of(linkData);
        } catch (ContentParserException e) {
            Logger.log(Level.ERROR)
                  .append("Failed to get YouTube link data")
                  .append(e)
                  .submit();

            _otherLink = Optional.empty();
        }
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] {},
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP3 },
                                                                 new Locale[] { getLanguage() },
                                                                 getDuration(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(2);
        list.add(linkData);
        final Optional<ExtractedLinkData> otherLink = getOtherLink();
        if (otherLink.isPresent()) {
            list.add(otherLink.get());
        }
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
