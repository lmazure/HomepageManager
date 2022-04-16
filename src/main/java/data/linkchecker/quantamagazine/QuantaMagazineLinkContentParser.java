package data.linkchecker.quantamagazine;

import java.time.LocalDate;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.TextParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class QuantaMagazineLinkContentParser {

    private final String _data;
    private static final TextParser s_titleParser
        = new TextParser("<h1 class=\"ml025 h3 noe mv0\">",
                         "</h1>",
                         "QuantaMagazine",
                         "title");
    private static final TextParser s_subtitleParser
        = new TextParser("<div class=\"post__title__excerpt wysiwyg p italic mb1 mt025 pr2 o4 theme__text[- a-z]*\">",
                         "</div>",
                         "QuantaMagazine",
                         "subtitle");
    private static final TextParser s_dateParser
        = new TextParser(",\"date\":\"",
                         "[^\"]*",
                         "T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\",\"featured_media_image\":",
                         "QuantaMagazine",
                         "date");
    private static final TextParser s_authorParser
        = new TextParser("<span class=\"byline__author uppercase kern light small\">",
                         "</span>",
                         "QuantaMagazine",
                         "author");

    public QuantaMagazineLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    public String getSubtitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_subtitleParser.extract(_data));
    }

    public LocalDate getDate() throws ContentParserException {
        return LocalDate.parse(HtmlHelper.cleanContent(s_dateParser.extract(_data)));
    }

    public AuthorData getAuthor() throws ContentParserException {
        return LinkContentParserUtils.getAuthor(s_authorParser.extract(_data));
    }
}
