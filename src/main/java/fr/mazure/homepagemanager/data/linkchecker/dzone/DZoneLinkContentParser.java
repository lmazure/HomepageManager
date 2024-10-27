package fr.mazure.homepagemanager.data.linkchecker.dzone;

import java.time.LocalDate;
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
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Dada extractor for DZone
 */
public class DZoneLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "DZone";

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<div class=\"title\">\n                        <h1 class=\"article-title\">",
                         "[^>]*",
                         "</h1>",
                         s_sourceName,
                         "title");

    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"subhead\">\n                        <h3>",
                         "[^>]*",
                         "</h3>",
                         s_sourceName,
                         "subtitle");

    private static final TextParser s_dateParser
        = new TextParser("\"datePublished\": \"",
                         "T00:00:00Z\"",
                         s_sourceName,
                         "date");

    private static final TextParser s_authorParser
        = new TextParser("<span class=\"author-name\">\n        <a (?:href=\"/users/\\d+/[a-zA-Z_0-9.-]+\\.html\" rel=\"nofollow\"|href=\"/authors/[a-zA-Z_0-9-]+\")>",
                         "</a>",
                         s_sourceName,
                         "author");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public DZoneLinkContentParser(final String url,
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
        return url.startsWith("https://dzone.com/articles/");
    }

    @Override
    public String getTitle() throws ContentParserException {
        return s_titleParser.extract(_data);
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        final String subtitle = s_subtitleParser.extract(_data);
        if (subtitle.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(subtitle);
    }

    /**
     * @return publication date
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getPublicationDate() throws ContentParserException {
        return LocalDate.parse(s_dateParser.extract(_data));
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getPublicationDate());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<String> authors = s_authorParser.extractMulti(_data);
        final List<AuthorData> list = new ArrayList<>(authors.size());
        for (final String author : authors) {
            list.add(LinkContentParserUtils.parseAuthorName(author));
        }
        return list;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 getSubtitle().isPresent() ? new String[] { getSubtitle().get() }
                                                                                           : new String[] {},
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
    public Locale getLanguage() throws ContentParserException {
        return Locale.ENGLISH;
    }
}
