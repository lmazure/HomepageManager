package data.linkchecker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class BaeldungLinkContentParser {

    private final String _data;
    private String _title;
    private LocalDate _date;
    private Optional<AuthorData> _author;

    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US);

    public BaeldungLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {

        if (_title == null) {
            _title = extractTitle();
        }

        return _title;
    }

    public LocalDate getDate() throws ContentParserException {

        if (_date == null) {
            _date = extractDate();
        }

        return _date;
    }

    public Optional<AuthorData> getAuthor() throws ContentParserException {

        if (_author == null) {
            _author = extractAuthor();
        }

        return _author;
    }

    private String extractTitle() throws ContentParserException {

        final Pattern p = Pattern.compile("<h1 class=\"single-title entry-title\" itemprop=\"headline\">([^<]*)</h1>");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return HtmlHelper.unescape(m.group(1));
        }

        throw new ContentParserException("Failed to find title in Baeldung page");
    }

    private LocalDate extractDate() throws ContentParserException {

        final Pattern p = Pattern.compile("<p class=\"post-modified\">Last modified: <span class=\"updated\">([^<]*)</span></p>");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            try {
                return LocalDate.parse(m.group(1), s_formatter);
            } catch (final DateTimeParseException e) {
                throw new ContentParserException("Failed to parse date (" + m.group(1) + ") in Baeldung page", e);
            }
        }

        throw new ContentParserException("Failed to find date in Baeldung page");
    }


    private Optional<AuthorData> extractAuthor() throws ContentParserException {

        final Pattern p = Pattern.compile("<a href=\"https://www.baeldung.com/author/[^/]*/\" title=\"Posts by [^\"]*\" rel=\"author\">([^<]*)</a>");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            final String match = m.group(1);
            if (match.equals("baeldung")) {
                return Optional.empty();
            }
            final String[] nameParts = HtmlHelper.cleanContent(match).split(" ");
            if (nameParts.length != 2) {
                throw new ContentParserException("Failed to parse author name (" + match + ")in Baeldung page");
            }
            return Optional.of(new AuthorData(Optional.empty(),
                                              Optional.of(nameParts[0]),
                                              Optional.empty(),
                                              Optional.of(nameParts[1]),
                                              Optional.empty(),
                                              Optional.empty()));
        }

        throw new ContentParserException("Failed to find author in Baeldung page");
    }
}
