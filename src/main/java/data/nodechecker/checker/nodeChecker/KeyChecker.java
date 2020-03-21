package data.nodechecker.checker.nodeChecker;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;

public class KeyChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
			NodeChecker.KEY
			} );

	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[1];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return checkKeyString(e);}
		                    @Override
							public String getDescription() { return "the KEY is correct"; } };

        return a;
	}

	private CheckStatus checkKeyString(final Element e) {
		
		final String str = e.getAttribute("ID");
		
		final Pattern pattern = Pattern.compile("^([-+*/=A-Z0-9;:.,&\"'%#!?_)(]|F[1-9]|F10|F11|F12|Left|Up|Right|Down|Beginning|PageUp|PageDown|Space|Tab|Enter|Del|Backspace|Esc|Break|Ins|End|Return|Num [-+*/0-9])$");
		final Matcher matcher = pattern.matcher(str);
		
		if (matcher.find()) return null;

		return new CheckStatus("Illegal KEY ("+ str + ")");
	}
}
