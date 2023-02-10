package data.nodechecker;

import data.nodechecker.tagselection.InclusionTagSelector;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Element;

/**
*
*/
public class TitleFormatChecker extends NodeChecker {

    private static final Set<String> s_authorizedList = new HashSet<>(Arrays.asList(
                                                                            "abc",
                                                                            "apt",
                                                                            "autosrb.pl",
                                                                            "awk",
                                                                            "bash",
                                                                            "csh",
                                                                            "curses",
                                                                            "ddd",
                                                                            "deCODEme",
                                                                            "e",
                                                                            "ePRO",
                                                                            "etrials",
                                                                            "flexcipio",
                                                                            "gcc",
                                                                            "gdb",
                                                                            "glibc",
                                                                            "gulp",
                                                                            "iostream",
                                                                            "jQuery",
                                                                            "ksh",
                                                                            "lit-html",
                                                                            "m4",
                                                                            "make",
                                                                            "npm",
                                                                            "quantum.country",
                                                                            "rpm",
                                                                            "sed",
                                                                            "sh",
                                                                            "systat",
                                                                            "tgAAC94",
                                                                            "tkdiff",
                                                                            "vi",
                                                                            "xUnit",
                                                                            "yacc",
                                                                            "zsh",
                                                                            "π"));

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.TITLE
            });

    /**
    * constructor
    */
    public TitleFormatChecker() {
        super(s_selector,
              // TitleFormatChecker::titleDoesNotFinishWithColon, "a TITLE must not finish with a colon",
              TitleFormatChecker::titleStartsWithUppercase,"a TITLE must start with an uppercase letter");
    }

    /* TODO temporary disabled
    private static CheckStatus titleDoesNotFinishWithColon(final Element e) {

        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) {
            return null;
        }

        if (list.get(list.size() - 1).endsWith(":")) {
            return new CheckStatus("TITLE \"" + e.getTextContent() + "\" must not finish with colon");
        }

        return null;
    }
    */

    private static CheckStatus titleStartsWithUppercase(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) {
            return null;
        }

        final Optional<String> firstWord = Arrays.stream(list.get(0).split(" ")).findFirst();
        if (firstWord.isEmpty() || (firstWord.get().length() == 0)) {
            return null;
        }

        if (s_authorizedList.contains(firstWord.get())) {
            return null;
        }

        if (Character.isLowerCase(firstWord.get().codePointAt(0))) {
            return new CheckStatus("LowercaseTitle",
                                   "TITLE \"" + e.getTextContent() + "\" must start with an uppercase", Optional.empty());
        }

        return null;
    }
}
