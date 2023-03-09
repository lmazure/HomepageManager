package fr.mazure.homepagemanager.utils.internet;

import java.net.MalformedURLException;
import java.net.URL;

/**
* Helper to manager URLs
*/
public class UrlHelper {

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

    /**
     * Remove a query parameter from a URL
     * 
     * @param url URL
     * @param parameter Query parameter to remove
     * @return Resulting URL
     */
    public static String removeQueryParameter(final String url,
                                              final String parameter) {
        return url.replaceFirst("(\\?|&)(" + parameter + "=[^&]*(&|$))", "$1")
                  .replaceFirst("(\\?|&)$","");
   }

    /**
     * Return the hos from a URL
     *
     * @param url URL
     * @return Hosr
     */
    public static String getHost(final String url) {
        if (url.startsWith("..")) {
            return null;
        }
        final URL u = convertStringToUrl(url);
        return u.getHost();
    }

    /**
     * Convert a string to URL, throw IllegalArgumentException if the string is an invalid URL
     *
     * @param str String
     * @return URL
     */
    public static URL convertStringToUrl(final String str) {
        try {
            return new URL(str);
        } catch (@SuppressWarnings("unused") final MalformedURLException e) {
            throw new IllegalArgumentException("Cannot convert string to URL: '" + str + "'");
        }
    }

    /**
     * Check if a String is a valid URL
     * 
     * @param str String
     * @return True if valid URL, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean isValidUrl(final String str) {
        try {
            new URL(str);
            return true;
        } catch (final MalformedURLException e) {
            return false;
        }
    }
}
