package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHelper {

    public static String getFirstLevelTextContent(final Node node) {
        final NodeList list = node.getChildNodes();
        StringBuilder textContent = new StringBuilder();
        for (int i = 0; i < list.getLength(); i++) {
            final Node child = list.item(i);
            if (child.getNodeType() == Node.TEXT_NODE)
                textContent.append(child.getTextContent());
        }
        return textContent.toString();
    }
}
