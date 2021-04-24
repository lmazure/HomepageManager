package data.linkchecker;

import java.time.LocalDate;
import java.util.Optional;

import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class QuantaMagazineLinkContentParser {

    private final String _data;
    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"post__title__title [^\"]+\" data-reactid=\"[0-9]+\">",
                         "</h1>",
                         "QuantaMagazine",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"post__title__excerpt [^\"]+\" data-reactid=\"[0-9]+\">",
                         " *</div>",
                         "QuantaMagazine",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser(",\"date\":\"",
                         "[^\"]*",
                         "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\",\"featured_media_image\":",
                         "QuantaMagazine",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<span class=\"byline__author [^\\\"]+\\\" data-reactid=\\\"[0-9]+\\\">",
                         "</span>",
                         "QuantaMagazine",
                         "author");

    public QuantaMagazineLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.unescape(s_titleParser.extract(_data));
    }

    public String getSubtitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_subtitleParser.extract(_data));
    }

    public LocalDate getDate() throws ContentParserException {
        return LocalDate.parse(s_dateParser.extract(_data));
    }

    public AuthorData getAuthor() throws ContentParserException {
        final String[] nameParts = HtmlHelper.cleanContent(s_authorParser.extract(_data)).split(" ");
        if (nameParts.length != 2) {
            throw new ContentParserException("Failed to parse author name in QuantaMagazine page");
        }
        return new AuthorData(Optional.empty(),
                              Optional.of(nameParts[0]),
                              Optional.empty(),
                              Optional.of(nameParts[1]),
                              Optional.empty(),
                              Optional.empty());
    }
}
