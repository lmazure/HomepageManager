package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import utils.xmlparsing.ElementType;

public class SpaceBetweenTagsChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.BLIST,
            ElementType.CELL,
            ElementType.CLIST,
            ElementType.CODESAMPLE,
            ElementType.COMMENT,
            ElementType.CONTENT,
            ElementType.DEFINITIONTABLE,
            ElementType.DEFINITION2TABLE,
            ElementType.DESC,
            ElementType.ITEM,
            ElementType.LLIST,
            ElementType.NLIST,
            ElementType.PAGE,
            ElementType.ROW,
            ElementType.SLIST,
            ElementType.TABLE,
            ElementType.TERM,
            ElementType.TEXTBLOCK
            });

    public SpaceBetweenTagsChecker() {
        super(s_selector,
                SpaceBetweenTagsChecker::checkNoSpacey, "element should not contain space between tags");
    }

    private static CheckStatus checkNoSpacey(final Element e) {

        for (int i = 0; i < e.getChildNodes().getLength(); i++) {
            final Node child = e.getChildNodes().item(i);
            if ((child.getNodeType() == Node.TEXT_NODE) && child.getTextContent().trim().isEmpty()) {
                return new CheckStatus("node " + e.getNodeName() + " (" + e.getTextContent() + ") shall not be contain space between tags");
            }
        }
        return null;
    }
}
