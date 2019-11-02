package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class EllipsisChecker extends NodeChecker {

	static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new String[] {
			NodeChecker.ARTICLE,
			NodeChecker.BLIST,
			NodeChecker.CELL,
			NodeChecker.CLIST,
			NodeChecker.CONTENT,
			NodeChecker.I,
			NodeChecker.ITEM,
			NodeChecker.LLIST,
			NodeChecker.NLIST,
			NodeChecker.PAGE,
			NodeChecker.ROW,
			NodeChecker.ST,
			NodeChecker.T,
			NodeChecker.X
			});
	
	/**
	 * @see lmzr.homepagechecker.checker.nodeChecker.NodeChecker#getTagSelector()
	 */
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	/**
	 * @see lmzr.homepagechecker.checker.nodeChecker.NodeChecker#getRules()
	 */
	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[1];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkEllipsis(e);}
		                    @Override
							public String getDescription() { return "ellipsis is properly encoded"; } };
		return a;
	}

	private CheckStatus checkEllipsis(final Element e) {
		final String s = e.getTextContent();
		if ( s.indexOf("...") == -1 ) return null;
		return new CheckStatus("\""+s+"\" should not contain \"...\"");
		}
}
