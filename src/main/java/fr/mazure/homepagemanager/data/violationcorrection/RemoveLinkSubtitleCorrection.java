package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * Remove a subtitle to a link
 */
public class RemoveLinkSubtitleCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badSubtitle  Bad subtitle
     * @param url URL of the link
     */
    public RemoveLinkSubtitleCorrection(final String badSubtitle,
                                        final String url) {
        super("Remove link subtitle",
              "><ST>" + Pattern.quote(XmlHelper.transform(badSubtitle)) + "</ST><A>" + Pattern.quote(url) + "</A><L>",
              "><A>" + url + "</A><L>");
    }
}
