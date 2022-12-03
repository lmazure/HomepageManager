package data.nodechecker.checker.nodechecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.ExclusionTagSelector;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
*
*/
public class ExtremitySpaceChecker extends NodeChecker {

    private static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.BLIST,
            ElementType.CELL,
            ElementType.CLIST,
            ElementType.CODEFILE,
            ElementType.CODESAMPLE,
            ElementType.CONTENT,
            ElementType.DEFINITION2TABLE,
            ElementType.DEFINITIONTABLE,
            ElementType.ITEM,
            ElementType.LLIST,
            ElementType.NLIST,
            ElementType.PAGE,
            ElementType.ROW,
            ElementType.SCRIPT,
            ElementType.SLIST,
            ElementType.TABLE,
            ElementType.TEXTBLOCK
            });

    /**
    * constructor
    */
    public ExtremitySpaceChecker() {
        super(s_selector,
              ExtremitySpaceChecker::checkSpaceAtBeginning, "space present at the beginning",
              ExtremitySpaceChecker::checkSpaceAtEnd, "space present at the end");
    }

    private static CheckStatus checkSpaceAtBeginning(final Element e) {
        final NodeList list = e.getChildNodes();
        if (list.getLength() == 0) {
            return checkSpaceAtBeginningInternal(e.getTextContent());
        }
        if (list.item(0).getNodeType() == Node.TEXT_NODE) {
            return checkSpaceAtBeginningInternal(list.item(0).getTextContent());
        }
        return null;
    }

    private static CheckStatus checkSpaceAtBeginningInternal(final String s) {
        if (s.length() == 0) {
            return null;
        }
        char c = s.charAt(0);
        if (!Character.isWhitespace(c)) {
            return null;
        }
        return new CheckStatus("\"" + s + "\" should not begin with a space");
    }

    private static CheckStatus checkSpaceAtEnd(final Element e) {
        final NodeList list = e.getChildNodes();
        if (list.getLength() == 0) {
            return checkSpaceAtEndInternal(e.getTextContent());
        }
        if (list.item(list.getLength() - 1).getNodeType() == Node.TEXT_NODE) {
            return checkSpaceAtEndInternal(list.item(list.getLength() - 1).getTextContent());
        }
        return null;
    }

    private static CheckStatus checkSpaceAtEndInternal(final String s) {
        if (s.length() == 0) {
            return null;
        }
        char c = s.charAt(s.length()-1);
        if (!Character.isWhitespace(c)) {
            return null;
        }
        return new CheckStatus("\"" + s + "\" should not end with a space");
    }
}
