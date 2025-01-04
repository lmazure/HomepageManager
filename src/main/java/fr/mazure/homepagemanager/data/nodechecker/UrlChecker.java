package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
 *
 */
public class UrlChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    /**
     * constructor
     */
    public UrlChecker() {
        super(s_selector,
              UrlChecker::checkProtocol, "uses an unknown protocol",
              UrlChecker::checkLastCharacter, "ends with a qustion mark");
    }

    private static CheckStatus checkProtocol(final Element e) {

        final String url = e.getTextContent();

        if (!url.contains(":")) {
            // pointer to another page
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

    private static CheckStatus checkLastCharacter(final Element e) {

        final String url = e.getTextContent();

        if (url.endsWith("?")) {
            return new CheckStatus("BadUrl",
                                   "URL \"" + url + "\" ends with a question mark",
                                   Optional.empty());
        }

        return null;
    }
}
