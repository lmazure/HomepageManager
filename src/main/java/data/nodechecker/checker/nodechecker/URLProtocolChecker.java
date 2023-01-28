package data.nodechecker.checker.nodechecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;

/**
*
*/
public class URLProtocolChecker extends NodeChecker {

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
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

        if(url.indexOf(':') < 0) {
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

        return new CheckStatus("UnkownProtoclInUrl",
                               "unknown protocol for URL \"" + url + "\"");
    }
}
