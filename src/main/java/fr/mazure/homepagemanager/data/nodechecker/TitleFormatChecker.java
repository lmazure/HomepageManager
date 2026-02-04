package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 *
 */
public class TitleFormatChecker extends NodeChecker {

    private static final Pattern s_authorizedPattern;

    static {
        final List<String> authorizedList = Arrays.asList("abc",
                                                          "apt",
                                                          "autosrb\\.pl",
                                                          "awk",
                                                          "bash",
                                                          "blackpenredpen",
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
                                                          "gpt-.*",
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
                                                          "quantum\\.country",
                                                          "rpm",
                                                          "sed",
                                                          "sh",
                                                          "smolagents",
                                                          "systat",
                                                          "tgAAC94",
                                                          "tinylog",
                                                          "tkdiff",
                                                          "tmux",
                                                          "uv",
                                                          "vi",
                                                          "xLSTM",
                                                          "xUnit",
                                                          "yacc",
                                                          "zsh",
                                                          "π");
        final String combinedPattern = authorizedList.stream()
                                                       .map(pattern -> "(" + pattern + ")")
                                                       .collect(Collectors.joining("|"));
        s_authorizedPattern = Pattern.compile(combinedPattern);
    }

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.TITLE
            });

    /**
    * constructor
    */
    public TitleFormatChecker() {
        super(s_selector,
              TitleFormatChecker::titleStartsWithUppercase,"a TITLE must start with an uppercase letter");
    }

    private static CheckStatus titleStartsWithUppercase(final Element e) {

        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.isEmpty()) {
            return null;
        }

        final Optional<String> firstWord = Arrays.stream(list.get(0).split(" ")).findFirst();
        if (firstWord.isEmpty() || firstWord.get().isEmpty()) {
            return null;
        }

        if (s_authorizedPattern.matcher(firstWord.get()).matches()) {
            return null;
        }

        if (Character.isLowerCase(firstWord.get().codePointAt(0))) {
            return new CheckStatus("LowercaseTitle",
                                   "TITLE \"" + e.getTextContent() + "\" must start with an uppercase", Optional.empty());
        }

        return null;
    }
}
