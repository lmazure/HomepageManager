package data.linkchecker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;

public class MediumLinkContentParser {

    private final String _data;
    private String _title;
    private LocalDate _publishDate;

    public MediumLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() {

        if (_title == null) {
            _title = extractTitle();
        }

        return _title;
    }

    public LocalDate getPublishDate() {

        if (_publishDate == null) {
            _publishDate = extractDate();
        }

        return _publishDate;
    }

    private String extractTitle() {

        final Pattern p = Pattern.compile("<h1[^>]+>(.+?)</h1>");
        final Matcher m = p.matcher(_data);        
        if (m.find()) {
            return m.group(1)
                    .replaceAll("<.+?>","") // remove HTML that may be in the title
                    .replace("&amp;","&")
                    .replace("&lt;","<")
                    .replace("&gt;",">");
         }

        ExitHelper.exit("Failed to find title in Medium page");

        // NOTREACHED
        return null;
    }


    private LocalDate extractDate() {

        final Pattern p = Pattern.compile("\"datePublished\":\"([^\"]*)\",\"dateModified\":\"[^\"]*\",\"headline\":\"[^\"]*\"");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            final String formattedDate = m.group(1);
            final Instant instant = Instant.parse(formattedDate);
            return LocalDate.ofInstant(instant, ZoneOffset.UTC);
        }

        ExitHelper.exit("Failed to find date in Medium page");

        // NOTREACHED
        return null;
    }
}
