package data.jsongenerator;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.LinkFormat;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.LinkProtection;
import utils.xmlparsing.LinkStatus;

/**
 *
 */
public class Link extends LinkData implements Comparable<Link> {

    private final String _sortingKey;

    /**
     * @param title
     * @param subtitles
     * @param url
     * @param status
     * @param protection
     * @param formats
     * @param languages
     * @param duration
     * @param publicationDate
     */
    public Link(final String title,
                final String[] subtitles,
                final String url,
                final Optional<LinkStatus> status,
                final Optional<LinkProtection> protection,
                final LinkFormat[] formats,
                final Locale[] languages,
                final Optional<Duration> duration,
                final Optional<TemporalAccessor> publicationDate) {
        super(title, subtitles, url, status, protection, formats, languages, duration, publicationDate);
        _sortingKey = normalizeName(url);
    }

    /**
     * @return
     */
    public String getSortingKey() {
        return _sortingKey;
    }

    /**
     * @param name name to be normalized
     * @return normalized (i.e. usable for sorting) name
     */
    private static String normalizeName(final String name) {
        final int i = name.indexOf(':');
        String str = name.substring(i + 1);
        while (str.codePointAt(0) == "/".codePointAt(0)) {
            str = str.substring(1);
        }
        return str;
    }

    @Override
    public int compareTo(final Link o) {
        return getSortingKey().compareToIgnoreCase(o.getSortingKey());
    }
}
