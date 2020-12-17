package utils;

import java.io.StringWriter;
import java.util.HashMap;

public class HtmlHelper {

    public static final String unescape(final String input) {
        // from https://stackoverflow.com/questions/994331/how-to-unescape-html-character-entities-in-java
        StringWriter writer = null;
        int len = input.length();
        int i = 1;
        int st = 0;
        while (true) {
            // look for '&'
            while (i < len && input.charAt(i-1) != '&')
                i++;
            if (i >= len)
                break;

            // found '&', look for ';'
            int j = i;
            while (j < len && j < i + MAX_ESCAPE + 1 && input.charAt(j) != ';')
                j++;
            if (j == len || j < i + MIN_ESCAPE || j == i + MAX_ESCAPE + 1) {
                i++;
                continue;
            }

            // found escape 
            if (input.charAt(i) == '#') {
                // numeric escape
                int k = i + 1;
                int radix = 10;

                final char firstChar = input.charAt(k);
                if (firstChar == 'x' || firstChar == 'X') {
                    k++;
                    radix = 16;
                }

                try {
                    int entityValue = Integer.parseInt(input.substring(k, j), radix);

                    if (writer == null) 
                        writer = new StringWriter(input.length());
                    writer.append(input.substring(st, i - 1));

                    if (entityValue > 0xFFFF) {
                        final char[] chrs = Character.toChars(entityValue);
                        writer.write(chrs[0]);
                        writer.write(chrs[1]);
                    } else {
                        writer.write(entityValue);
                    }

                } catch (@SuppressWarnings("unused") final NumberFormatException ex) { 
                    i++;
                    continue;
                }
            }
            else {
                // named escape
                Character value = lookupMap.get(input.substring(i, j));
                if (value == null) {
                    i++;
                    continue;
                }

                if (writer == null) 
                    writer = new StringWriter(input.length());
                writer.append(input.substring(st, i - 1));

                writer.append(value);
            }

            // skip escape
            st = j + 1;
            i = st;
        }

        if (writer != null) {
            writer.append(input.substring(st, len));
            return writer.toString();
        }
        return input;
    }

    private static final int MIN_ESCAPE = 2;
    private static final int MAX_ESCAPE = 6;

    private static final HashMap<String, Character> lookupMap;
    static {
        lookupMap = new HashMap<String, Character>();
        lookupMap.put("amp", '&');
        lookupMap.put("gt", '>');
        lookupMap.put("lt", '<');
    }
}
