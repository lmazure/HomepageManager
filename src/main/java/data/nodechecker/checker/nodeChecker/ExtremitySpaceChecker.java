
package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.ExclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class ExtremitySpaceChecker extends NodeChecker {

	static final ExclusionTagSelector s_selector = new ExclusionTagSelector(new String[] {
			NodeChecker.BLIST,
			NodeChecker.CELL,
			NodeChecker.CLIST,
			NodeChecker.CODEFILE,
			NodeChecker.CODESAMPLE,
			NodeChecker.CONTENT,
			NodeChecker.DEFINITION2TABLE,
			NodeChecker.DEFINITIONTABLE,
			NodeChecker.DESC,
			NodeChecker.ITEM,
			NodeChecker.LLIST,
			NodeChecker.NLIST,
			NodeChecker.PAGE,
			NodeChecker.ROW,
			NodeChecker.SCRIPT,
			NodeChecker.SLIST,
			NodeChecker.TABLE,
			NodeChecker.TERM,
			NodeChecker.TEXTBLOCK
			});
	
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[2];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkSpaceAtBeginning(e);}
		                    @Override
							public String getDescription() { return "space at the beginning"; } };
	    a[1] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkSpaceAtEnd(e);}
		                    @Override
							public String getDescription() { return "space at the end"; } };
		return a;
	}

	private CheckStatus checkSpaceAtBeginning(final Element e) {
		final String s = e.getTextContent();
		if (s.length()==0) return null;
		char c = s.charAt(0);
		if (!Character.isWhitespace(c)) return null;
		return new CheckStatus("\"" + s + "\" should not begin with a space");
		}

	private CheckStatus checkSpaceAtEnd(final Element e) {
		final String s = e.getTextContent();
		if (s.length()==0) return null;
		char c = s.charAt(s.length()-1);
		if (!Character.isWhitespace(c)) return null;
		return new CheckStatus("\"" + s + "\" should not end with a space");
	}
}
