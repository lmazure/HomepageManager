package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

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
                                                                            "llm",
                                                                            "m4",
                                                                            "make",
                                                                            "n8n",
                                                                            "npm",
                                                                            "pip",
                                                                            "quantum.country",
                                                                            "rpm",
                                                                            "sed",
                                                                            "sh",
                                                                            "smolagents",
                                                                            "systat",
                                                                            "tgAAC94",
                                                                            "tkdiff",
                                                                            "vi",
                                                                            "xLSTM",
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
        if (list.isEmpty()) {
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
        if (list.isEmpty()) {
            return null;
        }

        final Optional<String> firstWord = Arrays.stream(list.get(0).split(" ")).findFirst();
        if (firstWord.isEmpty() ||
            (firstWord.get().isEmpty()) ||
            s_authorizedList.contains(firstWord.get())) {
            return null;
        }

        if (Character.isLowerCase(firstWord.get().codePointAt(0))) {
            return new CheckStatus("LowercaseTitle",
                                   "TITLE \"" + e.getTextContent() + "\" must start with an uppercase", Optional.empty());
        }

        return null;
    }
}
