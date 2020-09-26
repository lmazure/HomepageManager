package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.xmlparsing.NodeType;

import org.w3c.dom.Element;

public class LanguageChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new NodeType[] {
            NodeType.L
            });

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() {
            @Override
            public CheckStatus checkElement(final Element e) { return checkLanguage(e);}
            @Override
            public String getDescription() { return "unknown language"; } };
        return a;
    }

    /**
     * @param e
     * @return
     */
    private CheckStatus checkLanguage(final Element e) {

        final String s = e.getTextContent();

        if (s.equals("en")) return null;
        if (s.equals("fr")) return null;
        if (s.equals("de")) return null;

        return new CheckStatus("\"" + s + "\" is an unknown language");
    }

}
