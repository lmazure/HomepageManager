package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * Add a subtitle to a link
 */
public class AddLinkSubtitleCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param correctSubtitle  Correct subtitle
     * @param url URL of the link
     */
    public AddLinkSubtitleCorrection(final String correctSubtitle,
                                     final String url) {
        super("Add a link subtitle",
              "</T><A>" + Pattern.quote(url) + "</A><L>",
              "</T><ST>" + XmlHelper.transform(correctSubtitle) + "</ST><A>" + url + "</A><L>");
    }
}
