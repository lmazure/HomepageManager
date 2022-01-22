package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ProtectionFromURLChecker extends NodeChecker {

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.X
            });

    public ProtectionFromURLChecker() {
        super(s_selector,
              ProtectionFromURLChecker::checkProtection, "given the URL, the protection is incorrect");
    }

    private static CheckStatus checkProtection(final Element e) {

        String url = "";
        String protection = "";

        final NodeList children = e.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            final Node child = children.item(j);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (XmlHelper.isOfType(child, ElementType.A)) {
                    url = child.getTextContent();
                }
            }
        }

        final Node statusAttribute = e.getAttributeNode("protection");
        if (statusAttribute != null) {
            protection = statusAttribute.getTextContent();
        }

        // TODO "free_registration" should not be hardcoded
        if (url.contains("auntminnie.com/") && !protection.equals("free_registration"))
           return new CheckStatus("\"" + url + "\" should be flagged as 'free_registration'");

        return null;
    }
}
