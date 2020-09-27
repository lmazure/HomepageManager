package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.ElementType;

import java.util.List;

import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
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

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[] = new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkDoubleSpace(e);}
                            @Override
                            public String getDescription() { return "double space is present"; } };
        return a;
    }

    private CheckStatus checkDoubleSpace(final Element e) {

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
