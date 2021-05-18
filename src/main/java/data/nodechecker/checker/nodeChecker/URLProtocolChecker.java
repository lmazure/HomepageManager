package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;

public class URLProtocolChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    public URLProtocolChecker() {
        super(s_selector,
              URLProtocolChecker::checkUrl, "uses a non-normalized URL");
    }

    private static CheckStatus checkUrl(final Element e) {

        final String s = e.getTextContent();

        if(s.indexOf(':') < 0) {
            // pointer to another of my pages
            return null;
        }

        if (s.startsWith("http:") ||
            s.startsWith("https:") ||
            s.startsWith("ftp:") ||
            s.startsWith("mailto:") ||
            s.startsWith("javascript:") ||
            s.startsWith("file:")) {
            return null;
        }

        return new CheckStatus("unknown protocol for URL \"" + s + "\"");
    }
}
