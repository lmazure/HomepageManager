package fr.mazure.homepagemanager.data.linkchecker.numberphile;

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
 * Data extractor for Numberphile podcast
 */
public class NumberphileLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Numberphile podcast";

    private final String _title;
    private final Optional<TemporalAccessor> _creationDate;
    private final Optional<TemporalAccessor> _publicationDate;
    private final Optional<Duration> _duration;
    private final List<AuthorData> _authors;
    private Optional<ExtractedLinkData> _otherLink;
    private final List<ExtractedLinkData> _links;
    private final Locale _language;

    private static final TextParser s_titleParser =
        new TextParser("<meta itemprop=\"headline\" content=\"",
                       "\"/>",
                       s_sourceName,
                       "title");
    private static final TextParser s_dateParser =
        new TextParser("<meta itemprop=\"datePublished\" content=\"",
                       "T",
                       s_sourceName,
                       "date");
    private static final TextParser s_youtubeLinkParser =
        new TextParser("youtube.com/embed/",
                       "\\?",
                       s_sourceName,
                       "YouTube ID");
    private static final TextParser s_mp3UrlParser =
        new TextParser("data-url=\"",
                       "\"",
                       s_sourceName,
                       "MP3 URL");

    private static final Pattern s_extractGuest = Pattern.compile(" - with (.*)$");

    private static final DateTimeFormatter s_dateformatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * @param url       URL of the link
     * @param data      retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public NumberphileLinkContentParser(final String url,
                                        final String data,
                                        final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(UrlHelper.removeFinalSlash(url), retriever);

        final String rawTitle = HtmlHelper.cleanContent(s_titleParser.extract(data));
        _title = rawTitle.replaceFirst("^PODCAST:\\s+", "");

        _publicationDate = Optional.of(LocalDate.parse(s_dateParser.extract(data), s_dateformatter));

        final String mp3url = s_mp3UrlParser.extract(data);
        final Mp3Helper helper = new Mp3Helper();
        _duration = Optional.of(helper.getMp3Duration(mp3url, getRetriever()));

        _authors = new ArrayList<>();
        final Matcher matcher = s_extractGuest.matcher(_title);
        if (matcher.find()) {
            final String guestName = matcher.group(1);
            _authors.add(LinkContentParserUtils.parseAuthorName(guestName));
        }
        _authors.add(WellKnownAuthors.BRADY_HARAN);

        final Optional<String> youtubeLink = s_youtubeLinkParser.extractOptional(data)
                                                                .map(s -> "https://www.youtube.com/watch?v=" + s);
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
        return UrlHelper.hasPrefix(url, "https://www.numberphile.com/podcast/");
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
            return;
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
                                                                     new String[] {},
                                                                     siteData.url(),
                                                                     Optional.empty(),
                                                                     Optional.empty(),
                                                                     new LinkFormat[] { LinkFormat.MP4 },
                                                                     new Locale[] { parser.getLanguage() },
                                                                     parser.getDuration(),
                                                                     Optional.of(youtubeDate));
            _otherLink = Optional.of(linkData);
        } catch (final ContentParserException e) {
            Logger.log(Level.ERROR).append("Failed to get YouTube link data").append(e).submit();
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
