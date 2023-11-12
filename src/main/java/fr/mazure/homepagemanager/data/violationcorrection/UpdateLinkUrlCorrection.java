package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

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
     *
     * @param badUrl Incorrect URL
     * @param correctUrl  Correct URL
     */
    public UpdateLinkUrlCorrection(final String badUrl,
                                   final String correctUrl) {
        super("Update the link URL");
        final String bad = Pattern.quote(XmlHelper.transform(badUrl));
        final String good = XmlHelper.transform(correctUrl);
        _pattern1 = Pattern.compile("<A>" + bad + "</A>");
        _replacement1 = "<A>" + good + "</A>";
        _pattern2 = Pattern.compile("predecessor=\""  + bad + "\"");
        _replacement2 = "predecessor=\""  + good + "\"";
    }

    @Override
    public String apply(final String content) {
        final Matcher matcher1 = _pattern1.matcher(content);
        final String str =  matcher1.replaceAll(_replacement1);
        final Matcher matcher2 = _pattern2.matcher(str);
        return matcher2.replaceAll(_replacement2);
    }
}