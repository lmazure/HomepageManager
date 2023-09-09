package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Correct the status of a link
 */
public class UpdateLinkStatusCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badStatus Incorrect status
     * @param correctStatus  Correct status
     * @param url URL of the link
     */
    public UpdateLinkStatusCorrection(final LinkStatus badStatus,
                                      final LinkStatus correctStatus,
                                      final String url) {
        super("Update link status",
              "(<X[^>]*)" + XmlGenerator.generateStatus(badStatus) + "([^>]*><T>[^>]+</T>(<ST>[^>]+</ST>)?)<A>" + Pattern.quote(url) + "</A>",
              "$1" + XmlGenerator.generateStatus(correctStatus) + "$2<A>" + url + "</A>");
    }
}
