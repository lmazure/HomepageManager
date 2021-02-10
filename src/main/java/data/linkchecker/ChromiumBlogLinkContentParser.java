package data.linkchecker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.HtmlHelper;

public class ChromiumBlogLinkContentParser {

    private final String _data;
    private String _title;
    private LocalDate _publicationDate;

    public ChromiumBlogLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {

        if (_title == null) {
            _title = extractTitle();
        }

        return _title;
    }

    public LocalDate getPublicationDate() throws ContentParserException {

        if (_publicationDate == null) {
            _publicationDate = extractPublicationDate();
        }

        return _publicationDate;
    }

    private String extractTitle() throws ContentParserException {

        final Pattern p = Pattern.compile("<title>Chromium Blog: (.+?)</title>", Pattern.MULTILINE);
        final Matcher m = p.matcher(_data);        
        if (m.find()) {
            return HtmlHelper.unescape(m.group(1).trim());
         }

        throw new ContentParserException("Failed to find title in Chromium Blog page");
    }

    private LocalDate extractPublicationDate() throws ContentParserException {

        final Pattern p = Pattern.compile("<span class='publishdate' itemprop='datePublished'>([^<]+)</span>", Pattern.MULTILINE);
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            final String formattedDate = m.group(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, u", Locale.ENGLISH); 
            return LocalDate.parse(formattedDate, formatter);
        }

        throw new ContentParserException("Failed to find date in Chromium Blog page");
    }
}
