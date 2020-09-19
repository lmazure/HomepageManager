package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.xmlparsing.NodeType;

public class NonNormalizedAuthorChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector( new NodeType[] {
            NodeType.AUTHOR
            } );

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }


    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkFormat(e);}
                            @Override
                            public String getDescription() { return "uses a non-normalized author name"; } };
        return a;
    }

    private CheckStatus checkFormat(final Element e) {

        final String s = e.getTextContent();

        if (s.equalsIgnoreCase("Vint Cerf"))
            return new CheckStatus("\"Vint Cerf\" should be \"Vinton Cerf\"");

        if (s.equalsIgnoreCase("Cynthia Keen"))
            return new CheckStatus("\"Cynthia Keen\" should be \"Cynthia E. Keen\"");

        if (s.equalsIgnoreCase("Deb Borfitz"))
            return new CheckStatus("\"Deb Borfitz\" should be \"Deborah Borfitz\"");

        return null;
    }
}
