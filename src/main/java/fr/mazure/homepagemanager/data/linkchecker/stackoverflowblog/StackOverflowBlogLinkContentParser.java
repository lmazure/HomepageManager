package fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for StackOverflow blog
 */
public class StackOverflowBlogLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "StackOverflow blog";

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"fs-display2 lh-xs p-ff-roboto-slab-bold mb24\" itemprop=\"name\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<p class=\"fs-title fc-black-500 wmx6\" itemprop=\"abstract\">",
                         "</p>",
                         s_sourceName,
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<header class=\"mb32 pt12\"><time datetime=\"",
                         "\"",
                         s_sourceName,
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<div class=\"fw-bold fs-body3\" itemprop=\"author\">",
                         "</div>",
                         s_sourceName,
                         "author");

    /**
     * Constructor
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public StackOverflowBlogLinkContentParser(final String url,
                                              final String data,
                                              final CachedSiteDataRetriever retriever) {
        super(url, retriever);
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://stackoverflow.blog/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        return Optional.of(HtmlHelper.cleanContent(s_subtitleParser.extract(_data)));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        final String extractedDate = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        final LocalDate date = ZonedDateTime.parse(extractedDate, DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
        return Optional.of(date);
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        final String extracted = s_authorParser.extract(_data);
        list.add(LinkContentParserUtils.parseAuthorName(HtmlHelper.cleanContent(extracted)));
        return list;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { getSubtitle().get() },
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
