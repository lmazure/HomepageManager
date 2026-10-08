package fr.mazure.homepagemanager.utils.internet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.FeedFormat;

/**
 * Methods to manage feeds
 */
public class FeedHelper {

    // name of the first XML element (<?xml ...?>, <!-- ... --> and <!DOCTYPE ...> are skipped
    // since '?' and '!' are not matched by the leading letter class)
    private static final Pattern s_rootElement = Pattern.compile("<(\\p{Alpha}[\\p{Alnum}:_.-]*)");

    /**
     * Determine the format of a feed from its content
     *
     * @param data content of the feed
     * @return format of the feed
     */
    public static FeedFormat getFormat(final String data) {
        final Matcher matcher = s_rootElement.matcher(data);
        if (!matcher.find()) {
            throw new IllegalArgumentException("no XML element found in feed content");
        }
        final String rootTag = matcher.group(1);
        if (rootTag.equals("feed")) {
            return FeedFormat.Atom;
        }
        if (rootTag.equals("rss")) {
            return FeedFormat.RSS2;
        }
        if (rootTag.equals("RDF") || rootTag.endsWith(":RDF")) {
            // RSS 1.0 (RDF), the root element is typically <rdf:RDF>
            return FeedFormat.RSS;
        }
        throw new IllegalArgumentException("unrecognized feed format (root element: " + rootTag + ")");
    }
}
