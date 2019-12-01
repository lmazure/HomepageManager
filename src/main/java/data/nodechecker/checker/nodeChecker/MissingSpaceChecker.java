package data.nodechecker.checker.nodeChecker;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;

public class MissingSpaceChecker extends NodeChecker {
    
    static final InclusionTagSelector s_selector = new InclusionTagSelector(new String[] {
            NodeChecker.COMMENT,
            NodeChecker.TITLE,
            });
    
    static final Pattern pattern = Pattern.compile("\\s([a-zA-Z])+[\\.?:,;)\\]}][a-zA-Z]+\\s");

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkMissingSpace(e);}
                            @Override
                            public String getDescription() { return "space is missing"; } };
        return a;
    }

    private CheckStatus checkMissingSpace(final Element e) {

        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) return null;

        for (final String l: list) {
            final Matcher matcher = pattern.matcher(l);
            if (matcher.find()) {
                return new CheckStatus("\"" + e.getTextContent() + "\" is missing a space");
            }
        }

        return null;
    }    

}
