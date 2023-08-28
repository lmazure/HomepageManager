package fr.mazure.homepagemanager.data.violationcorrection;

import java.time.Duration;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;

/**
 * Correct the duration of a link
 */
public class UpdateLinkDurationCorrection extends RegexpViolationCorrection {

    /**
     * @param badDuration Incorrect duration
     * @param correctDuration Correct Duration
     * @param url URL of the link
     */
    public UpdateLinkDurationCorrection(final Duration badDuration,
                                        final Duration correctDuration,
                                        final String url) {
        super("Update the link duration",
              "<A>" + Pattern.quote(url) + "</A>(<L>..</L><F>[a-zA-Z0-9 ]+</F>)" + XmlGenerator.generateDuration(badDuration),
              "<A>" + url + "</A>$1" + XmlGenerator.generateDuration(correctDuration));
    }
}
