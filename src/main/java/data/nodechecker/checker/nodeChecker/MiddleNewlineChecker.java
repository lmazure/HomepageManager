package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.xmlparsing.NodeType;

import org.w3c.dom.Element;

public class MiddleNewlineChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new NodeType[] {
            NodeType.B,
            NodeType.BLIST,
            NodeType.CELL,
            NodeType.CLIST,
            NodeType.CODESAMPLE,
            NodeType.CONTENT,
            NodeType.DEFINITION2TABLE,
            NodeType.DEFINITIONTABLE,
            NodeType.DESC,
            NodeType.I,
            NodeType.ITEM,
            NodeType.LLIST,
            NodeType.NLIST,
            NodeType.PAGE,
            NodeType.ROW,
            NodeType.SCRIPT,
            NodeType.SLIST,
            NodeType.SMALL,
            NodeType.TABLE,
            NodeType.TERM,
            NodeType.TERM1,
            NodeType.TEXTBLOCK
            });

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkNewline(e);}
                            @Override
                            public String getDescription() { return "newline"; } };
        return a;
    }

    private CheckStatus checkNewline(final Element e) {
        final String s = e.getTextContent();
        if (s.indexOf('\n') == -1) return null;
        return new CheckStatus("\"" + s + "\" should not contain a newline");
    }
}
