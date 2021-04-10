package data.linkchecker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.JsonHelper;

public class MediumLinkContentParser {

    private final String _data;
    private String _title;
    private LocalDate _publicationDate;

    public MediumLinkContentParser(final String data) {
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

        /*
        final Pattern p = Pattern.compile("<h1[^>]+>(.+?)</h1>");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return HtmlHelper.unescape(m.group(1)
                                        .replaceAll("<br/>"," ") // replace newline by space
                                        .replaceAll("<.+?>","")); // remove other HTML that may be in the title
         }
         */

        final Pattern p = Pattern.compile("\"primaryTopic\":(null|\\{[^}]+\\}),\"title\":\"([^\"]+)\"");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return JsonHelper.unescapeString(m.group(2)).replace("\n"," ");
        }

        throw new ContentParserException("Failed to find title in Medium page");
    }

    private LocalDate extractPublicationDate() throws ContentParserException {

        /*
        final Pattern p = Pattern.compile("\"datePublished\":\"([^\"]*)\",\"dateModified\":\"[^\"]*\",\"headline\":\"[^\"]*\"");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            final String formattedDate = m.group(1);
            final Instant instant = Instant.parse(formattedDate);
            return LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris"));
        }
        */

        final Pattern p = Pattern.compile("\"firstPublishedAt\":(\\d+),[^{]+\"primaryTopic\":");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            final String i = m.group(1);
            final Instant instant = Instant.ofEpochMilli(Long.parseLong(i));
            return LocalDate.ofInstant(instant, ZoneId.of("Europe/Paris"));
        }

        throw new ContentParserException("Failed to find date in Medium page");
    }
}
