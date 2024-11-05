package fr.mazure.homepagemanager.data.violationcorrection;

import java.time.temporal.TemporalAccessor;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;

/**
 * Correct the date of an article
 */
public class UpdateArticleDateCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     *
     * @param badDate Incorrect article date
     * @param correctDate Correct article date
     * @param url URL of one of the links of the article
     */
    public UpdateArticleDateCorrection(final TemporalAccessor badDate,
                                       final TemporalAccessor correctDate,
                                       final String url) {
        super("Update the article date",
              "<A>" + Pattern.quote(url) + "</A>(.*)" + XmlGenerator.generateDate(badDate)+ "<COMMENT>",
              "<A>" + url + "</A>$1" + XmlGenerator.generateDate(correctDate)+ "<COMMENT>");
    }
}
