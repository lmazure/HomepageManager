package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class FormatChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
			NodeChecker.F
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
							public String getDescription() { return "unknown format"; } };
		return a;
	}

	/**
	 * @param e
	 * @return
	 */
	private CheckStatus checkFormat(final Element e) {
		
		final String s = e.getTextContent();
		
		if (s.equals("HTML")) return null;
		if (s.equals("PDF")) return null;
		if (s.equals("Flash Video")) return null;
		if (s.equals("Word")) return null;
		if (s.equals("PostScript")) return null;
		if (s.equals("Flash")) return null;
		if (s.equals("PowerPoint")) return null;
		if (s.equals("ASCII")) return null;
		if (s.equals("RSS")) return null;
		if (s.equals("RSS2")) return null;
		if (s.equals("MP3")) return null;
		if (s.equals("MP4")) return null;
		if (s.equals("RealMedia")) return null;
		if (s.equals("Windows Media Player")) return null;
		if (s.equals("Atom")) return null;
		if (s.equals("txt")) return null;
		
		return new CheckStatus("\""+s+"\" is a unknown format");
	}

}
