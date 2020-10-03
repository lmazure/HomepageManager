package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;

public class MiddleNewlineChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.B,
            ElementType.BLIST,
            ElementType.CELL,
            ElementType.CLIST,
            ElementType.CODESAMPLE,
            ElementType.CONTENT,
            ElementType.DEFINITION2TABLE,
            ElementType.DEFINITIONTABLE,
            ElementType.DESC,
            ElementType.I,
            ElementType.ITEM,
            ElementType.LLIST,
            ElementType.NLIST,
            ElementType.PAGE,
            ElementType.ROW,
            ElementType.SCRIPT,
            ElementType.SLIST,
            ElementType.SMALL,
            ElementType.TABLE,
            ElementType.TERM,
            ElementType.TERM1,
            ElementType.TEXTBLOCK
            });

    public MiddleNewlineChecker() {
        super(s_selector,
              MiddleNewlineChecker::checkNewline, "newline is the middle of the string");
    }

    private static CheckStatus checkNewline(final Element e) {
        final String s = e.getTextContent();
        if (s.indexOf('\n') == -1) return null;
        return new CheckStatus("\"" + s + "\" should not contain a newline");
    }
}
