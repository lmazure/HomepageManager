package fr.mazure.homepagemanager.data.linkchecker.martinfowler;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Martin Fowler's articles
 */
public class MartinFowlerLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "martinfowler.com";

    private static final TextParser s_titleParser
        = new TextParser("<h1(?: class = 'name')?(?: id=\"[^\"]+\")?>",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<p class = '(?:subtitle|intent)'>",
                         "</p>",
                         s_sourceName,
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<meta content = 'article' property = 'og:type'></meta>\n\n<meta content = '",
                         "' property = 'og:article:modified_time'></meta>",
                         s_sourceName,
                         "date");
    private static final TextParser s_authorParser1
        = new TextParser(" rel = 'author'>",
                         "</a></address>",
                         s_sourceName,
                         "author");
    private static final TextParser s_authorParser2
        = new TextParser("<div class = 'author'>",
                         ":",
                         s_sourceName,
                         "author");
    private static final TextParser s_authorParser3
        = new TextParser("<p class = 'content-author'>by <b>",
                         "</b></p>",
                         s_sourceName,
                         "author");

    private final String _title;
    private final Optional<String> _subtitle;
    private final TemporalAccessor _publicationDate;
    private final List<AuthorData> _authors;
    private final List<ExtractedLinkData> _links;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public MartinFowlerLinkContentParser(final String url,
                                         final String data,
                                         final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        _subtitle = s_subtitleParser.extractOptional(data)
                                    .map(HtmlHelper::cleanContent);

        String dateString = s_dateParser.extract(data);
        dateString = HtmlHelper.cleanContent(dateString);
        TemporalAccessor publicationDate;
        try {
            publicationDate = LocalDate.parse(dateString.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (final DateTimeParseException | StringIndexOutOfBoundsException _) {
            try {
                publicationDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
            } catch (final DateTimeParseException _) {
                try {
                    publicationDate = YearMonth.parse(dateString, DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH));
                } catch (final DateTimeParseException e) {
                    throw new ContentParserException("Failed to parse date", e);
                }
            }
        }
        _publicationDate = publicationDate;

        List<AuthorData> authors = new ArrayList<>();
        for (final String authorName: s_authorParser1.extractMulti(data)) {
            authors.add(LinkContentParserUtils.parseAuthorName(authorName));
        }
        if (authors.isEmpty()) {
            final Optional<String> auth = s_authorParser2.extractOptional(data);
            if (auth.isPresent()) {
                authors.add(LinkContentParserUtils.parseAuthorName(auth.get()));
            } else {
                authors = LinkContentParserUtils.getAuthors(s_authorParser3.extract(data));
            }
        }
        _authors = authors;

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 _subtitle.map(st -> new String[] { st })
                                                                          .orElse(new String[] { }),
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        _links = list;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://martinfowler.com/articles/") &&
               !url.equals("https://martinfowler.com/articles/eurogames/"); // this page is special, we do not handle it here
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
        return Optional.of(_publicationDate);
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return Optional.of(_publicationDate);
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
