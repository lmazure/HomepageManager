package utils;

import java.io.StringWriter;
import java.util.HashMap;

@SuppressWarnings("boxing")
public class HtmlHelper {

    public static final String cleanContent(final String input) {
        return unescape(removeHtmlTags(unduplicateSpace(input)));
    }

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
            } else {
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
        lookupMap = new HashMap<>();
        lookupMap.put("Aacute", '\u00C1');
        lookupMap.put("aacute", '\u00E1');
        lookupMap.put("Acirc", '\u00C2');
        lookupMap.put("acirc", '\u00E2');
        lookupMap.put("acute", '\u00B4');
        lookupMap.put("AElig", '\u00C6');
        lookupMap.put("aelig", '\u00E6');
        lookupMap.put("Agrave", '\u00C0');
        lookupMap.put("agrave", '\u00E0');
        lookupMap.put("alefsym", '\u2135');
        lookupMap.put("Alpha", '\u0391');
        lookupMap.put("alpha", '\u03B1');
        lookupMap.put("amp", '\u0026');
        lookupMap.put("and", '\u2227');
        lookupMap.put("ang", '\u2220');
        lookupMap.put("Aring", '\u00C5');
        lookupMap.put("aring", '\u00E5');
        lookupMap.put("asymp", '\u2248');
        lookupMap.put("Atilde", '\u00C3');
        lookupMap.put("atilde", '\u00E3');
        lookupMap.put("Auml", '\u00C4');
        lookupMap.put("auml", '\u00E4');
        lookupMap.put("bdquo", '\u201E');
        lookupMap.put("Beta", '\u0392');
        lookupMap.put("beta", '\u03B2');
        lookupMap.put("brvbar", '\u00A6');
        lookupMap.put("bull", '\u2022');
        lookupMap.put("cap", '\u2229');
        lookupMap.put("Ccedil", '\u00C7');
        lookupMap.put("ccedil", '\u00E7');
        lookupMap.put("cedil", '\u00B8');
        lookupMap.put("cent", '\u00A2');
        lookupMap.put("Chi", '\u03A7');
        lookupMap.put("chi", '\u03C7');
        lookupMap.put("circ", '\u02C6');
        lookupMap.put("clubs", '\u2663');
        lookupMap.put("cong", '\u2245');
        lookupMap.put("copy", '\u00A9');
        lookupMap.put("crarr", '\u21B5');
        lookupMap.put("cup", '\u222A');
        lookupMap.put("curren", '\u00A4');
        lookupMap.put("dagger", '\u2020');
        lookupMap.put("Dagger", '\u2021');
        lookupMap.put("darr", '\u2193');
        lookupMap.put("dArr", '\u21D3');
        lookupMap.put("deg", '\u00B0');
        lookupMap.put("Delta", '\u0394');
        lookupMap.put("delta", '\u03B4');
        lookupMap.put("diams", '\u2666');
        lookupMap.put("divide", '\u00F7');
        lookupMap.put("Eacute", '\u00C9');
        lookupMap.put("eacute", '\u00E9');
        lookupMap.put("Ecirc", '\u00CA');
        lookupMap.put("ecirc", '\u00EA');
        lookupMap.put("Egrave", '\u00C8');
        lookupMap.put("egrave", '\u00E8');
        lookupMap.put("empty", '\u2205');
        lookupMap.put("emsp", '\u2003');
        lookupMap.put("ensp", '\u2002');
        lookupMap.put("Epsilon", '\u0395');
        lookupMap.put("epsilon", '\u03B5');
        lookupMap.put("equiv", '\u2261');
        lookupMap.put("Eta", '\u0397');
        lookupMap.put("eta", '\u03B7');
        lookupMap.put("ETH", '\u00D0');
        lookupMap.put("eth", '\u00F0');
        lookupMap.put("Euml", '\u00CB');
        lookupMap.put("euml", '\u00EB');
        lookupMap.put("euro", '\u20AC');
        lookupMap.put("exist", '\u2203');
        lookupMap.put("fnof", '\u0192');
        lookupMap.put("forall", '\u2200');
        lookupMap.put("frac12", '\u00BD');
        lookupMap.put("frac14", '\u00BC');
        lookupMap.put("frac34", '\u00BE');
        lookupMap.put("frasl", '\u2044');
        lookupMap.put("Gamma", '\u0393');
        lookupMap.put("gamma", '\u03B3');
        lookupMap.put("ge", '\u2265');
        lookupMap.put("gt", '\u003E');
        lookupMap.put("harr", '\u2194');
        lookupMap.put("hArr", '\u21D4');
        lookupMap.put("hearts", '\u2665');
        lookupMap.put("hellip", '\u2026');
        lookupMap.put("Iacute", '\u00CD');
        lookupMap.put("iacute", '\u00ED');
        lookupMap.put("Icirc", '\u00CE');
        lookupMap.put("icirc", '\u00EE');
        lookupMap.put("iexcl", '\u00A1');
        lookupMap.put("Igrave", '\u00CC');
        lookupMap.put("igrave", '\u00EC');
        lookupMap.put("image", '\u2111');
        lookupMap.put("infin", '\u221E');
        lookupMap.put("int", '\u222B');
        lookupMap.put("Iota", '\u0399');
        lookupMap.put("iota", '\u03B9');
        lookupMap.put("iquest", '\u00BF');
        lookupMap.put("isin", '\u2208');
        lookupMap.put("Iuml", '\u00CF');
        lookupMap.put("iuml", '\u00EF');
        lookupMap.put("Kappa", '\u039A');
        lookupMap.put("kappa", '\u03BA');
        lookupMap.put("Lambda", '\u039B');
        lookupMap.put("lambda", '\u03BB');
        lookupMap.put("lang", '\u2329');
        lookupMap.put("laquo", '\u00AB');
        lookupMap.put("larr", '\u2190');
        lookupMap.put("lArr", '\u21D0');
        lookupMap.put("lceil", '\u2308');
        lookupMap.put("ldquo", '\u201C');
        lookupMap.put("le", '\u2264');
        lookupMap.put("lfloor", '\u230A');
        lookupMap.put("lowast", '\u2217');
        lookupMap.put("loz", '\u25CA');
        lookupMap.put("lrm", '\u200E');
        lookupMap.put("lsaquo", '\u2039');
        lookupMap.put("lsquo", '\u2018');
        lookupMap.put("lt", '\u003C');
        lookupMap.put("macr", '\u00AF');
        lookupMap.put("mdash", '\u2014');
        lookupMap.put("micro", '\u00B5');
        lookupMap.put("middot", '\u00B7');
        lookupMap.put("minus", '\u2212');
        lookupMap.put("Mu", '\u039C');
        lookupMap.put("mu", '\u03BC');
        lookupMap.put("nabla", '\u2207');
        lookupMap.put("nbsp", '\u00A0');
        lookupMap.put("ndash", '\u2013');
        lookupMap.put("ne", '\u2260');
        lookupMap.put("ni", '\u220B');
        lookupMap.put("not", '\u00AC');
        lookupMap.put("notin", '\u2209');
        lookupMap.put("nsub", '\u2284');
        lookupMap.put("Ntilde", '\u00D1');
        lookupMap.put("ntilde", '\u00F1');
        lookupMap.put("Nu", '\u039D');
        lookupMap.put("nu", '\u03BD');
        lookupMap.put("Oacute", '\u00D3');
        lookupMap.put("oacute", '\u00F3');
        lookupMap.put("Ocirc", '\u00D4');
        lookupMap.put("ocirc", '\u00F4');
        lookupMap.put("OElig", '\u0152');
        lookupMap.put("oelig", '\u0153');
        lookupMap.put("Ograve", '\u00D2');
        lookupMap.put("ograve", '\u00F2');
        lookupMap.put("oline", '\u203E');
        lookupMap.put("Omega", '\u03A9');
        lookupMap.put("omega", '\u03C9');
        lookupMap.put("Omicron", '\u039F');
        lookupMap.put("omicron", '\u03BF');
        lookupMap.put("oplus", '\u2295');
        lookupMap.put("or", '\u2228');
        lookupMap.put("ordf", '\u00AA');
        lookupMap.put("ordm", '\u00BA');
        lookupMap.put("Oslash", '\u00D8');
        lookupMap.put("oslash", '\u00F8');
        lookupMap.put("Otilde", '\u00D5');
        lookupMap.put("otilde", '\u00F5');
        lookupMap.put("otimes", '\u2297');
        lookupMap.put("Ouml", '\u00D6');
        lookupMap.put("ouml", '\u00F6');
        lookupMap.put("para", '\u00B6');
        lookupMap.put("part", '\u2202');
        lookupMap.put("permil", '\u2030');
        lookupMap.put("perp", '\u22A5');
        lookupMap.put("Phi", '\u03A6');
        lookupMap.put("phi", '\u03C6');
        lookupMap.put("Pi", '\u03A0');
        lookupMap.put("pi", '\u03C0');
        lookupMap.put("piv", '\u03D6');
        lookupMap.put("plusmn", '\u00B1');
        lookupMap.put("pound", '\u00A3');
        lookupMap.put("prime", '\u2032');
        lookupMap.put("Prime", '\u2033');
        lookupMap.put("prod", '\u220F');
        lookupMap.put("prop", '\u221D');
        lookupMap.put("Psi", '\u03A8');
        lookupMap.put("psi", '\u03C8');
        lookupMap.put("quot", '\u0022');
        lookupMap.put("radic", '\u221A');
        lookupMap.put("rang", '\u232A');
        lookupMap.put("raquo", '\u00BB');
        lookupMap.put("rarr", '\u2192');
        lookupMap.put("rArr", '\u21D2');
        lookupMap.put("rceil", '\u2309');
        lookupMap.put("rdquo", '\u201D');
        lookupMap.put("real", '\u211C');
        lookupMap.put("reg", '\u00AE');
        lookupMap.put("rfloor", '\u230B');
        lookupMap.put("Rho", '\u03A1');
        lookupMap.put("rho", '\u03C1');
        lookupMap.put("rlm", '\u200F');
        lookupMap.put("rsaquo", '\u203A');
        lookupMap.put("rsquo", '\u2019');
        lookupMap.put("sbquo", '\u201A');
        lookupMap.put("Scaron", '\u0160');
        lookupMap.put("scaron", '\u0161');
        lookupMap.put("sdot", '\u22C5');
        lookupMap.put("sect", '\u00A7');
        lookupMap.put("shy", '\u00AD');
        lookupMap.put("Sigma", '\u03A3');
        lookupMap.put("sigma", '\u03C3');
        lookupMap.put("sigmaf", '\u03C2');
        lookupMap.put("sim", '\u223C');
        lookupMap.put("spades", '\u2660');
        lookupMap.put("sub", '\u2282');
        lookupMap.put("sube", '\u2286');
        lookupMap.put("sum", '\u2211');
        lookupMap.put("sup", '\u2283');
        lookupMap.put("sup1", '\u00B9');
        lookupMap.put("sup2", '\u00B2');
        lookupMap.put("sup3", '\u00B3');
        lookupMap.put("supe", '\u2287');
        lookupMap.put("szlig", '\u00DF');
        lookupMap.put("Tau", '\u03A4');
        lookupMap.put("tau", '\u03C4');
        lookupMap.put("there4", '\u2234');
        lookupMap.put("Theta", '\u0398');
        lookupMap.put("theta", '\u03B8');
        lookupMap.put("thetasym", '\u03D1');
        lookupMap.put("thinsp", '\u2009');
        lookupMap.put("THORN", '\u00DE');
        lookupMap.put("thorn", '\u00FE');
        lookupMap.put("tilde", '\u02DC');
        lookupMap.put("times", '\u00D7');
        lookupMap.put("trade", '\u2122');
        lookupMap.put("Uacute", '\u00DA');
        lookupMap.put("uacute", '\u00FA');
        lookupMap.put("uarr", '\u2191');
        lookupMap.put("uArr", '\u21D1');
        lookupMap.put("Ucirc", '\u00DB');
        lookupMap.put("ucirc", '\u00FB');
        lookupMap.put("Ugrave", '\u00D9');
        lookupMap.put("ugrave", '\u00F9');
        lookupMap.put("uml", '\u00A8');
        lookupMap.put("upsih", '\u03D2');
        lookupMap.put("Upsilon", '\u03A5');
        lookupMap.put("upsilon", '\u03C5');
        lookupMap.put("Uuml", '\u00DC');
        lookupMap.put("uuml", '\u00FC');
        lookupMap.put("weierp", '\u2118');
        lookupMap.put("Xi", '\u039E');
        lookupMap.put("xi", '\u03BE');
        lookupMap.put("Yacute", '\u00DD');
        lookupMap.put("yacute", '\u00FD');
        lookupMap.put("yen", '\u00A5');
        lookupMap.put("yuml", '\u00FF');
        lookupMap.put("Yuml", '\u0178');
        lookupMap.put("Zeta", '\u0396');
        lookupMap.put("zeta", '\u03B6');
        lookupMap.put("zwj", '\u200D');
        lookupMap.put("zwnj", '\u200C');
    }

    public static final String unduplicateSpace(final String input) {

        final StringBuilder builder = new StringBuilder(input.length());
        boolean previousWasSpace = false;

        for (int i = 0; i < input.length();) {
            final int character = input.codePointAt(i);
            if (character == ' ' /*Character.isWhitespace(character) || (character == '\u00A0')*/) {
                if (!previousWasSpace) {
                    builder.appendCodePoint(' ');
                }
                previousWasSpace = true;
            } else {
                builder.appendCodePoint(character);
                previousWasSpace = false;
            }
            i += Character.charCount(character);
        }

        return builder.toString();
    }

    public static final String removeHtmlTags(final String input) {

        final StringBuilder builder = new StringBuilder(input.length());
        boolean inTag = false;

        for (int i = 0; i < input.length();) {
            final int character = input.codePointAt(i);
            if (inTag) {
                inTag = character != '>';
            } else {
                if (character == '<') {
                    inTag = true;
                } else {
                    builder.appendCodePoint(character);
                }
            }
            i += Character.charCount(character);
        }

        return builder.toString();
    }
}
