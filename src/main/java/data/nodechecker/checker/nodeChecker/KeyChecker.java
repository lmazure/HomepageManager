package data.nodechecker.checker.nodeChecker;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.xmlparsing.ElementType;

public class KeyChecker extends NodeChecker {
    // TODO do we really need this checker? this should be verified by the schema

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.KEY
            });

    public KeyChecker() {
        super(s_selector,
              KeyChecker::checkKeyString, "the KEY is incorrect");
    }

    private static CheckStatus checkKeyString(final Element e) {

        final String str = e.getAttribute("id");

        final Pattern pattern = Pattern.compile("^([-+*/=A-Z0-9à^;:.,&\"'%#!?_)(]|F[1-9]|F10|F11|F12|Left|Up|Right|Down|Beginning|PageUp|PageDown|Space|Tab|Enter|Del|Backspace|Esc|Break|Ins|End|Return|Num [-+*/0-9])$");
        final Matcher matcher = pattern.matcher(str);

        if (matcher.find()) return null;

        return new CheckStatus("Illegal KEY (" + str + ")");
    }
}
