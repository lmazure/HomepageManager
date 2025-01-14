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
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Dada extractor for DZone
 */
public class DZoneLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "DZone";

    private final String _title;
    private final Optional<String> _subtitle;
    private final Optional<TemporalAccessor> _date;
    private final List<AuthorData> _authors;

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
     * @throws ContentParserException Failure to extract the information
     */
    public DZoneLinkContentParser(final String url,
                                  final String data,
                                  final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = s_titleParser.extract(data);

        final String subtitle = s_subtitleParser.extract(data);
       if (subtitle.isEmpty()) {
           _subtitle = Optional.empty();
       } else {
           _subtitle = Optional.of(subtitle);
       }

       _date = Optional.of(LocalDate.parse(s_dateParser.extract(data)));

       final List<String> authors = s_authorParser.extractMulti(data);
       _authors = new ArrayList<>(authors.size());
       for (final String author : authors) {
           _authors.add(LinkContentParserUtils.parseAuthorName(author));
       }
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://dzone.com/articles/");
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
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return _date;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
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
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
