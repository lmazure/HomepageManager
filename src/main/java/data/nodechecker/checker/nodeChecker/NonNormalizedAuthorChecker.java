package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

public class NonNormalizedAuthorChecker extends NodeChecker {
    // TODO add a unit test for this because I am pretty sure this does not work anymore

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.AUTHOR
            });

    public NonNormalizedAuthorChecker() {
        super(s_selector,
              NonNormalizedAuthorChecker::checkAuthorNamet, "uses a non-normalized author name");
    }

    private static CheckStatus checkAuthorNamet(final Element e) {

        final String s = e.getTextContent();

        if (s.equalsIgnoreCase("Vint Cerf"))
            return new CheckStatus("\"Vint Cerf\" should be \"Vinton Cerf\"");

        if (s.equalsIgnoreCase("Cynthia Keen"))
            return new CheckStatus("\"Cynthia Keen\" should be \"Cynthia E. Keen\"");

        if (s.equalsIgnoreCase("Deb Borfitz"))
            return new CheckStatus("\"Deb Borfitz\" should be \"Deborah Borfitz\"");

        return null;
    }
}
