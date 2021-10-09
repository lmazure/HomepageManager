package data.nodechecker.checker.nodeChecker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class IncorrectSpaceChecker extends NodeChecker {

    static final Set<String> s_authorizedMissingSpaceList = new HashSet<>(Arrays.asList(
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
            "Heu?reka",
            "i.e.",
            "Intl.RelativeTimeFormat",
            "Intro.js",
            "Java.Next",
            "Kosmopoli:t",
            "MANIFEST.MF",
            "MSCTF.DLL",
            "MVC.NET",
            ".NET",
            "Node.js",
            "Normalize.css",
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

    static final Set<String> s_authorizedMissingPrecedingSpaceList = new HashSet<>(Arrays.asList(
            ".NET"));

    static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT,
            ElementType.TITLE,
            });

    public IncorrectSpaceChecker() {
        super(s_selector,
              IncorrectSpaceChecker::checkMissingSpace, "space is missing",
              IncorrectSpaceChecker::checkSpaceBeforePunctuation, "space is present before punctuation");
    }

    private static CheckStatus checkMissingSpace(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);

        for (final String l: list) {
            if (Arrays.stream(l.split("[ /]")).anyMatch(IncorrectSpaceChecker::missesSpaceBeforePunctuation)) {
                return new CheckStatus("\"" + e.getTextContent() + "\" is missing a space");
            }
        }

       return null;
    }

    private static CheckStatus checkSpaceBeforePunctuation(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);

        for (final String l: list) {
            if (containsPunctuationPrecededBySpace(l)) {
                return new CheckStatus("\"" + l + "\" has a space before a punctuation");
            }
        }

       return null;
    }

    private static boolean missesSpaceBeforePunctuation(final String str) {

        final char[] chars = str.toCharArray();

        if (isVersionString(chars)) {
            return false;
        }

        if (!containsPunctuationNotFollowedBySpace(chars)) {
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

    private static boolean containsPunctuationNotFollowedBySpace(final char[] chars) {
        for (int i = 0; i < chars.length - 1; i++) {
            if (isPunctuation(chars[i]) && Character.isAlphabetic(chars[i + 1])) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsPunctuationPrecededBySpace(final String str) {
        for (int i = 1; i < str.length(); i++) {
            final char c1 = str.charAt(i - 1);
            final char c2 = str.charAt(i);
            if (isPunctuation(c2) && Character.isSpaceChar(c1)) {
                if (!startsWithAuthorizedMissingPrecedingSpaceList(str.substring(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean startsWithAuthorizedMissingPrecedingSpaceList(final String str) {
        final String[] spl = str.split(" ", 2);
        final String firstWord = spl[0];
        return s_authorizedMissingPrecedingSpaceList.contains(firstWord);
    }

    private static boolean isPunctuation(final char c) {
        return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
    }
}
