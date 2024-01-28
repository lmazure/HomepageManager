package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
 *
 */
public class NonNormalizedAuthorChecker extends NodeChecker {
    // TODO add a unit test for this because I am pretty sure this does not work anymore

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
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

        if (s.equalsIgnoreCase("Vint Cerf")) {
            return new CheckStatus("ImproperAuthorEncoding", "\"Vint Cerf\" should be \"Vinton Cerf\"", Optional.empty());
        }

        if (s.equalsIgnoreCase("Cynthia Keen")) {
            return new CheckStatus("ImproperAuthorEncoding", "\"Cynthia Keen\" should be \"Cynthia E. Keen\"", Optional.empty());
        }

        if (s.equalsIgnoreCase("Deb Borfitz")) {
            return new CheckStatus("ImproperAuthorEncoding", "\"Deb Borfitz\" should be \"Deborah Borfitz\"", Optional.empty());
        }

        return null;
    }
}
