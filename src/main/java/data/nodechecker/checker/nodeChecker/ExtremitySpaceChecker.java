
package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;

public class ExtremitySpaceChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.BLIST,
            ElementType.CELL,
            ElementType.CLIST,
            ElementType.CODEFILE,
            ElementType.CODESAMPLE,
            ElementType.CONTENT,
            ElementType.DEFINITION2TABLE,
            ElementType.DEFINITIONTABLE,
            ElementType.DESC,
            ElementType.ITEM,
            ElementType.LLIST,
            ElementType.NLIST,
            ElementType.PAGE,
            ElementType.ROW,
            ElementType.SCRIPT,
            ElementType.SLIST,
            ElementType.TABLE,
            ElementType.TERM,
            ElementType.TEXTBLOCK
            });

    public ExtremitySpaceChecker() {
        super(s_selector,
              ExtremitySpaceChecker::checkSpaceAtBeginning, "space present at the beginning",
              ExtremitySpaceChecker::checkSpaceAtEnd, "space present at the end");
    }

    private static CheckStatus checkSpaceAtBeginning(final Element e) {
        final String s = e.getTextContent();
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
        final String s = e.getTextContent();
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
