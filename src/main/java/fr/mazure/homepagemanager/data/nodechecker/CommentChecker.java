package fr.mazure.homepagemanager.data.nodechecker;

import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.data.violationcorrection.AddDotAtCommentEnd;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 *
 */
public class CommentChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.COMMENT
            });

    /**
    * constructor
    */
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

        return error ? new CheckStatus("MissingPuctuation",
                                       "COMMENT \"" + e.getTextContent() + "\" must end with a punctuation",
                                       Optional.of(new AddDotAtCommentEnd(list)))
                     : null;
    }

    private static boolean isFinalPunctuation(final char c) {
        return (c == '.') || (c == '?') || (c == '!') || (c == '…') || (c == '‽') || (c == '⸘');
    }
}
