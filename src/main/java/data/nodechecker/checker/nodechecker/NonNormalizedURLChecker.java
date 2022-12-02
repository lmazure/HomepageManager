package data.nodechecker.checker.nodechecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

/**
*
*/
public class NonNormalizedURLChecker extends NodeChecker {

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    /**
    * constructor
    */
    public NonNormalizedURLChecker() {
        super(s_selector,
              NonNormalizedURLChecker::checkUrl, "uses a non-normalized URL",
              NonNormalizedURLChecker::checkNoDoubleSlash, "contains a double slash");
    }

    private static CheckStatus checkUrl(final Element element) {

        final String url = element.getTextContent();

        if (url.contains("youtube.fr")) {
            return new CheckStatus("\"youtube.fr\" should be \"youtube.com\"");
        }

        if (url.contains("fr.youtube")) {
            return new CheckStatus("\"fr.youtube\" should be \"youtube.com\"");
        }

        if (url.contains("google.fr")) {
            return new CheckStatus("\"google.fr\" should be \"google.com\"");
        }

        if (url.contains("www-128.ibm.com")) {
            return new CheckStatus("\"www-128.ibm.com\" should be \"www.ibm.com\"");
        }

        if (url.contains("blogs.oracle.com") && url.contains("/post/")) {
            return new CheckStatus("\"blogs.oracle.com\" should not contain \"/post/\"");
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
            return new CheckStatus("URL \"" + url + "\"contains \"//\"");
        }

        return null;
    }
}
