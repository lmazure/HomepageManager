package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Violation correction based on search/replace
 */
public class RegexpViolationCorrection extends ViolationCorrection {

    private final Pattern _pattern;
    private final String _replacement;

    /**
     * Constructor
     *
     * @param description of the correction
     * @param pattern Searched pattern
     * @param replacement Replacement
     */
    public RegexpViolationCorrection(final String description,
                                     final String pattern,
                                     final String replacement) {
        super(description);
        _pattern = Pattern.compile(pattern);
        _replacement = replacement;
    }

    @Override
    public String apply(final String content) {
        final Matcher matcher = _pattern.matcher(content);
        return matcher.replaceAll(_replacement);
    }

    /**
     * Escape the string used as replacement in Matcher.replaceAll().
     *
     * @param replacement
     * @return
     */
    protected static String escapeReplacementString(final String replacement) {
        return replacement.replace("$", "\\$");
    }
}
