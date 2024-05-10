package fr.mazure.homepagemanager.data.jsongenerator;

import java.text.Collator;
import java.util.Locale;

/**
 * Helpers to manipulate strings
 */
public class StringHelper {

    private static final Collator s_collator = Collator.getInstance(Locale.ENGLISH);

    /**
     * Compare two strings in Locale.ENGLISH
     *
     * @param str1 first string
     * @param str2 second string
     * @return -1 if str1 < str2, 0 if str1 == str2, 1 if str1 > str2
     */
    public static int compare(final String str1,
                              final String str2) {

        return s_collator.compare(StringHelper.cleanString(str1), StringHelper.cleanString(str2));
    }

    /**
     * @param str
     * @return
     */
    private static String cleanString(final String str) {
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < str.length(); i++) {
            final int c = str.codePointAt(i);
            if (Character.isLetterOrDigit(c)) {
                startIndex = i;
                break;
            }
        }
        if (startIndex == -1) {
            return "";
        }

        for (int i = str.length()-1; i >= 0; i--) {
            final int c = str.codePointAt(i);
            if (Character.isLetterOrDigit(c)) {
                endIndex = i;
                break;
            }
        }

        String result = "";
        boolean lastCharacterWasSpace = false;
        for (int i = startIndex; i <= endIndex; i++) {
            final int c = str.codePointAt(i);
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
