package data.nodechecker;

import data.nodechecker.tagselection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

import java.util.Optional;

import org.w3c.dom.Element;

/**
*
*/
public class URLProtocolChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    /**
     * constructor
     */
    public URLProtocolChecker() {
        super(s_selector,
              URLProtocolChecker::checkUrl, "uses an unknown protocol");
    }

    private static CheckStatus checkUrl(final Element e) {

        final String url = e.getTextContent();

        if (url.indexOf(':') < 0) {
            // pointer to another of my pages
            return null;
        }

        if (url.startsWith("http:") ||
            url.startsWith("https:") ||
            url.startsWith("ftp:") ||
            url.startsWith("mailto:") ||
            url.startsWith("javascript:") ||
            url.startsWith("file:")) {
            return null;
        }

        return new CheckStatus("UnkownProtocolInUrl",
                               "unknown protocol for URL \"" + url + "\"",
                               Optional.empty());
    }
}
