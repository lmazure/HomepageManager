package fr.mazure.homepagemanager.data.nodechecker;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.ExclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 *
 */
public class DoubleSpaceChecker extends NodeChecker {

    private static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.BLIST,
            ElementType.CLIST,
            ElementType.CODESAMPLE,
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

    private static final Pattern s_indentationPattern2 = Pattern.compile("^\\n +|\\n +$");

    /**
    * constructor
    */
    public DoubleSpaceChecker() {
        super(s_selector,
              DoubleSpaceChecker::checkDoubleSpace, "double space is present");
    }

    private static CheckStatus checkDoubleSpace(final Element e) {

        // ignore titles of articles
        if ((XmlHelper.isOfType(e, ElementType.T) || XmlHelper.isOfType(e, ElementType.ST)) &&
            XmlHelper.isOfType(e.getParentNode(), ElementType.X) &&
            XmlHelper.isOfType(e.getParentNode().getParentNode(), ElementType.ARTICLE)) {
            return null;
        }

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.isEmpty()) {
            return null;
        }

        for (final String l: list) {
            final String str = s_indentationPattern2.matcher(l).replaceAll("");
            if (str.contains("  ")) {
                return new CheckStatus("DoubleSpace", "\"" + e.getTextContent() + "\" should not contain a double space (in \"" + l + "\")", Optional.empty());
            }
        }

        return null;
    }
}
