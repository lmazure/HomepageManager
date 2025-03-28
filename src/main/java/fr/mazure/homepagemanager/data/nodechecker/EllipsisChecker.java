package fr.mazure.homepagemanager.data.nodechecker;

import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.ExclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

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
            ElementType.CODEROUTINE,
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
        if (!s.contains("...")) {
            return null;
        }

        return new CheckStatus("ImproperEllipsis", "\"" + s + "\" should not contain \"...\"", Optional.empty());
    }

    private static CheckStatus checkDoubleDot(final Element e) {

        final List<String> content = XmlHelper.getFirstLevelTextContent(e);
        for (final String s: content) {
            if (s.contains("..") && !s.contains("...")) {
                return new CheckStatus("DoubleDot", "\"" + s + "\" contains \"..\"", Optional.empty());
            }
        }
        return null;
    }

    private static CheckStatus checkApostrophe(final Element e) {

        final List<String> strings = XmlHelper.getFirstLevelTextContent(e);
        for (final String s: strings) {
            if (s.contains("'")) {
                return new CheckStatus("ImproperApostrophe", "' should be’", Optional.empty());
            }
        }

        return null;
    }
}
