package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    static final Pattern s_indentationPattern = Pattern.compile("\\n +");
    
    public DoubleSpaceChecker() {
        super(s_selector,
              DoubleSpaceChecker::checkDoubleSpace, "double space is present");
    }

    private static CheckStatus checkDoubleSpace(final Element e) {

        // ignore titles of articles
        if (XmlHelper.isOfType(e, ElementType.T) &&
            XmlHelper.isOfType(e.getParentNode(), ElementType.X) &&
            XmlHelper.isOfType(e.getParentNode().getParentNode(), ElementType.ARTICLE)) {
            return null;
        }


        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) {
            return null;
        }

        for (final String l: list) {
            final Matcher matcher = s_indentationPattern.matcher(l);
            final String str = matcher.replaceFirst("");
            if (str.indexOf("  ") >= 0) {
                return new CheckStatus("\"" + e.getTextContent() + "\" should not contain a double space");
            }
        }

        return null;
    }
}
