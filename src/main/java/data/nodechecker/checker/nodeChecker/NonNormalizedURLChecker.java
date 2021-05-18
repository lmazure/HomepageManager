package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

public class NonNormalizedURLChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    public NonNormalizedURLChecker() {
        super(s_selector,
              NonNormalizedURLChecker::checkUrl, "uses a non-normalized URL");
    }

    private static CheckStatus checkUrl(final Element e) {

        final String s = e.getTextContent();

        if (s.contains("youtube.fr")) {
            return new CheckStatus("\"youtube.fr\" should be \"youtube.com\"");
        }

        if (s.contains("fr.youtube")) {
            return new CheckStatus("\"fr.youtube\" should be \"youtube.com\"");
        }

        if (s.contains("google.fr")) {
            return new CheckStatus("\"google.fr\" should be \"google.com\"");
        }

        if (s.contains("www-128.ibm.com")) {
            return new CheckStatus("\"www-128.ibm.com\" should be \"www.ibm.com\"");
        }

        return null;
    }
}
