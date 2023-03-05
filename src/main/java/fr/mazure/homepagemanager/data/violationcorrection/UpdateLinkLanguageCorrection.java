package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Correct the language of a link
 */
public class UpdateLinkLanguageCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param badLanguage Incorrect language
     * @param correctLanguage  Correct language
     * @param url URL of the link
     */
    public UpdateLinkLanguageCorrection(final Locale badLanguage,
                                        final Locale correctLanguage,
                                        final String url) {
        super("Update the link language",
              "<A>" + Pattern.quote(url) + "</A><L>" + badLanguage.getLanguage() + "</L>",
              "<A>" + url + "</A><L>" + correctLanguage.getLanguage() + "</L>");
    }
}