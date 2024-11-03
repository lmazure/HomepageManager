package fr.mazure.homepagemanager.data.violationcorrection;

import java.time.LocalDate;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;

/**
 * Correct the date of a link
 */
public class UpdateLinkDateCorrection extends RegexpViolationCorrection {

    /**
     * Constructor
     *
     * @param badDate Incorrect link date
     * @param correctDate Correct link date
     * @param url URL of the link
     */
    public UpdateLinkDateCorrection(final LocalDate badDate,
                                    final LocalDate correctDate,
                                    final String url) {
        super("Update the link date",
              "<A>" + Pattern.quote(url) + "</A>(.*)" + XmlGenerator.generateDate(badDate)+ "</X>",
              "<A>" + url + "</A>$1" + XmlGenerator.generateDate(correctDate)+ "</X>");
    }
}
