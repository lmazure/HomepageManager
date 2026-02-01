package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 *
 */
public class IncorrectSpaceChecker extends NodeChecker {

    //TODO ajouter l'ellipsis

    private static final Set<String> s_authorizedMissingSpaceList = Set.of(
            "2b2t.org",
            "2.X",
            "3.X",
            "4.X",
            "a.k.a.",
            "Ampersand.js",
            "AGENTS.md",
            "Answer.AI",
            "analytics.katalon.com",
            "asm.js",
            "autosrb.pl",
            "ASP.NET",
            "bolt.diy",
            "Bubbl.us",
            "cdn.polyfill.io",
            "Claude.ai",
            "clinicaltrials.gov",
            "Clipboard.com",
            "Comma.ai",
            "chess.com",
            "distributed.net",
            "e.g.",
            "Famo.us",
            "Fireworks.ai",
            "Fly.io",
            "Frama.space",
            "github.com",
            "GitLab.com",
            "GPU.zip",
            "Heu?reka",
            "i.e.",
            "Intl.RelativeTimeFormat",
            "Intro.js",
            "Java.Next",
            "Kling.ai",
            "Kosmopoli:t",
            "llama.cpp",
            "learntail.com",
            "MANIFEST.MF",
            "M.A.R.I.",
            "MSCTF.DLL",
            "MVC.NET",
            "Mojobak.com",
            "NaturalNews.com",
            ".Net",
            ".NET",
            "Node.js",
            "Normalize.css",
            "npmjs.com",
            "openjdk.org",
            "OpenOffice.org",
            "P.Anno",
            "peer.tube",
            "polyfill.js",
            "Pony.ai",
            "quantum.country",
            "Quokka.js",
            "redhat.com",
            "Sails.js",
            "scicomm.xyz",
            "sketch.dev",
            "The A.R.T Project",
            "Three.js",
            "tween.js",
            "typescriptlang.org",
            "U.S.",
            "Vectorize.io",
            "Venus.js",
            "view.json",
            "Wallaby.js",
            "Washington Z.C.",
            "xml:id",
            "X.org",
            "xsl:key",
            "Z.ai");

    private static final Set<String> s_authorizedMissingPrecedingSpaceList = Set.of(
            ".Net",
            ".NET");

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT,
            ElementType.TITLE,
            });

    /**
    * constructor
    */
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
            return new CheckStatus("MissingLanguage", "\"" + e.getNodeName() + "\" has no language", Optional.empty());
        }

        for (final String l: list) {
            if (Arrays.stream(l.split("[ /]")).anyMatch(s -> missesSpaceBeforePunctuation(s, locale.get()))) {
                return new CheckStatus("MissingSpace", "\"" + e.getTextContent() + "\" is missing a space before punctuation", Optional.empty());
            }
        }

       return null;
    }

    private static CheckStatus checkMissingSpaceAfterPunctuation(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        final Optional<Locale> locale = XmlHelper.getElementLanguage(e);
        if (locale.isEmpty()) {
            return new CheckStatus("MissingLanguage", "\"" + e.getNodeName() + "\" has no language", Optional.empty());
        }

        for (final String l: list) {
            if (Arrays.stream(l.split("[ /]")).anyMatch(s -> missesSpaceAfterPunctuation(s, locale.get()))) {
                return new CheckStatus("MissingSpace", "\"" + e.getTextContent() + "\" is missing a space after punctuation", Optional.empty());
            }
        }

       return null;
    }

    private static CheckStatus checkInvalidSpaceBeforePunctuation(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        final Optional<Locale> locale = XmlHelper.getElementLanguage(e);
        if (locale.isEmpty()) {
            return new CheckStatus("MissingLanguage", "\"" + e.getNodeName() + "\" has no language", Optional.empty());
        }

        for (final String l: list) {
            final int c = containsPunctuationPrecededByInvalidSpace(l, locale.get());
            if (c > 0) {
                return new CheckStatus("SpuriousSpace", "\"" + l + "\" has a space before a punctuation (at index " + c + ")", Optional.empty());
            }
        }

       return null;
    }

    private static boolean missesSpaceBeforePunctuation(final String str,
                                                        final Locale locale) {

        final char[] chars = str.toCharArray();

        if (isVersionString(chars) ||
            !containsPunctuationNotPrecededBySpace(chars, locale)) {
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

        if (isVersionString(chars) ||
            !containsPunctuationNotFollowedBySpace(chars, locale)) {
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
        for (final char element : chars) {
            if (Character.isLetter(element)) {
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
     * @return return index of error if the string contains a space that should not be present before a punctuation
     *                -1 if the string is correct
     */
    private static int containsPunctuationPrecededByInvalidSpace(final String str,
                                                                 final Locale locale) {
        for (int i = 1; i < str.length(); i++) {
            final char previousChar = str.charAt(i - 1);
            final char currentChar = str.charAt(i);
            if (Character.isSpaceChar(previousChar)) {
                if (startsWithAuthorizedMissingPrecedingSpaceList(str.substring(i))) {
                    return -1;
                }
                if (locale.equals(Locale.ENGLISH)) {
                    if (isEnglishPunctuationWithNoSpaceBefore(currentChar)) {
                        return i;
                    }
                } else if (locale.equals(Locale.FRENCH)) {
                    if (isFrenchPunctuationWithNoSpaceBefore(currentChar)) {
                        return i;
                    }
                } else if (locale.getLanguage().isEmpty()) {
                    if (isEnglishPunctuationWithNoSpaceBefore(currentChar) && isFrenchPunctuationWithNoSpaceBefore(currentChar)) {
                        return i;
                    }
                } else {
                    throw new UnsupportedOperationException("Illegal locale value (" + locale + ")");
                }
            }
        }
        return -1;
    }

    /**
     * @param str
     * @return return true if the string begins with a "word" beginning by space (e.g. ".Net")
     */
    private static boolean startsWithAuthorizedMissingPrecedingSpaceList(final String str) {
        final int spaceIndex = str.indexOf(' ');
        final String firstWord = (spaceIndex == -1) ? str : str.substring(0, spaceIndex);
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
