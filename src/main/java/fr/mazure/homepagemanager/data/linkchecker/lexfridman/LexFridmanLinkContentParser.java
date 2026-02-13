package fr.mazure.homepagemanager.data.linkchecker.lexfridman;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
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

    private final String _title;
    private final Optional<TemporalAccessor> _creationDate;
    private final Optional<TemporalAccessor> _publicationDate;
    private final Optional<Duration> _duration;
    private final List<AuthorData> _authors;
    private Optional<ExtractedLinkData> _otherLink;
    private final List<ExtractedLinkData> _links;
    private final Locale _language;

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
     * @throws ContentParserException Failure to extract the information
     */
    public LexFridmanLinkContentParser(final String url,
                                       final String data,
                                       final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));
        _publicationDate = Optional.of(LocalDate.parse(s_dateParser.extract(data), s_dateformatter));
        final String mp3url = s_mp3UrlParser.extract(data) + "?_=1";
        final Mp3Helper helper = new Mp3Helper();
        _duration = Optional.of(helper.getMp3Duration(mp3url, getRetriever()));

        _authors = new ArrayList<>();
        final Matcher matcher = s_extractName.matcher(_title);
        if (matcher.find()) {
            final String authorName = matcher.group(1);
            _authors.add(LinkContentParserUtils.parseAuthorName(authorName));
        }
        _authors.add(WellKnownAuthors.LEX_FRIDMAN);

        final Optional<String> youtubeLink = s_youtubeLinkParser.extractOptional(data).map(s -> "https://www.youtube.com/watch?v=" + s);
        initializeOtherLink(youtubeLink);

        _creationDate = DateTimeHelper.getMinTemporalAccessor(_publicationDate, _otherLink.map(link -> link.publicationDate().get()));

        _language = Locale.ENGLISH;
        _links = initializeLinks();
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://lexfridman.com/");
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
        return _publicationDate;
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

    private void initializeOtherLink(final Optional<String> youtubeLink) {
        if (youtubeLink.isEmpty()) {
            _otherLink = Optional.empty();
        }

        // get YouTube payload
        getRetriever().retrieve(youtubeLink.get(), this::consumeYouTubeData, false);
    }

    private void consumeYouTubeData(final FullFetchedLinkData siteData) {
        final String payload = HtmlHelper.slurpFile(siteData.dataFileSection().get());

        // extract the link data
        try {
            final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(siteData.url(), payload, getRetriever());
            final LocalDate youtubeDate = DateTimeHelper.convertTemporalAccessorToLocalDate(parser.getCreationDate().get());
            final ExtractedLinkData linkData = new ExtractedLinkData(parser.getTitle(),
                                                                     new String[] { },
                                                                     siteData.url(),
                                                                     Optional.empty(),
                                                                     Optional.empty(),
                                                                     new LinkFormat[] { LinkFormat.MP4 },
                                                                     new Locale[] { parser.getLanguage() },
                                                                     parser.getDuration(),
                                                                     Optional.of(youtubeDate));
            _otherLink = Optional.of(linkData);
        } catch (ContentParserException e) {
            Logger.log(Level.ERROR)
                  .append("Failed to get YouTube link data")
                  .append(e)
                  .submit();

            _otherLink = Optional.empty();
        }
    }

    private List<ExtractedLinkData> initializeLinks() {
        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] {},
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP3 },
                                                                 new Locale[] { _language },
                                                                 _duration,
                                                                 _publicationDate);
        final List<ExtractedLinkData> list = new ArrayList<>(2);
        list.add(linkData);
        if (_otherLink.isPresent()) {
            list.add(_otherLink.get());
        }
        return list;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return _language;
    }
}
