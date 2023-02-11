package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.ExclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
*
*/
public class SpaceBetweenTagsChecker extends NodeChecker {

    private static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
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

    /**
    * constructor
    */
    public SpaceBetweenTagsChecker() {
        super(s_selector,
                SpaceBetweenTagsChecker::checkNoSpace, "element should not contain space between tags");
    }

    private static CheckStatus checkNoSpace(final Element e) {

        for (int i = 0; i < e.getChildNodes().getLength(); i++) {
            final Node child = e.getChildNodes().item(i);
            if ((child.getNodeType() == Node.TEXT_NODE) && child.getTextContent().trim().isEmpty()) {
                return new CheckStatus("SpaceBetweenTags",
                                       "node " + e.getNodeName() + " (" + e.getTextContent() + ") shall not contain space between tags",
                                       Optional.empty());
            }
        }
        return null;
    }
}
