package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


/**
 * @author Laurent
 *
 */
public class NonNormalizedURLChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
	        NodeChecker.A
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
							public String getDescription() { return "uses a non-normalized URL"; } };
		return a;
	}

	private CheckStatus checkFormat(final Element e) {

		final String s = e.getTextContent();
		
		if (s.contains("youtube.fr"))
			return new CheckStatus("\"youtube.fr\" should be \"youtube.com\"");
			
		if (s.contains("fr.youtube"))
			return new CheckStatus("\"fr.youtube\" should be \"youtube.com\"");
			
		if (s.contains("google.fr"))
			return new CheckStatus("\"google.fr\" should be \"google.com\"");

		if (s.contains("www-128.ibm.com"))
			return new CheckStatus("\"www-128.ibm.com\" should be \"www.ibm.com\"");

		return null;
	}
}
