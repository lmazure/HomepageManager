package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;

import java.util.List;

import org.w3c.dom.Element;

public class TitleFormatChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
	        NodeChecker.TITLE
			} );
	
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

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
		
		final List<String> list = XMLHelper.getFirstLevelTextContent(e);
		if (list.size() == 0) return null;

		if (list.get(list.size() - 1).endsWith(":")) return new CheckStatus("TITLE \"" + e.getTextContent() + "\" must not finish with colon");
		
		return null;
	}

	private CheckStatus titleStartsWithUppercase(final Element e) {
		
        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) return null;

	    if (Character.isLowerCase(list.get(0).codePointAt(0))) return new CheckStatus("TITLE \"" + e.getTextContent() + "\" must start with an uppercase");
		
		return null;
	}

}
