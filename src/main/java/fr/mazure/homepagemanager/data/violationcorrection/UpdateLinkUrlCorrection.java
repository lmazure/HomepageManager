package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Correct the URL of a link
 */
public class UpdateLinkUrlCorrection extends ViolationCorrection {

    private final Pattern _pattern1;
    private final String _replacement1;
    private final Pattern _pattern2;
    private final String _replacement2;

    /**
     * Constructor
     * @param badUrl Incorrect URL
     * @param correctUrl  Correct URL
     */
    public UpdateLinkUrlCorrection(final String badUrl,
                                   final String correctUrl) {
        super("Update the link URL");
        _pattern1 = Pattern.compile("<A>" + Pattern.quote(badUrl) + "</A>");
        _replacement1 = "<A>" + correctUrl + "</A>";
        _pattern2 = Pattern.compile("predecessor=\""  + Pattern.quote(badUrl) + "\"");
        _replacement2 = "predecessor=\""  + correctUrl + "\"";
    }

    @Override
    public String apply(final String content) {
        final Matcher matcher1 = _pattern1.matcher(content);
        final String str =  matcher1.replaceAll(_replacement1);
        final Matcher matcher2 = _pattern2.matcher(str);
        return matcher2.replaceAll(_replacement2);
    }
}