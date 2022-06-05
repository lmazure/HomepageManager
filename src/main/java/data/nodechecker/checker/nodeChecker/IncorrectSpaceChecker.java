package data.nodechecker.checker.nodeChecker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class IncorrectSpaceChecker extends NodeChecker {

    //TODO ajouter l'ellipsis

    private static final Set<String> s_authorizedMissingSpaceList = new HashSet<>(Arrays.asList(
            "2.X",
            "3.X",
            "4.X",
            "a.k.a.",
            "Ampersand.js",
            "asm.js",
            "autosrb.pl",
            "ASP.NET",
            "Bubbl.us",
            "clinicaltrials.gov",
            "Clipboard.com",
            "Comma.ai",
            "distributed.net",
            "e.g.",
            "Famo.us",
            "gitlab.com",
            "Heu?reka",
            "i.e.",
            "Intl.RelativeTimeFormat",
            "Intro.js",
            "Java.Next",
            "analytics.katalon.com",
            "Kosmopoli:t",
            "MANIFEST.MF",
            "MSCTF.DLL",
            "MVC.NET",
            "NaturalNews.com",
            ".NET",
            "Node.js",
            "Normalize.css",
            "openjdk.org",
            "OpenOffice.org",
            "P.Anno",
            "quantum.country",
            "Quokka.js",
            "redhat.com",
            "Sails.js",
            "Three.js",
            "tween.js",
            "typescriptlang.org",
            "U.S.",
            "Venus.js",
            "view.json",
            "Wallaby.js",
            "xml:id",
            "X.org",
            "xsl:key"));

    private static final Set<String> s_authorizedMissingPrecedingSpaceList = new HashSet<>(Arrays.asList(
            ".NET"));

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT,
            ElementType.TITLE,
            });

    public IncorrectSpaceChecker() {
        super(s_selector,
              IncorrectSpaceChecker::checkMissingSpaceBeforePunctuation, "space is missing before punctuation",
              IncorrectSpaceChecker::checkMissingSpaceAfterPunctuation, "space is missing after punctuation",
              IncorrectSpaceChecker::checkInvalidSpaceBeforePunctuation, "invalid space is present before punctuation");
    }

    private static CheckStatus checkMissingSpaceBeforePunctuation(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        final Optional<Locale> locale = XmlHelper.getElementLanguage(e);
        if (locale.isEmpty()) {
            return new CheckStatus("\"" + e.getNodeName() + "\" has no language");
        }

        for (final String l: list) {
            if (Arrays.stream(l.split("[ /]")).anyMatch(s -> missesSpaceBeforePunctuation(s, locale.get()))) {
                return new CheckStatus("\"" + e.getTextContent() + "\" is missing a space before punctuation");
            }
        }

       return null;
    }

    private static CheckStatus checkMissingSpaceAfterPunctuation(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        final Optional<Locale> locale = XmlHelper.getElementLanguage(e);
        if (locale.isEmpty()) {
            return new CheckStatus("\"" + e.getNodeName() + "\" has no language");
        }

        for (final String l: list) {
            if (Arrays.stream(l.split("[ /]")).anyMatch(s -> missesSpaceAfterPunctuation(s, locale.get()))) {
                return new CheckStatus("\"" + e.getTextContent() + "\" is missing a space after punctuation");
            }
        }

       return null;
    }

    private static CheckStatus checkInvalidSpaceBeforePunctuation(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        final Optional<Locale> locale = XmlHelper.getElementLanguage(e);
        if (locale.isEmpty()) {
            return new CheckStatus("\"" + e.getNodeName() + "\" has no language");
        }

        for (final String l: list) {
            if (containsPunctuationPrecededByInvalidSpace(l, locale.get())) {
                return new CheckStatus("\"" + l + "\" has a space before a punctuation");
            }
        }

       return null;
    }

    private static boolean missesSpaceBeforePunctuation(final String str,
                                                        final Locale locale) {

        final char[] chars = str.toCharArray();

        if (isVersionString(chars)) {
            return false;
        }

        if (!containsPunctuationNotPrecededBySpace(chars, locale)) {
            return false;
        }

        for (final String a: s_authorizedMissingSpaceList) {
            final String s = str.replace(a, "");
            if (containsNoLetter(s)) {
                return false;
            }
        }

        return true;
    }

    private static boolean missesSpaceAfterPunctuation(final String str,
                                                       final Locale locale) {

        final char[] chars = str.toCharArray();

        if (isVersionString(chars)) {
            return false;
        }

        if (!containsPunctuationNotFollowedBySpace(chars, locale)) {
            return false;
        }

        for (final String a: s_authorizedMissingSpaceList) {
            final String s = str.replace(a, "");
            if (containsNoLetter(s)) {
                return false;
            }
        }

        return true;
    }

    private static boolean containsNoLetter(final String str) {
        final char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVersionString(final char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] != '.') && !Character.isDigit(chars[i])) {
                return false;
            }
            if (i > 0) {
                if ((chars[i - 1] == '.') && (chars[i] == '.')) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean containsPunctuationNotPrecededBySpace(final char[] chars,
                                                                 final Locale locale) {
        for (int i = 0; i < chars.length - 1; i++) {
            final char currentChar = chars[i];
            if (Character.isAlphabetic(currentChar)) {
                final char nextChar = chars[i+ 1];
                if (locale.equals(Locale.ENGLISH)) {
                    if (isEnglishPunctuationWithSpaceBefore(nextChar)) {
                        return true;
                    }
                } else if (locale.equals(Locale.FRENCH)) {
                    if (isFrenchPunctuationWithSpaceBefore(nextChar)) {
                        return true;
                    }
                } else if (locale.getLanguage().isEmpty()) {
                    if (isEnglishPunctuationWithSpaceBefore(nextChar) && isFrenchPunctuationWithSpaceBefore(nextChar)) {
                        return true;
                    }
                } else {
                    throw new UnsupportedOperationException("Illegal locale value (" + locale + ")");
                }
            }
        }
        return false;
    }

    private static boolean containsPunctuationNotFollowedBySpace(final char[] chars,
                                                                 final Locale locale) {
        for (int i = 0; i < chars.length - 1; i++) {
            final char nextChar = chars[i + 1];
            if (Character.isAlphabetic(nextChar)) {
                final char currentChar = chars[i];
                if (locale.equals(Locale.ENGLISH)) {
                    if (isEnglishPunctuationWithSpaceAfter(currentChar)) {
                        return true;
                    }
                } else if (locale.equals(Locale.FRENCH)) {
                    if (isFrenchPunctuationWithSpaceAfter(currentChar)) {
                        return true;
                    }
                } else if (locale.getLanguage().isEmpty()) {
                    if (isEnglishPunctuationWithSpaceAfter(currentChar) && isFrenchPunctuationWithSpaceAfter(currentChar)) {
                        return true;
                    }
                } else {
                    throw new UnsupportedOperationException("Illegal locale value (" + locale + ")");
                }
            }
        }
        return false;
    }

    /**
     * @param str
     * @param locale
     * @return return true if the string contains a space that should not be present before a punctuation
     */
    private static boolean containsPunctuationPrecededByInvalidSpace(final String str,
                                                                     final Locale locale) {
        for (int i = 1; i < str.length(); i++) {
            final char previousChar = str.charAt(i - 1);
            final char currentChar = str.charAt(i);
            if (Character.isSpaceChar(previousChar)) {
                if (startsWithAuthorizedMissingPrecedingSpaceList(str.substring(i))) {
                    return false;
                }
                if (locale.equals(Locale.ENGLISH)) {
                    if (isEnglishPunctuationWithNoSpaceBefore(currentChar)) {
                        return true;
                    }
                } else if (locale.equals(Locale.FRENCH)) {
                    if (isFrenchPunctuationWithNoSpaceBefore(currentChar)) {
                        return true;
                    }
                } else if (locale.getLanguage().isEmpty()) {
                    if (isEnglishPunctuationWithNoSpaceBefore(currentChar) && isFrenchPunctuationWithNoSpaceBefore(currentChar)) {
                        return true;
                    }
                } else {
                    throw new UnsupportedOperationException("Illegal locale value (" + locale + ")");
                }
            }
        }
        return false;
    }

    /**
     * @param str
     * @return return true if the string begins with a "word" beginning by space (e.g. ".Net")
     */
    private static boolean startsWithAuthorizedMissingPrecedingSpaceList(final String str) {
        final String[] spl = str.split("\\s", 2);
        final String firstWord = spl[0];
        return s_authorizedMissingPrecedingSpaceList.contains(firstWord);
    }

    /**
     * @param c
     * @return true is this is a punctuation that should have no space before in English
     */
    private static boolean isEnglishPunctuationWithNoSpaceBefore(final char c) {
        return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
    }

    /**
     * @param c
     * @return true is this is a punctuation that should have no space before in French
     */
    private static boolean isFrenchPunctuationWithNoSpaceBefore(final char c) {
        return (c == ',') || (c == '.');
    }

    /**
     * @param c
     * @return true is this is a punctuation that should be followed by a space in English
     */
    private static boolean isEnglishPunctuationWithSpaceAfter(final char c) {
        return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
    }

    /**
     * @param c
     * @return true is this is a punctuation that should be followed by a space in French
     */
    private static boolean isFrenchPunctuationWithSpaceAfter(final char c) {
        return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
    }

    /**
     * @param c
     * @return true is this is a punctuation that should be preceded by a space in English
     */
    private static boolean isEnglishPunctuationWithSpaceBefore(final char c) {
        return false;
    }

    /**
     * @param c
     * @return true is this is a punctuation that should be preceded by a space in French
     */
    private static boolean isFrenchPunctuationWithSpaceBefore(final char c) {
        return (c == ':') || (c == ';');
    }
}
