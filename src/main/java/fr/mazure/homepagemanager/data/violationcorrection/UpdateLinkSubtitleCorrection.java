package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * Correct the subtitle of a link
 */
public class UpdateLinkSubtitleCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     *
     * @param badSubtitle Incorrect subtitle
     * @param correctSubtitle Correct subtitle
     * @param url URL of the link
     */
    public UpdateLinkSubtitleCorrection(final String badSubtitle,
                                        final String correctSubtitle,
                                        final String url) {
        super("Update a link subtitle",
              "<ST>" + Pattern.quote(XmlHelper.transform(badSubtitle)) + "</ST><A>" + Pattern.quote(url) + "</A>",
              "<ST>" + escapeReplacementString(XmlHelper.transform(correctSubtitle)) + "</ST><A>" + url + "</A>");
    }
}
