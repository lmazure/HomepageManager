package data.linkchecker.baeldung;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.TextParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class BaeldungLinkContentParser {

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"single-title entry-title\" itemprop=\"headline\">",
                         "</h1>",
                         "Baeldung",
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<p class=\"post-modified\">Last modified: <span class=\"updated\">",
                         "</span></p>",
                         "Baeldung",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<a href=\"https://www.baeldung.com/author/[^/]*\" title=\"Posts by [^\"]*\" rel=\"author\">",
                         "</a>",
                         "Baeldung",
                         "author");
    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);

    public BaeldungLinkContentParser(final String data,
                                     final String url) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    public LocalDate getDate() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        try {
            return LocalDate.parse(date, s_formatter);
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Baeldung page", e);
        }
    }

    public Optional<AuthorData> getAuthor() throws ContentParserException {
        final String author = s_authorParser.extract(_data);
        if (author.equals("baeldung")) {
            return Optional.empty();
        }
        return Optional.of(LinkContentParserUtils.getAuthor(author));
    }
}
