package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import utils.XMLHelper;
import utils.xmlparsing.ElementType;

import java.util.List;

import org.w3c.dom.Element;

public class EllipsisChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.A,
            ElementType.ARTICLE,
            ElementType.BLIST,
            ElementType.CELL,
            ElementType.CLIST,
            ElementType.CODESAMPLE,
            ElementType.CONTENT,
            ElementType.I,
            ElementType.ITEM,
            ElementType.LLIST,
            ElementType.NLIST,
            ElementType.PAGE,
            ElementType.ROW,
            ElementType.SCRIPT,
            ElementType.ST,
            ElementType.T,
            ElementType.X
            });

    public EllipsisChecker() {
        super(s_selector,
             EllipsisChecker::checkEllipsis, "ellipsis is improperly encoded",
             EllipsisChecker::checkDoubleDot, "double dot is present");
    }

    private static CheckStatus checkEllipsis(final Element e) {

        final String s = e.getTextContent();
        if (s.indexOf("...") == -1) {
            return null;
        }

        return new CheckStatus("\"" + s + "\" should not contain \"...\"");
    }

    private static CheckStatus checkDoubleDot(final Element e) {

        final List<String> content = XMLHelper.getFirstLevelTextContent(e);
        for (final String s: content) {
            if (s.indexOf("..") >= 0) {
                return new CheckStatus("\"" + s + "\" contains \"..\"");
            }
        }
        return null;
    }
}
