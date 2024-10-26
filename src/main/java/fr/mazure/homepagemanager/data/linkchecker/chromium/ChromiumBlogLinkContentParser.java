package fr.mazure.homepagemanager.data.linkchecker.chromium;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Data extractor from Chromium blog
 */
public class ChromiumBlogLinkContentParser {

    private static final String s_sourceName = "Chromium Blog";

    private final String _data;

    private static final TextParser s_titleParser
        = new TextParser("<title>\\nChromium Blog: ",
                         "</title>",
                         s_sourceName,
                         "title");
    private static final TextParser s_dateParser
        = new TextParser("<span class='publishdate' itemprop='datePublished'>",
                         "</span>",
                         s_sourceName,
                         "date");
    private static final DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, u", Locale.ENGLISH);

    /**
     * @param data retrieved link data
     */
    public ChromiumBlogLinkContentParser(final String data) {
        _data = data;
    }

    /**
     * @return title
     * @throws ContentParserException Failure to extract the information
     */
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    /**
     * @return publication date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getPublicationDate() throws ContentParserException {
        final String date = HtmlHelper.cleanContent(s_dateParser.extract(_data));
        try {
            return LocalDate.parse(date, s_formatter);
        } catch (final DateTimeParseException e) {
            throw new ContentParserException("Failed to parse date (" + date + ") in Chromium Blog page", e);
        }
    }
}
