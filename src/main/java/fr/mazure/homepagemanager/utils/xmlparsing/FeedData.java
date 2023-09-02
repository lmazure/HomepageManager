package fr.mazure.homepagemanager.utils.xmlparsing;

/**
 * Data of a link feed
 */
public class FeedData {

    private final String _url;
    private final FeedFormat _format;

    /** Constructor
     *
     * @param url URL
     * @param format Format
     */
    public FeedData(final String url,
                    final FeedFormat format) {
        _url = url;
        _format = format;
    }

    /**
     * @return the URL
     */
    public String getUrl() {
        return _url;
    }

    /**
     * @return the format
     */
    public FeedFormat getFormat() {
        return _format;
    }

    /**
     * Convert a string into a link status
     *
     * @param format string to be converted
     * @return the format
     */
    public static FeedFormat parseFormat(final String format) {
        if (format.equals("RSS")) {
            return FeedFormat.RSS;
        }
        if (format.equals("RSS2")) {
            return FeedFormat.RSS2;
        }
        if (format.equals("Atom")) {
            return FeedFormat.Atom;
        }
        throw new UnsupportedOperationException("Illegal feed format value (" + format + ")");
    }
}
