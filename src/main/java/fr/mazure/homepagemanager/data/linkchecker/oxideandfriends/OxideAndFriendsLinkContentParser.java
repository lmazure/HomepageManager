package fr.mazure.homepagemanager.data.linkchecker.oxideandfriends;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.internet.YouTubeHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *  Data extractor for Oxide and Friends blog
 */
public class OxideAndFriendsLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Oxide and Friends";

    private final String _title;
    private Optional<TemporalAccessor> _creationDate;
    private final Optional<TemporalAccessor> _blogPublicationDate;
    private final Optional<Duration> _duration;
    private List<AuthorData> _authors;
    private Optional<ExtractedLinkData> _otherLink;

    private static final TextParser s_titleParser
        = new TextParser("<h2 class=\"site-episode-title\">",
                         "</h2>",
                         s_sourceName,
                         "title");
    private static final TextParser s_blogPublicationDateParser
        = new TextParser("<time>",
                         "</time>",
                         s_sourceName,
                         "blog publcation date");
    private static final TextParser s_creationDateParser
        = new TextParser("Oxide and Friends ",
                         " --",
                         s_sourceName,
                         "creation date");
    private static final TextParser s_durationParser
        = new TextParser("<span>",
                         "\\d\\d:\\d\\d:\\d\\d",
                         "</span><span>/</span><span>S",
                         s_sourceName,
                         "duration");
    private static final TextParser s_transcriptParticipantParser
        = new TextParser("<cite>",
                         ":</cite>",
                         s_sourceName,
                         "transcript participant");

    private static final DateTimeFormatter s_blogPublicationDateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter s_creationDateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    private static final Integer ZERO = Integer.valueOf(0);

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public OxideAndFriendsLinkContentParser(final String url,
                                            final String data,
                                            final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        _blogPublicationDate = Optional.of(LocalDate.parse(s_blogPublicationDateParser.extract(data), s_blogPublicationDateFormatter));

        final String durationString = s_durationParser.extract(data);
        final String[] parts = durationString.split(":");
        final int hours = Integer.parseInt(parts[0]);
        final int minutes = Integer.parseInt(parts[1]);
        final int seconds = Integer.parseInt(parts[2]);
        _duration = Optional.of(Duration.ofHours(hours)
                                        .plusMinutes(minutes)
                                        .plusSeconds(seconds));

        // get transcript
        final String transcriptUrl = url + "/transcript";
        getRetriever().retrieve(transcriptUrl, this::consumeTranscript, false);

        // get YouTube link
        final YouTubeHelper helper = new YouTubeHelper();
        final Optional<String> youtubeLink = helper.getVideoURL("Oxide Computer Company", getTitle(), getRetriever());
        if (youtubeLink.isEmpty()) {
            _otherLink = Optional.empty();
        } else {
            // get YouTube payload
            getRetriever().retrieve(youtubeLink.get(), this::consumeYouTubeData, false);
        }
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://oxide-and-friends.transistor.fm/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return _creationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _blogPublicationDate;
    }

    @Override
    public Optional<Duration> getDuration() {
        return _duration;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return Collections.emptyList();
    }

    private Optional<ExtractedLinkData> getOtherLink() {
        return _otherLink;
    }

    private void consumeTranscript(final FullFetchedLinkData siteData) {
        final String payload = HtmlHelper.slurpFile(siteData.dataFileSection().get());

        final List<String> transcriptParticipants = s_transcriptParticipantParser.extractMulti(payload);

        // Count the occurrences of each string
        final Map<String, Integer> counts = new HashMap<>();
        for (final String participant : transcriptParticipants) {
            counts.put(participant, Integer.valueOf(counts.getOrDefault(participant, ZERO).intValue() + 1));
        }

        // Sort the strings by their counts in descending order
        final List<String> sortedParticipants = new ArrayList<>(counts.keySet());
        sortedParticipants.sort((a, b) -> counts.get(b).intValue() - counts.get(a).intValue());

        // Parse the author names
        _authors = new ArrayList<>(sortedParticipants.size());
        for (final String participant : sortedParticipants) {
            try {
                _authors.add(LinkContentParserUtils.parseAuthorName(participant));
            } catch (final ContentParserException e) {
                ExitHelper.exit("Failed to parse author name in Oxide and Friends transcript", e);
            }
        }
    }

    private void consumeYouTubeData(final FullFetchedLinkData siteData) {
        final String payload = HtmlHelper.slurpFile(siteData.dataFileSection().get());

        // extract the link data
        try {
            final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(siteData.url(), payload, getRetriever());
            final String creationDateString = s_creationDateParser.extract(parser.getTitle());
            _creationDate = Optional.of(LocalDate.parse(creationDateString, s_creationDateFormatter));
            final ExtractedLinkData linkData = new ExtractedLinkData(parser.getTitle(),
                                                                     new String[] { },
                                                                     siteData.url(),
                                                                     Optional.empty(),
                                                                     Optional.empty(),
                                                                     new LinkFormat[] { LinkFormat.MP4 },
                                                                     new Locale[] { parser.getLanguage() },
                                                                     parser.getDuration(),
                                                                     parser.getCreationDate());
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
    public List<ExtractedLinkData> getLinks() {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] {},
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP3 },
                                                                 new Locale[] { getLanguage() },
                                                                 getDuration(),
                                                                 getPublicationDate());
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
