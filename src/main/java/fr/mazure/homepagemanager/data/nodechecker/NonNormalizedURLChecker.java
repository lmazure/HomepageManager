package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
 *
 */
public class NonNormalizedURLChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    /**
    * constructor
    */
    public NonNormalizedURLChecker() {
        super(s_selector,
              NonNormalizedURLChecker::checkUrl, "uses a non-normalized URL",
              NonNormalizedURLChecker::checkNoDoubleSlash, "contains a double slash",
              NonNormalizedURLChecker::checkNoFinalSlash, "finishes with a slash");
    }

    private static CheckStatus checkUrl(final Element element) {

        final String url = element.getTextContent();

        if (url.contains("youtube.fr")) {
            return new CheckStatus("ImproperUrlEncoding", "\"youtube.fr\" should be \"youtube.com\"", Optional.empty());
        }

        if (url.contains("fr.youtube")) {
            return new CheckStatus("ImproperUrlEncoding", "\"fr.youtube\" should be \"youtube.com\"", Optional.empty());
        }

        if (url.contains("google.fr")) {
            return new CheckStatus("ImproperUrlEncoding", "\"google.fr\" should be \"google.com\"", Optional.empty());
        }

        if (url.contains("www.martinfowler.com")) {
            return new CheckStatus("ImproperUrlEncoding", "\"www.martinfowler.com\" should be \"martinfowler.com\"", Optional.empty());
        }

        if (url.contains("blogs.oracle.com/javamagazine") && !url.contains("/post/") && !url.endsWith("/javamagazine/")) {
            return new CheckStatus("ImproperUrlEncoding", "\"blogs.oracle.com\" should contain \"/post/\"", Optional.empty());
        }

        return null;
    }

    private static CheckStatus checkNoDoubleSlash(final Element element) {

        final String url = element.getTextContent();
        if (url.startsWith("https://web.archive.org/web/") ||
            url.startsWith("http://static.googleusercontent.com/")) {
            return null;
        }

        final String urlWithoutProtocol = url.replaceFirst("^[a-z]+://", "");
        if (urlWithoutProtocol.contains("//")) {
            return new CheckStatus("ImproperUrl", "URL \"" + url + "\"contains \"//\"", Optional.empty());
        }

        return null;
    }

    private static CheckStatus checkNoFinalSlash(final Element element) {

        final String url = element.getTextContent();
        if (!url.startsWith("https://lexfridman.com/")) {
            return null;
        }

        if (url.endsWith("/")) {
            return new CheckStatus("ImproperUrl", "URL \"" + url + "\" should not ends with a slash", Optional.empty());
        }

        return null;
    }
}
