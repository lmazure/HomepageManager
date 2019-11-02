package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class MiddleNewlineChecker extends NodeChecker {

	static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new String[] {
			NodeChecker.B,
			NodeChecker.BLIST,
			NodeChecker.CELL,
			NodeChecker.CLIST,
			NodeChecker.CODESAMPLE,
			NodeChecker.CONTENT,
			NodeChecker.DEFINITION2TABLE,
			NodeChecker.DEFINITIONTABLE,
			NodeChecker.DESC,
			NodeChecker.I,
			NodeChecker.ITEM,
			NodeChecker.LLIST,
			NodeChecker.NLIST,
			NodeChecker.PAGE,
			NodeChecker.ROW,
			NodeChecker.SCRIPT,
			NodeChecker.SLIST,
			NodeChecker.SMALL,
			NodeChecker.TABLE,
			NodeChecker.TERM,
			NodeChecker.TERM1,
			NodeChecker.TEXTBLOCK
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
		public CheckStatus checkElement(final Element e) { return checkNewline(e);}
		                    @Override
							public String getDescription() { return "newline"; } };
		return a;
	}

	private CheckStatus checkNewline(final Element e) {
		final String s = e.getTextContent();
		if ( s.indexOf('\n') == -1 ) return null;
		return new CheckStatus("\"" + s + "\" should not contain a newline");
	}
}
