package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import utils.XMLHelper;
import utils.xmlparsing.ElementType;

import java.util.List;

import org.w3c.dom.Element;

public class DoubleSpaceChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.BLIST,
            ElementType.CLIST,
            ElementType.CODESAMPLE,
            ElementType.CONTENT,
            ElementType.DEFINITIONTABLE,
            ElementType.ITEM,
            ElementType.LLIST,
            ElementType.NLIST,
            ElementType.PAGE,
            ElementType.ROW,
            ElementType.SCRIPT,
            ElementType.SLIST,
            ElementType.TEXTBLOCK
            });

    public DoubleSpaceChecker() {
        super(s_selector,
              DoubleSpaceChecker::checkDoubleSpace, "double space is present");
    }

    private static CheckStatus checkDoubleSpace(final Element e) {

        // ignore titles of articles
        if (XMLHelper.isOfType(e, ElementType.T) &&
            XMLHelper.isOfType(e.getParentNode(), ElementType.X) &&
            XMLHelper.isOfType(e.getParentNode().getParentNode(), ElementType.ARTICLE)) {
            return null;
        }


        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) {
            return null;
        }

        for (final String l: list) {
            if (l.indexOf("  ") >= 0) {
                if (!l.matches("\\n +")) {
                    return new CheckStatus("\"" + e.getTextContent() + "\" should not contain a double space");
                }
            }
        }

        return null;
    }
}
