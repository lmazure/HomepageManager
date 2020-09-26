package data.jsongenerator;

import java.text.Collator;
import java.util.Locale;

/**
 * @author Laurent
 *
 */
public class StringHelper {

    static private final Collator s_collator = Collator.getInstance(Locale.UK);

    static public int compare(final String str1, final String str2) {

        return s_collator.compare(StringHelper.cleanString(str1), StringHelper.cleanString(str2));
    }

    /**
     * @param str
     * @return
     */
    static private String cleanString(final String str) {
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < str.length(); i++) {
            int c = str.codePointAt(i);
            if (Character.isLetterOrDigit(c)) {
                startIndex = i;
                break;
            }
        }
        if (startIndex == -1) return "";

        for (int i = str.length()-1; i >= 0; i--) {
            int c = str.codePointAt(i);
            if (Character.isLetterOrDigit(c)) {
                endIndex = i;
                break;
            }
        }

        String result = "";
        boolean lastCharacterWasSpace = false;
        for (int i = startIndex; i <= endIndex; i++) {
            int c = str.codePointAt(i);
            if (Character.isLetterOrDigit(c)) {
                result += new String(Character.toChars(Character.toUpperCase(c)));
                lastCharacterWasSpace = false;
            } else {
                if (!lastCharacterWasSpace) {
                    result += "~";
                    lastCharacterWasSpace = true;
                }
            }
        }

        return result;
    }
}
