package fr.mazure.homepagemanager.data.violationcorrection;

import java.util.List;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * Add a dot at the end of a comment
 *
 */
public class AddDotAtCommentEnd extends RegexpViolationCorrection {

    /**
     * Constructor
     * @param commentComponents text components of the comment
     */
    public AddDotAtCommentEnd(final List<String> commentComponents) {
        super("Add a dot at comment end",
              fromPattern(commentComponents),
              toPattern(commentComponents));
    }
    
    private static String fromPattern(final List<String> commentComponents) {
        final StringBuilder builder = new StringBuilder("<COMMENT>((<.*>)?)");
        boolean first = true;
        for (final String str: commentComponents) {
            if (!first) {
                builder.append("(<.*>)");
            }
            builder.append(Pattern.quote(XmlHelper.transform(str)));
            first = false;
        }
        builder.append("((<.*>)?)</COMMENT>");
        return builder.toString();
    }
    
    private static String toPattern(final List<String> commentComponents) {
        final StringBuilder builder = new StringBuilder("<COMMENT>$1");
        int i = 2;
        for (final String str: commentComponents) {
            if (i > 2) {
                builder.append("$" + i);
            }
            builder.append(XmlHelper.transform(str));
            i++;
        }
        builder.append("$" + i + ".</COMMENT>");
        return builder.toString();
    }
}
