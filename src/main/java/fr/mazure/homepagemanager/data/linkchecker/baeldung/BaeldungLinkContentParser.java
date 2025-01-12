package fr.mazure.homepagemanager.data.linkchecker.baeldung;

import java.time.LocalDate;
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
 * Data extractor for Baeldung articles
 */
public class BaeldungLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Baeldung";

    private final String _title;
    private final Optional<TemporalAccessor> _creationDate;
    private final List<AuthorData> _sureAuthors;
    private final List<ExtractedLinkData> _links;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"single-title entry-title\" itemprop=\"headline\">",
                         "</h1>",
                         s_sourceName,
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<p class=\"post-modified\">Last updated: <span class=\"updated\">",
                         "</span></p>",
                         s_sourceName,
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<a href=\"https://www.baeldung.com(?:/ops)?/author/[^/]*\" title=\"Posts by [^\"]*\" rel=\"author\">",
                         "</a>",
                         s_sourceName,
                         "author");
    private static final DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException failure to extract the information
     */
    public BaeldungLinkContentParser(final String url,
                                     final String data,
                                     final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);
        _title = HtmlHelper.cleanContent(s_titleParser.extract(data));

        final String date = HtmlHelper.cleanContent(s_dateParser.extract(data));
        try {
            _creationDate = Optional.of(LocalDate.parse(date, s_formatter));
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Baeldung page", e);
        }

        final List<AuthorData> authorList = new ArrayList<>(1);
        final String author = s_authorParser.extract(data);
        if (!author.equals("baeldung")) {
            authorList.add(LinkContentParserUtils.parseAuthorName(author));
        }
        _sureAuthors = authorList;

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] { },
                                                                 url,
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { Locale.ENGLISH },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> linkList = new ArrayList<>(1);
        linkList.add(linkData);
        _links = linkList;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://www.baeldung.com/");
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
		return getCreationDate();
	}

    @Override
    public List<AuthorData> getSureAuthors() {
        return _sureAuthors;
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
