package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

/**
 * Correct the URL of a link
 */
public class UpdateLinkUrlCorrection  extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badUrl Incorrect URL
     * @param correctUrl  Correct URL
     */
    public UpdateLinkUrlCorrection(final String badUrl,
                                   final String correctUrl) {
        super("Update the link URL",
              "<A>" + Pattern.quote(badUrl) + "</A>",
              "<A>" + correctUrl + "</A>");
    }
}