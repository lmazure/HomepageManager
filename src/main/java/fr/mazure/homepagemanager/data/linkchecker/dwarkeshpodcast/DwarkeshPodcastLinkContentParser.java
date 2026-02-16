package fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

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
import fr.mazure.homepagemanager.utils.internet.JsonHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *  Data extractor for Dwarkesh Podcast
 */
public class DwarkeshPodcastLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Dwarkesh Podcast";

    private final String _title;
    private final Optional<String> _subtitle;
    private final Optional<TemporalAccessor> _creationDate;
    private final Optional<TemporalAccessor> _publicationDate;
    private final Optional<Duration> _duration;
    private final List<AuthorData> _authors;
    private Optional<ExtractedLinkData> _otherLink;
    private final List<ExtractedLinkData> _links;
    private final Locale _language;

    private static final TextParser s_jsonParser
        = new TextParser("window\\._preloads\\s+=\\s+JSON\\.parse\\(\"",
                         "\"\\)</script>",
                         s_sourceName,
                         "JSON");
    private static final TextParser s_subtitleParser
        = new TextParser("<div dir=\"auto\" class=\"pencraft pc-reset color-pub-secondary-text-hGQ02T line-height-24-jnGwiv font-pub-headings-FE5byy size-17-JHHggF weight-regular-mUq6Gb reset-IxiVJZ subtitle-HEEcLo\">",
                         "</div>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_youtubeLinkParser
        = new TextParser("youtube-nocookie\\.com/embed/",
                         "[A-Za-z0-9_-]+",
                         "\\?",
                         s_sourceName,
                         "YouTube link");

    private static final Pattern s_extractGuest = Pattern.compile("^(.+?)\\s+[\u2013\u2014]\\s+.+$");

    /**
     * Constructor
     *
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public DwarkeshPodcastLinkContentParser(final String url,
                                            final String data,
                                            final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        try {
            final String escapedJson = s_jsonParser.extract(data);
            final String json = JsonHelper.unescape(escapedJson);
            final JSONObject payload = new JSONObject(json);
            final JSONObject post = JsonHelper.getAsNode(payload, "post");

            _title = HtmlHelper.cleanContent(JsonHelper.getAsText(post, "title"));
            final String postDate = JsonHelper.getAsText(post, "post_date");
            _publicationDate = Optional.of(ZonedDateTime.parse(postDate, DateTimeFormatter.ISO_DATE_TIME).toLocalDate());
            final double podcastDuration = post.getDouble("podcast_duration");
            _duration = Optional.of(Duration.ofSeconds(Math.round(podcastDuration)));
        } catch (final IllegalStateException e) {
            throw new ContentParserException("Unexpected JSON", e);
        }

        _subtitle = s_subtitleParser.extractOptional(data)
                                    .map(HtmlHelper::cleanContent);

        _authors = new ArrayList<>();
        final Matcher matcher = s_extractGuest.matcher(_title);
        if (matcher.find()) {
            final String guestName = matcher.group(1);
            _authors.add(LinkContentParserUtils.parseAuthorName(guestName));
        }
        _authors.add(WellKnownAuthors.DWARKESH_PATEL);

        final Optional<String> youtubeVideoId = s_youtubeLinkParser.extractOptional(data);
        final Optional<String> youtubeLink = youtubeVideoId.map(s -> "https://www.youtube.com/watch?v=" + s);
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
        return UrlHelper.hasPrefix(url, "https://www.dwarkesh.com/p/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return _subtitle;
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
                                                                     new String[] { },
                                                                     siteData.url(),
                                                                     Optional.empty(),
                                                                     Optional.empty(),
                                                                     new LinkFormat[] { LinkFormat.MP4 },
                                                                     new Locale[] { parser.getLanguage() },
                                                                     parser.getDuration(),
                                                                     Optional.of(youtubeDate));
            _otherLink = Optional.of(linkData);
        } catch (final ContentParserException e) {
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
                                                                 new LinkFormat[] { LinkFormat.HTML },
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
