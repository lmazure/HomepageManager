package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Pattern;

/**
 * Correct the title of an article
 */
public class UpdateArticleTitleCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badTitle Incorrect title
     * @param correctTitle  Correct title
     * @param url URL of the link
     */
    public UpdateArticleTitleCorrection(final String badTitle,
                                        final String correctTitle,
                                        final String url) {
        super("Update the article title",
              "><T>" + Pattern.quote(badTitle) + "</T>(<ST>[^<]+</ST>)?<A>" + Pattern.quote(url) + "</A><L>",
              "><T>" + correctTitle + "</T>$1<A>" + url + "</A><L>");
    }
}