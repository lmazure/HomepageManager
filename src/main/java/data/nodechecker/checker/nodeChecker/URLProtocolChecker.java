package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class URLProtocolChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
			NodeChecker.A
			} );
	
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
		public CheckStatus checkElement(final Element e) { return checkFormat(e);}
		                    @Override
							public String getDescription() { return "uses a non-normalized URL"; } };
		return a;
	}

	private CheckStatus checkFormat(final Element e) {

		final String s = e.getTextContent();
		
		if(s.indexOf(':')<0) return null; // pointer to another of my page
		
		if (s.startsWith("http:")) return null;
		if (s.startsWith("https:")) return null;
		if (s.startsWith("ftp:")) return null;
		if (s.startsWith("mailto:")) return null;
		if (s.startsWith("javascript:")) return null;
		if (s.startsWith("file:")) return null;
		
		return new CheckStatus("unknown protocol for URL \""+s+"\"");
	}
}
