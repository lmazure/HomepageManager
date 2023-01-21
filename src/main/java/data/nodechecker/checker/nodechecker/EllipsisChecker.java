package data.nodechecker.checker.nodechecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.ExclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

import java.util.List;

import org.w3c.dom.Element;

/**
*
*/
public class EllipsisChecker extends NodeChecker {

    private static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
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

    /**
    * constructor
    */
    public EllipsisChecker() {
        super(s_selector,
             EllipsisChecker::checkEllipsis, "ellipsis is improperly encoded",
             EllipsisChecker::checkDoubleDot, "double dot is present",
             EllipsisChecker::checkApostrophe, "apostrophe should be used");
    }

    private static CheckStatus checkEllipsis(final Element e) {

        final String s = e.getTextContent();
        if (s.indexOf("...") == -1) {
            return null;
        }

        return new CheckStatus("\"" + s + "\" should not contain \"...\"");
    }

    private static CheckStatus checkDoubleDot(final Element e) {

        final List<String> content = XmlHelper.getFirstLevelTextContent(e);
        for (final String s: content) {
            if (s.indexOf("..") >= 0) {
                return new CheckStatus("\"" + s + "\" contains \"..\"");
            }
        }
        return null;
    }

    private static CheckStatus checkApostrophe(final Element e) {

        final String s = e.getTextContent();
        if (s.indexOf("'s ") >= 0) {
            return new CheckStatus("\"'s \" should be \"’s ");
        }
        if (s.indexOf("s' ") >= 0) {
            return new CheckStatus("\"s' \" should be \"s’ ");
        }
        if (s.indexOf("x' ") >= 0) {
            return new CheckStatus("\"x' \" should be \"x’ ");
        }
        if (s.indexOf("z' ") >= 0) {
            return new CheckStatus("\"z' \" should be \"z’ ");
        }

        return null;
    }

}
