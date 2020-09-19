package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.NodeType;

import java.util.List;

import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class DoubleSpaceChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new NodeType[] {
            NodeType.BLIST,
            NodeType.CLIST,
            NodeType.CODESAMPLE,
            NodeType.CONTENT,
            NodeType.DEFINITIONTABLE,
            NodeType.ITEM,
            NodeType.LLIST,
            NodeType.NLIST,
            NodeType.PAGE,
            NodeType.ROW,
            NodeType.SCRIPT,
            NodeType.SLIST,
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
        public CheckStatus checkElement(final Element e) { return checkDoubleSpace(e);}
                            @Override
                            public String getDescription() { return "double space is present"; } };
        return a;
    }

    private CheckStatus checkDoubleSpace(final Element e) {

        // ignore titles of articles
        if (XMLHelper.isOfType(e, NodeType.T) &&
            XMLHelper.isOfType(e.getParentNode(), NodeType.X) &&
            XMLHelper.isOfType(e.getParentNode().getParentNode(), NodeType.ARTICLE)) {
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
