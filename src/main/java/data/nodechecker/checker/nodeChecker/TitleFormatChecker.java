package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


import org.w3c.dom.Element;

/**
 * @author Laurent
 *
 */
public class TitleFormatChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
	        NodeChecker.TITLE
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
		final NodeRule a[]= new NodeRule[2];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return titleDoesNotFinishWithColon(e);}
		                    @Override
							public String getDescription() { return "a TITLE must not finish with a colon"; } };
		a[1] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return titleStartsWithUppercase(e);}
		                    @Override
							public String getDescription() { return "a TITLE must start with an uppercase letter"; } };

        return a;
	}

	private CheckStatus titleDoesNotFinishWithColon(final Element e) {
		
		final String s = e.getTextContent();

		if (s.substring(s.length()-1).equals(":")) return new CheckStatus("TITLE \"" + s + "\" must not finish with colon");
		
		return null;
	}

	private CheckStatus titleStartsWithUppercase(final Element e) {
		
	    //TODO <TITLE><ANCHOR>foo<ANCHOR>Bar</TITLE> returns an error (because the text is "fooBar") while it should not
	    
	    final String s = XMLHelper.getFirstLevelTextContent(e);

		if (Character.isLowerCase(s.codePointAt(0))) return new CheckStatus("TITLE \"" + s + "\" must start with an uppercase");
		
		return null;
	}

}
