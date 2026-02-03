package fr.mazure.homepagemanager.data.linkchecker.thoughtworks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for ThoughtWorks podcasts
 */
public class ThoughtWorksLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "ThoughtWorks podcast";

    private final String _title;
    private final Optional<String> _subtitle;
    private final Optional<TemporalAccessor> _date;
    private final List<AuthorData> _sureAuthors;
    private final Optional<Duration> _duration;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class='hero-banner__overlay__container__title'>",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<div class='[^']+'>\n                    <span>",
                         "</span>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<meta name=\"tw_published_date\" content=\"",
                         "\"",
                         s_sourceName,
                         "date");
    private static final TextParser s_guestParser
        = new TextParser("<span class=\"guest\">",
                         "</span>",
                         s_sourceName,
                         "guest author");
    private static final TextParser s_hostParser
        = new TextParser("<span class=\"host\">",
                         "</span>",
                         s_sourceName,
                         "host author");
    private static final TextParser s_durationParser
        = new TextParser("<div class=\"date-duration\">\n *\n *\n *[^|]+ \\|",
                         "\n *</div>",
                         s_sourceName,
                         "duration");
                         
    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     *
     * @throws ContentParserException Failure to extract the information
     */
    public ThoughtWorksLinkContentParser(final String url,
                                         final String data,
                                         final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        final Optional<String> subtitleRaw = s_subtitleParser.extractOptional(data);
        _subtitle = subtitleRaw.isEmpty() ? Optional.empty()
                                          : Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(data)));

        final String date = HtmlHelper.cleanContent(s_dateParser.extract(data));
        _date = Optional.of(LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate());

        _sureAuthors = new ArrayList<>();
        for (final String authorName: s_guestParser.extractMulti(data)) {
            _sureAuthors.add(LinkContentParserUtils.parseAuthorName(cleanName(authorName)));
        }
        for (final String authorName: s_hostParser.extractMulti(data)) {
            _sureAuthors.add(LinkContentParserUtils.parseAuthorName(cleanName(authorName)));
        }
        
        final String duration = HtmlHelper.cleanContent(s_durationParser.extract(data));
        _duration = Optional.of(DateTimeHelper.parseDuration(duration));
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/");
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
        return _date;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _date;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _sureAuthors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Duration> getDuration() {
	    return _duration;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[0],
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.MP3 },
                                                                 new Locale[] { getLanguage() },
                                                                 _duration,
                                                                 _date);
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }

    private static String cleanName(final String name) {
        return name.replaceFirst("^ *and *", "")
                   .replaceFirst("^ *, *", "");
    }
}
