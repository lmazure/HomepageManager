package data.linkchecker;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.HtmlHelper;

public class QuantaMagazineLinkContentParser {

    private final String _data;
    private String _title;
    private String _subtitle;
    private LocalDate _date;

    public QuantaMagazineLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {

        if (_title == null) {
            _title = extractTitle();
        }

        return _title;
    }

    public String getSubtitle() throws ContentParserException {

        if (_subtitle == null) {
            _subtitle = extractSubtitle();
        }

        return _subtitle;
    }

    public LocalDate getDate() throws ContentParserException {

        if (_date == null) {
            _date = extractDate();
        }

        return _date;
    }

    private String extractTitle() throws ContentParserException {

        final Pattern p = Pattern.compile("<h1 class=\"post__title__title [^\"]+\" data-reactid=\"[0-9]+\">([^<]*)</h1>");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return HtmlHelper.unescape(m.group(1));
        }

        throw new ContentParserException("Failed to find title in QuantaMagazine page");
    }

    private String extractSubtitle() throws ContentParserException {

        final Pattern p = Pattern.compile("<div class=\"post__title__excerpt [^\"]+\" data-reactid=\"[0-9]+\">(.*?) *</div>");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return HtmlHelper.cleanContent(m.group(1));
        }

        throw new ContentParserException("Failed to find subtitle in QuantaMagazine page");
    }

    private LocalDate extractDate() throws ContentParserException {

        final Pattern p = Pattern.compile(",\"date\":\"([^\"]*)T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]\",\"featured_media_image\":");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return LocalDate.parse(m.group(1));
        }

        throw new ContentParserException("Failed to find date in QuantaMagazine page");
    }
}
