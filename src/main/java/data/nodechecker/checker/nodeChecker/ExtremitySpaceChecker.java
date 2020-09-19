
package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.xmlparsing.NodeType;

import org.w3c.dom.Element;

public class ExtremitySpaceChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new NodeType[] {
            NodeType.BLIST,
            NodeType.CELL,
            NodeType.CLIST,
            NodeType.CODEFILE,
            NodeType.CODESAMPLE,
            NodeType.CONTENT,
            NodeType.DEFINITION2TABLE,
            NodeType.DEFINITIONTABLE,
            NodeType.DESC,
            NodeType.ITEM,
            NodeType.LLIST,
            NodeType.NLIST,
            NodeType.PAGE,
            NodeType.ROW,
            NodeType.SCRIPT,
            NodeType.SLIST,
            NodeType.TABLE,
            NodeType.TERM,
            NodeType.TEXTBLOCK
            });

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[2];
        a[0] = new NodeRule() { @Override
                                public CheckStatus checkElement(final Element e) { return checkSpaceAtBeginning(e);}
                                @Override
                                public String getDescription() { return "space at the beginning"; } };
        a[1] = new NodeRule() { @Override
                                public CheckStatus checkElement(final Element e) { return checkSpaceAtEnd(e);}
                                @Override
                                public String getDescription() { return "space at the end"; } };
        return a;
    }

    private CheckStatus checkSpaceAtBeginning(final Element e) {
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

    private CheckStatus checkSpaceAtEnd(final Element e) {
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
