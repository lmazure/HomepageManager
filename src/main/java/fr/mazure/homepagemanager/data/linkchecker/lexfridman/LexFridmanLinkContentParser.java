package fr.mazure.homepagemanager.data.linkchecker.lexfridman;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.Mp3Helper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *  Data extractor for Lex Fridman podcast
 */
public class LexFridmanLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Lex Fridman podcast";

    private final String _data;
    private String _title;
    private Optional<TemporalAccessor> _date;
    private Optional<Duration> _duration;
    private List<AuthorData> _authors;
    private Optional<ExtractedLinkData> _otherLink;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"entry-title\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<meta property=\"article:published_time\" content=\"",
                         "T",
                         s_sourceName,
                         "date");
    private static final TextParser s_youtubeLinkParser
        = new TextParser("\"https://www.youtube.com/embed/",
                         "\"",
                         s_sourceName,
                         "duration");
    private static final TextParser s_mp3UrlParser
        = new TextParser("Play in new window</a> \\| <a href=\"",
                         "[^\"]+",
                         "\" class=\"powerpress_link_d\" title=\"Download\" rel=\"nofollow\" download=\"",
                         s_sourceName,
                         "MP3 URL");

    private static final Pattern s_extractName = Pattern.compile("(?:^#\\d+ – )?(.*):.*$");

    private static final DateTimeFormatter s_dateformatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Constructor
     *
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public LexFridmanLinkContentParser(final String url,
                                       final String data,
                                       final CachedSiteDataRetriever retriever) {
        super(UrlHelper.removeFinalSlash(url), retriever);
        _data = data;
        _title = null;
        _date = null;
        _duration = null;
        _authors = null;
        _otherLink = null;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://lexfridman.com/");
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
            final String mp3url = s_mp3UrlParser.extract(_data) + "?_=1";
            final Mp3Helper helper = new Mp3Helper();
            _duration = Optional.of(helper.getMp3Duration(mp3url, getRetriever()));
        }
        return _duration;
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        if (_authors == null) {
            final Matcher matcher = s_extractName.matcher(getTitle());
            if (!matcher.find()) {
                throw new ContentParserException("Failed to extract author name from title \"" + getTitle() + "\"");
            }
            final String authorName = matcher.group(1);
            _authors = LinkContentParserUtils.getAuthors(authorName);
            _authors.add(WellKnownAuthors.LEX_FRIDMAN);
        }
        return _authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return new ArrayList<>(0);
    }

    private Optional<String> getYoutubeLink() {
        return s_youtubeLinkParser.extractOptional(_data).map(s -> "https://www.youtube.com/watch?v=" + s);
    }

    private Optional<ExtractedLinkData> getOtherLink() {
        if (_otherLink == null) {
            // get YouTube link
            Optional<String> youtubeLink = getYoutubeLink();
            if (youtubeLink.isEmpty()) {
                return Optional.empty();
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
            Optional<TemporalAccessor> publicationDate = Optional.empty();
            final LocalDate youtubeDate = DateTimeHelper.convertTemporalAccessorToLocalDate(parser.getDate().get());
            final LocalDate lexFridmanDate = DateTimeHelper.convertTemporalAccessorToLocalDate(getDate().get());
            if (youtubeDate.isBefore(lexFridmanDate)) {
                ExitHelper.exit("YouTube date (" + youtubeDate + ") is before Lex Fridman date (" + lexFridmanDate + ").");
            }
            if (youtubeDate.isAfter(lexFridmanDate)) {
                publicationDate = Optional.of(youtubeDate);
            }
            final ExtractedLinkData linkData = new ExtractedLinkData(parser.getTitle(),
                                                                     new String[] { },
                                                                     siteData.url(),
                                                                     Optional.empty(),
                                                                     Optional.empty(),
                                                                     new LinkFormat[] { LinkFormat.MP4 },
                                                                     new Locale[] { parser.getLanguage() },
                                                                     parser.getDuration(),
                                                                     publicationDate);
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
