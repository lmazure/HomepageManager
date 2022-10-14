package data.linkchecker.arstechnica;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

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
                     "subtitle");
    private static final TextParser s_dateParser
        = new TextParser("<time class=\"date\" data-time=\"",
                         "\" datetime=\".*?\">",
                         "Ars Technica",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<p class=\"byline\" itemprop=\"author creator\" itemscope itemtype=\"http://schema.org/Person\">.      <a itemprop=\"url\" href=\"https://arstechnica.com/author/[a-z0-9_-]+/\" rel=\"author\"><span itemprop=\"name\">",
                         "</span></a>",
                         "Ars Technica",
                         "author");

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
        final Instant instant = Instant.ofEpochSecond(Long.parseLong(date));
        return LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris"));
    }

    public Optional<AuthorData> getAuthor() throws ContentParserException {
        final String authorName = s_authorParser.extract(_data);
        if (authorName.equals("Ars Staff")) {
            return Optional.empty();
        }
        return Optional.of(LinkContentParserUtils.getAuthor(authorName));
    }
}
