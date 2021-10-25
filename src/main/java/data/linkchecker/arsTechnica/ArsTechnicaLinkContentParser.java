package data.linkchecker.arsTechnica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import data.linkchecker.ContentParserException;
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

    public Optional<@NonNull AuthorData> getAuthor() throws ContentParserException {
        final String author = HtmlHelper.cleanContent(s_authorParser.extract(_data));
        if (author.equals("baeldung")) {
            return Optional.empty();
        }
        final String[] nameParts = HtmlHelper.cleanContent(author).split(" ");
        if (nameParts.length == 2) {
            return Optional.of(new AuthorData(Optional.empty(),
                    Optional.of(nameParts[0]),
                    Optional.empty(),
                    Optional.of(nameParts[1]),
                    Optional.empty(),
                    Optional.empty()));
        }
        if (nameParts.length == 3) {
            return Optional.of(new AuthorData(Optional.empty(),
                    Optional.of(nameParts[0]),
                    Optional.of(nameParts[1]),
                    Optional.of(nameParts[2]),
                    Optional.empty(),
                    Optional.empty()));
        }
        throw new ContentParserException("Failed to parse author name (" + author + ")in Ars Technica page");
    }
}
