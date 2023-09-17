package fr.mazure.homepagemanager.utils.internet;

import java.net.URI;
import java.net.URISyntaxException;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;

/**
 *
 */
public class UriHelper {

    /**
     * Return the host from a URI
     *
     * @param uri URI
     * @return Host
     */
    public static String getHost(final String uri) {
        if (uri.startsWith("..")) {
            return null;
        }
        final URI u = convertStringToUri(uri);
        return u.getHost();
    }

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
            Logger.log(Level.ERROR)
                  .appendln("Invalid URI (" + str + ")")
                  .append(e)
                  .submit();
            return null;
        }
    }

    /**
     * Build an URI from its components
     *
     * @param scheme scheme
     * @param host host
     * @param path path
     * @return the built URI
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

    /**
     * Check if a String is a valid URI
     *
     * @param str String
     * @return True if valid URL, false otherwise
     */
    public static boolean isValidUri(final String str) {
        try {
            @SuppressWarnings("unused") final URI u = new URI(str);
            return true;
        } catch (@SuppressWarnings("unused") final URISyntaxException e) {
            return false;
        }
    }
}
