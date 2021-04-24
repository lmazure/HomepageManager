package data.linkchecker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.JsonHelper;

public class MediumLinkContentParser {

    private final String _data;

    public MediumLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        final Pattern p = Pattern.compile("\"primaryTopic\":(null|\\{[^}]+\\}),(\"viewerEdge\":\\{[^}]+\\},)?\"title\":\"([^\"]+)\"");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return JsonHelper.unescapeString(m.group(3)).replace("\n"," ");
        }

        throw new ContentParserException("Failed to find title in Medium page");
    }

    public LocalDate getPublicationDate() throws ContentParserException {
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
