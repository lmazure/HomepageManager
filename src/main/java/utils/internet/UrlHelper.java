package utils.internet;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlHelper {

    public static String removeQueryParameters(final String url,
                                               final String ...parameters) {
        String u = url;
        for (final String p: parameters) {
            u = removeQueryParameter(u, p);
        }
        return u;
    }

    public static String removeQueryParameter(final String url,
                                              final String parameter) {
        return url.replaceFirst("(\\?|&)(" + parameter + "=[^&]*(&|$))", "$1")
                  .replaceFirst("(\\?|&)$","");
   }

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
     * @param str
     * @return
     */
    public static URL convertStringToUrl(final String str) {
        try {
            return new URL(str);
        } catch (@SuppressWarnings("unused") final MalformedURLException e) {
            throw new IllegalArgumentException("Cannot convert string to URL: '" + str + "'");
        }
    }

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
