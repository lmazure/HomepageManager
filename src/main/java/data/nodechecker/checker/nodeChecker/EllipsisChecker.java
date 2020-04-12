package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;

import java.util.List;

import org.w3c.dom.Element;

public class EllipsisChecker extends NodeChecker {

	static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new String[] {
			NodeChecker.A,
			NodeChecker.ARTICLE,
			NodeChecker.BLIST,
			NodeChecker.CELL,
			NodeChecker.CLIST,
			NodeChecker.CODESAMPLE,
			NodeChecker.CONTENT,
			NodeChecker.I,
			NodeChecker.ITEM,
			NodeChecker.LLIST,
			NodeChecker.NLIST,
			NodeChecker.PAGE,
			NodeChecker.ROW,
			NodeChecker.SCRIPT,
			NodeChecker.ST,
			NodeChecker.T,
			NodeChecker.X
			});
	
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[2];
		a[0] = new NodeRule() { @Override
		                        public CheckStatus checkElement(final Element e) { return checkEllipsis(e);}
		                        @Override
							    public String getDescription() { return "ellipsis is properly encoded"; } };
        a[1] = new NodeRule() { @Override
			                    public CheckStatus checkElement(final Element e) { return checkDoubleDot(e);}
			                    @Override
								public String getDescription() { return "double dot"; } };
		return a;
	}

	private CheckStatus checkEllipsis(final Element e) {
		
		final String s = e.getTextContent();
		if (s.indexOf("...") == -1) {
			return null;
		}

		return new CheckStatus("\"" + s + "\" should not contain \"...\"");
	}

	private CheckStatus checkDoubleDot(final Element e) {
		
		final List<String> content = XMLHelper.getFirstLevelTextContent(e);
		for (final String s: content) {
			if (s.indexOf("..") >= 0) {
				return new CheckStatus("\"" + s + "\" contains \"..\"");
			}
		}
		return null;
	}
}
