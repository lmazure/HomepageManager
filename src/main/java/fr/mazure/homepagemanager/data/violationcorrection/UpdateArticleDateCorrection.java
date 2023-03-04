package fr.mazure.homepagemanager.data.violationcorrection;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Correct the date of an article
 */
public class UpdateArticleDateCorrection extends RegexpViolationCorrection {
    /**
     * Constructor
     * @param badDate Incorrect article date
     * @param correctDate  Correct article date
     * @param url URL of one of the links of the article
     */
    public UpdateArticleDateCorrection(final LocalDate badDate,
                                       final LocalDate correctDate,
                                       final String url) {
        super("Update the article date",
              "<A>" + Pattern.quote(url) + "</A>(.*)<DATE><YEAR>" + badDate.getYear() + "</YEAR><MONTH>" + badDate.getMonthValue() + "</MONTH><DAY>" + badDate.getDayOfMonth() + "</DAY></DATE><COMMENT>",
              "<A>" + url + "</A>$1<DATE><YEAR>" + correctDate.getYear() + "</YEAR><MONTH>" + correctDate.getMonthValue() + "</MONTH><DAY>" + correctDate.getDayOfMonth() + "</DAY></DATE><COMMENT>");
    }
}
