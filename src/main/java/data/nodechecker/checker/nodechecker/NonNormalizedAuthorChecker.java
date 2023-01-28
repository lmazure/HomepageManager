package data.nodechecker.checker.nodechecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

/**
*
*/
public class NonNormalizedAuthorChecker extends NodeChecker {
    // TODO add a unit test for this because I am pretty sure this does not work anymore

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.AUTHOR
            });

    /**
    * constructor
    */
    public NonNormalizedAuthorChecker() {
        super(s_selector,
              NonNormalizedAuthorChecker::checkAuthorNamet, "uses a non-normalized author name");
    }

    private static CheckStatus checkAuthorNamet(final Element e) {

        final String s = e.getTextContent();

        if (s.equalsIgnoreCase("Vint Cerf"))
            return new CheckStatus("ImproperAuthorEncoding", "\"Vint Cerf\" should be \"Vinton Cerf\"");

        if (s.equalsIgnoreCase("Cynthia Keen"))
            return new CheckStatus("ImproperAuthorEncoding", "\"Cynthia Keen\" should be \"Cynthia E. Keen\"");

        if (s.equalsIgnoreCase("Deb Borfitz"))
            return new CheckStatus("ImproperAuthorEncoding", "\"Deb Borfitz\" should be \"Deborah Borfitz\"");

        return null;
    }
}
