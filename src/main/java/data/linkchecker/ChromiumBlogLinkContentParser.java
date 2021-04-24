package data.linkchecker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import utils.HtmlHelper;

public class ChromiumBlogLinkContentParser {

    private final String _data;
    private static final TextParser s_titleParser
        = new TextParser("<title>Chromium Blog: ",
                         "</title>",
                         "Chromium Blog",
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<span class='publishdate' itemprop='datePublished'>",
                         "</span>",
                         "Chromium Blog",
                         "date");
    private static final DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, u", Locale.ENGLISH);

    public ChromiumBlogLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.unescape(s_titleParser.extract(_data).trim());
    }

    public LocalDate getPublicationDate() throws ContentParserException {
        final String date = s_dateParser.extract(_data);
        try {
            return LocalDate.parse(date, s_formatter);
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Baeldung page", e);
        }
    }
}
