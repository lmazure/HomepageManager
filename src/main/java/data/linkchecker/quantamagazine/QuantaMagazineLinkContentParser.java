package data.linkchecker.quantamagazine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern s_authorPattern
        = Pattern.compile("<a (class=\"[^\"]+\" )?href=\"/authors/[^/]+/\"><span [^>]+>([^<]+)</span></a>");

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

    public List<AuthorData> getAuthors() throws ContentParserException {
        final List<AuthorData> authors = new ArrayList<>();
        final Matcher m = s_authorPattern.matcher(_data);
        while (m.find()) {
            authors.add(LinkContentParserUtils.getAuthor(m.group(2)));
        }
        if (authors.size() == 0) {
            throw new ContentParserException("Failed to find author in QuantaMagazine");
        }
        return authors;
    }
}
