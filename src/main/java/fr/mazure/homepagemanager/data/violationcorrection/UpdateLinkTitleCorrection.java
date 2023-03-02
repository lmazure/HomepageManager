package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * Correct the title of a link
 */
public class UpdateLinkTitleCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badTitle Incorrect title
     * @param correctTitle  Correct title
     * @param url URL of the link
     */
    public UpdateLinkTitleCorrection(final String badTitle,
                                     final String correctTitle,
                                     final String url) {
        super("Update the article title",
              "><T>" + Pattern.quote(XmlHelper.transform(badTitle)) + "</T>(<ST>[^<]+</ST>)?<A>" + Pattern.quote(url) + "</A><L>",
              "><T>" + XmlHelper.transform(correctTitle) + "</T>$1<A>" + url + "</A><L>");
    }
}