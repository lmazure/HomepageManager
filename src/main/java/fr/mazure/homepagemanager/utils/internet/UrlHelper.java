package fr.mazure.homepagemanager.utils.internet;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import fr.mazure.homepagemanager.utils.ExitHelper;

/**
 * Helper to manager URLs
 */
public class UrlHelper {

    /**
     * Encode a string to be included in a URL
     *
     * @param part String to encode
     * @return Encoded string
     */
    public static String encodeUrlPart(final String part) {
        try {
            return  URLEncoder.encode(part, StandardCharsets.UTF_8.toString());
        } catch (final UnsupportedEncodingException e) {
            ExitHelper.exit(e);
            return null;
        }
    }

    /**
     * Remove some query parameters from a URL
     *
     * @param url URL
     * @param parameters Query parameters to remove
     * @return Resulting URL
     */
    public static String removeQueryParameters(final String url,
                                               final String ...parameters) {
        String u = url;
        for (final String p: parameters) {
            u = removeQueryParameter(u, p);
        }
        return u;
    }

    private static String removeQueryParameter(final String url,
                                               final String parameter) {
        return url.replaceFirst("(\\?|&)(" + parameter + "=[^&]*(&|$))", "$1")
                  .replaceFirst("(\\?|&)$","");
   }

    /**
     * Convert a string to URL, throw IllegalArgumentException if the string is an invalid URL
     *
     * @param string String
     * @return URL
     */
    public static URL convertStringToUrl(final String string) {
        try {
            return UriHelper.convertStringToUri(string).toURL();
        } catch (@SuppressWarnings("unused") final MalformedURLException e) {
            throw new IllegalArgumentException("Cannot convert string to URL: '" + string + "'");
        }
    }
}
