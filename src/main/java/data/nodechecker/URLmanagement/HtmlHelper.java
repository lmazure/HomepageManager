package data.nodechecker.URLmanagement;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlHelper {

    static final private String s_encodingUsAscii = "US-ASCII";
    static final private String s_encodingUtf8 = "UTF-8";
    static final private String s_encodingIso88591 = "ISO-8859-1";
    static final private String s_encodingWindows1252 = "windows-1252";
    static final private String s_encodingGbk = "GBK";

    static final Pattern s_specifiedEncodingPattern1 = Pattern.compile("<\\s*meta\\s*http-equiv\\s*=\\s*\"[Cc]ontent-[Tt]ype\"\\s*content\\s*=\\s*\"text/html\\s*;\\s*charset\\s*=\\s*([^\"]+)\\s*\"\\s*/>");
    static final Pattern s_specifiedEncodingPattern2 = Pattern.compile("<\\s*meta\\s*charset\\s*=\"\\s*([^\"]+)\"\\s*/>");

    /**
     * @param input
     * @return partially decode the HTML
     */
    public static String decode(final byte[] input) {

        String s = null;
        try {
            s = new String(input, s_encodingIso88591);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        final String defaultEncoding = getDefaultEncoding(s);
        final String specifiedEncoding = getSpecifiedEncoding(s);
        final String encoding = (specifiedEncoding == null) ? defaultEncoding : specifiedEncoding;

        if (encoding != s_encodingIso88591) {
            try {
                s = new String(input, encoding);
            } catch (final UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }

        return s
            .replaceAll("\\\"", "\"") // TODO ce cas devrait être géré de façon spécifique et pris en compte que si on est dans une chaîne de caracatères avec des double-quotes
            .replaceAll("\\\\\u0026", "&") // TODO idem, ne devrait être fait que dans une chaîne de caractères

            .replaceAll("&#039;", "'")

            .replaceAll("&#146;", "’")
            .replaceAll("&#39;", "'")
            .replaceAll("&#8216;", "‘")
            .replaceAll("&#8217;", "’")
            .replaceAll("&#8220;", "“")
            .replaceAll("&#8221;", "”")
            .replaceAll("&#8230;", "…")

            .replaceAll("&#x27;", "'")

            .replaceAll("&aacute;", "á")
            .replaceAll("&amp;", "&")
            .replaceAll("&hellip;", "…")
            .replaceAll("&mdash;", "—")
            .replaceAll("&nbsp;", " ")
            .replaceAll("&ndash;", "–")
            .replaceAll("&oacute;", "ó")
            .replaceAll("&ouml;", "ö")
            .replaceAll("&quot;", "\"")
            .replaceAll("&rsquot;", "’");
    }

    /**
     * @param input
     * @return partially decode the HTML
     */
    static public String digest(final String input) {

        final String s = input
            .replaceAll("\\s+", " ")
            .replaceAll(" *\\n *", " ")
            .replaceAll(" *\\< *[bB][rR] */\\> *", " ");

        return s;
    }

    static private String getDefaultEncoding(final String contentDecodedAsIso8859)
    {
        if (contentDecodedAsIso8859.toUpperCase().startsWith("<!DOCTYPE HTML>")) {
            // HTML5
            return s_encodingUtf8;
        }
        // older HTML versions
        return s_encodingIso88591;
    }

    static private String getSpecifiedEncoding(final String contentDecodedAsIso8859)
    {
        Matcher matcher = s_specifiedEncodingPattern1.matcher(contentDecodedAsIso8859);
        if (!matcher.find()) {
            matcher = s_specifiedEncodingPattern2.matcher(contentDecodedAsIso8859);
            if (!matcher.find()) return null;
        }

        final String specifiedEncoding = matcher.group(1);
        switch (specifiedEncoding)
        {
            case "us-ascii":     return s_encodingUsAscii;
            case "utf-8":        return s_encodingUtf8;
            case "UTF-8":        return s_encodingUtf8;
            case "iso-8859-1":   return s_encodingIso88591;
            case "ISO-8859-1":   return s_encodingIso88591;
            case "windows-1252": return s_encodingWindows1252;
            case "Windows-1252": return s_encodingWindows1252;
            case "gbk":          return s_encodingGbk;
            default: throw new IllegalArgumentException("\"" + specifiedEncoding + "\" is an unknown encoding");
        }
    }
}
