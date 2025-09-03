package fr.mazure.homepagemanager.utils.internet;

import java.net.URI;
import java.net.URISyntaxException;

import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;

/**
 *
 */
public class UriHelper {

    /**
     * Return the scheme from a URI
     *
     * @param uri URI
     * @return Host
     */
    public static String getScheme(final String uri) {
        if (uri.startsWith("..")) {
            return null;
        }
        final URI u = convertStringToUri(uri);
        return u.getScheme();
    }

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
     * Return the path from a URI
     *
     * @param uri URI
     * @return Path
     */
    public static String getPath(final String uri) {
        try {
            return new URI(uri).getPath();
        } catch (final URISyntaxException _) {
            return null;
        }
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
     * Check if a String is a valid URI
     *
     * @param str String
     * @return True if valid URL, false otherwise
     */
    public static boolean isValidUri(final String str) {
        try {
            @SuppressWarnings("unused") final URI u = new URI(str);
            return true;
        } catch (final URISyntaxException _) {
            return false;
        }
    }
}
