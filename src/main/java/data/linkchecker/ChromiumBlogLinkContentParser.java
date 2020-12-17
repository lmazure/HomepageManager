package data.linkchecker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;

public class ChromiumBlogLinkContentParser {

    private final String _data;
    private String _title;
    private LocalDate _publicationDate;

    public ChromiumBlogLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() {

        if (_title == null) {
            _title = extractTitle();
        }

        return _title;
    }

    public LocalDate getPublicationDate() {

        if (_publicationDate == null) {
            _publicationDate = extractPublicationDate();
        }

        return _publicationDate;
    }

    private String extractTitle() {

        final Pattern p = Pattern.compile("<title>Chromium Blog: (.+?)</title>", Pattern.MULTILINE);
        final Matcher m = p.matcher(_data);        
        if (m.find()) {
            return m.group(1)
                    .trim()
                    .replace("&amp;","&")
                    .replace("&lt;","<")
                    .replace("&gt;",">")
                    .replace("&#8211;", "–")
                    .replace("&#8212;", "—")
                    .replace("&#8216;", "‘")
                    .replace("&#8217;", "’");
         }

        ExitHelper.exit("Failed to find title in Chromium Blog page");

        // NOTREACHED
        return null;
    }

    private LocalDate extractPublicationDate() {

        final Pattern p = Pattern.compile("<span class='publishdate' itemprop='datePublished'>([^<]+)</span>", Pattern.MULTILINE);
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            final String formattedDate = m.group(1);
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, u", Locale.ENGLISH); 
            return LocalDate.parse(formattedDate, formatter);
        }

        ExitHelper.exit("Failed to find date in Chromium Blog page");

        // NOTREACHED
        return null;
    }
}
