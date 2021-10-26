package data.linkchecker.ars_technica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.TextParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class ArsTechnicaLinkContentParser {

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<h1 itemprop=\"headline\">",
                         "</h1>",
                         "Ars Technica",
                         "title");
    private static final TextParser s_subtitleParser
    = new TextParser("<h2 itemprop=\"description\">",
                     "</h2>",
                     "Ars Technica",
                     "title");
    private static final TextParser s_dateParser
        = new TextParser("<time class=\"date\" data-time=\"[0-9]{10}\" datetime=\"",
                         "T.*\">",
                         "Ars Technica",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<p class=\"byline\" itemprop=\"author creator\" itemscope itemtype=\"http://schema.org/Person\">.      <a itemprop=\"url\" href=\"https://arstechnica.com/author/[a-z]+/\" rel=\"author\"><span itemprop=\"name\">",
                         "</span></a>",
                         "Ars Technica",
                         "author");
    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ArsTechnicaLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    public String getSubtitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_subtitleParser.extract(_data));
    }

    public LocalDate getDate() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        try {
            return LocalDate.parse(date, s_formatter);
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Ars Technica page", e);
        }
    }

    public AuthorData getAuthor() throws ContentParserException {
        return LinkContentParserUtils.getAuthor(s_authorParser.extract(_data));
    }
}
