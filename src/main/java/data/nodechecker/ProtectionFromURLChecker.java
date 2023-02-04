package data.nodechecker;

import data.nodechecker.tagselection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
*
*/
public class ProtectionFromURLChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.X
            });

    /**
    * constructor
    */
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

        if (url.contains("auntminnie.com/") && !protection.equals("free_registration"))
           return new CheckStatus("MissingRegistrationIndicator",
                                  "\"" + url + "\" should be flagged as 'free_registration'",
                                  Optional.empty());

        return null;
    }
}
