package fr.mazure.homepagemanager.data.linkchecker.baeldung;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
* Data extractor for Baeldung articles
*/
public class BaeldungLinkContentParser extends LinkDataExtractor {

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"single-title entry-title\" itemprop=\"headline\">",
                         "</h1>",
                         "Baeldung",
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<p class=\"post-modified\">Last updated: <span class=\"updated\">",
                         "</span></p>",
                         "Baeldung",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<a href=\"https://www.baeldung.com/author/[^/]*\" title=\"Posts by [^\"]*\" rel=\"author\">",
                         "</a>",
                         "Baeldung",
                         "author");
    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public BaeldungLinkContentParser(final String url,
                                     final String data) {
        super(url);
        _data = data;
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        try {
            return Optional.of(LocalDate.parse(date, s_formatter));
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Baeldung page", e);
        }
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final List<AuthorData> list = new ArrayList<>(1);
        final String author = s_authorParser.extract(_data);
        if (!author.equals("baeldung")) {
            list.add(LinkContentParserUtils.getAuthor(author));
        }
        return list;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl().toString(),
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
