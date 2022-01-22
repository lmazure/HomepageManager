package data.nodechecker.checker.nodeChecker;

import java.util.List;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class CommentChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT
            });

    public CommentChecker() {
        super(s_selector,
              CommentChecker::commentFinishesWithPunctuation,"a COMMENT must finish with a punctuation");
    }

    private static CheckStatus commentFinishesWithPunctuation(final Element e) {
        final List<String> list = XmlHelper.getFirstLevelTextContent(e);
        if (list.isEmpty()) {
            return null;
        }
        final String lastPart = list.get(list.size() - 1);
        final char lastChar = lastPart.toCharArray()[lastPart.length() - 1];

        boolean error = false;
        if ((lastChar == ')') || (lastChar == '"')) {
            if (lastPart.length() > 1) {
                final char previousChar = lastPart.toCharArray()[lastPart.length() - 2];
                error = !isFinalPunctuation(previousChar);
            }
        } else {
            error = !isFinalPunctuation(lastChar);
        }

        return error ? new CheckStatus("COMMENT \"" + e.getTextContent() + "\" must end with a punctuation")
                     : null;
    }

    static boolean isFinalPunctuation(final char c) {
        return (c == '.') || (c == '?') || (c == '!') || (c == '…') || (c == '‽');
    }
}
