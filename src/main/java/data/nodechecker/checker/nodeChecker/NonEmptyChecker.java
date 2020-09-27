package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.xmlparsing.ElementType;

import org.w3c.dom.Element;

public class NonEmptyChecker extends NodeChecker {

    static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new ElementType[] {
            ElementType.BR,
            ElementType.CELL,
            ElementType.CONTENT, // added to avoid an OutOfMemoryError
            ElementType.KEY,
            ElementType.LINE,
            ElementType.MODIFIERKEY,
            ElementType.PAGE,    // added to avoid an OutOfMemoryError
            ElementType.PROMPT,
            ElementType.TAB,
            ElementType.TABCHAR
            });

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkNonEmpty(e);}
                            @Override
                            public String getDescription() { return "empty element"; } };
        return a;
    }

    private CheckStatus checkNonEmpty(final Element e) {

        final String s = e.getTextContent();

        if ((s.length()>0) || (e.getChildNodes().getLength()>0)) return null;

        return new CheckStatus("node shall not be empty");
    }
}
