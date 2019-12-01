package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;

import java.util.List;

import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class DoubleSpaceChecker extends NodeChecker {

	static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new String[] {
			NodeChecker.BLIST,
			NodeChecker.CLIST,
			NodeChecker.CODESAMPLE,
			NodeChecker.CONTENT,
			NodeChecker.DEFINITIONTABLE,
			NodeChecker.ITEM,
			NodeChecker.LLIST,
			NodeChecker.NLIST,
			NodeChecker.PAGE,
			NodeChecker.ROW,
			NodeChecker.SCRIPT,
			NodeChecker.SLIST,
			NodeChecker.TEXTBLOCK
			});
	
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[1];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkDoubleSpace(e);}
		                    @Override
							public String getDescription() { return "double space is present"; } };
		return a;
	}

	private CheckStatus checkDoubleSpace(final Element e) {
	    
        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) return null;

        for (final String l: list) {
    		if (l.indexOf("  ") >= 0) {
    	        return new CheckStatus("\"" + e.getTextContent() + "\" should not contain a double space");
    		}
        }

        return null;
	}
}
