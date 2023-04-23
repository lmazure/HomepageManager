package fr.mazure.homepagemanager.utils.internet;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import fr.mazure.homepagemanager.utils.ExitHelper;

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
        } catch (@SuppressWarnings("unused") final URISyntaxException e) {
            return convertStringToUriSlowerButSafer(str);
        }
    }

    /**
     * Convert a string to URI, exit if the string is an invalid URI
     * This version is slow since it uses the URL class
     *
     * @param str string
     * @return URI
     */
    private static URI convertStringToUriSlowerButSafer(final String str) {
        try {
            final URL url = new URL(str);
            return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
        } catch (final URISyntaxException e) {
            ExitHelper.exit("Invalid URI", e);
            // NOTREACHED
            return null;
        } catch (MalformedURLException e) {
            ExitHelper.exit("Invalid URL", e);
            // NOTREACHED
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
}
