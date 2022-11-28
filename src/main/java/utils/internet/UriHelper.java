package utils.internet;

import java.net.URI;
import java.net.URISyntaxException;

import utils.ExitHelper;

/**
 *
 */
public class UriHelper {

    /**
     * Convert a string to URI, exit if the string is an invalid URI
     *
     * @param str string
     * @return URI
     */
    public static URI convertStringToUri(final String str) {
        try {
            return new URI(str);
        } catch (final URISyntaxException e) {
            ExitHelper.exit("Invalid URI", e);
            // NOTREACHED
            return null;
        }
    }

    /**
     * Build an URI from its components
     *
     * @param scheme
     * @param host
     * @param path
     * @return
     */
    public static URI buildUri(final String scheme,
                               final String host,
                               final String path) {
        try {
            return new URI(scheme, host, path, null);
        } catch (final URISyntaxException e) {
            ExitHelper.exit("Invalid URI (scheme = \"" + scheme + "\", host = \"" + host + "\", path = \"" + path + "\")", e);
            // NOTREACHED
            return null;
        }
    }
}
