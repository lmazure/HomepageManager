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

public class MissingSpaceChecker extends NodeChecker {

    static final Set<String> s_authorizedList = new HashSet<>(Arrays.asList("2.X",
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

    static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT,
            ElementType.TITLE,
            });

    public MissingSpaceChecker() {
        super(s_selector,
              MissingSpaceChecker::checkMissingSpace, "space is missing");
    }

    private static CheckStatus checkMissingSpace(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) return null;

        for (final String l: list) {
            if (Arrays.stream(l.split("[ /]")).anyMatch(MissingSpaceChecker::isInvalid)) {
                return new CheckStatus("\"" + e.getTextContent() + "\" is missing a space");
            }
        }

       return null;
    }

    private static boolean isInvalid(final String str) {

        final char[] chars = str.toCharArray();

        if (isVersionString(chars)) return false;

        if (!containsPunctuation(chars)) {
            return false;
        }

        for (final String a: s_authorizedList) {
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
            if (Character.isLetter(chars[i])) return false;
        }
        return true;
    }

    private static boolean isVersionString(final char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (!isPunctuation(chars[i]) && !Character.isDigit(chars[i])) return false; // TODO should test for dot instead of punctuation
        }
        return true;
    }

    private static boolean containsPunctuation(final char[] chars) {
        for (int i = 0; i < chars.length - 1; i++) {
            if (isPunctuation(chars[i])  && Character.isAlphabetic(chars[i + 1])) return true;
        }
        return false;
    }

    private static boolean isPunctuation(final char c) {
        return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
    }
}
