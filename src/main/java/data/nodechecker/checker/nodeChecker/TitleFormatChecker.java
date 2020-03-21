package data.nodechecker.checker.nodeChecker;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.w3c.dom.Element;

public class TitleFormatChecker extends NodeChecker {

	static final Set<String> s_authorizedList = new HashSet<String>(Arrays.asList("abc",
			                                                                      "apt",
			                                                                      "autosrb.pl",
                                                                                  "awk",
                                                                                  "bash",
                                                                                  "csh",
                                                                                  "curses",
			                                                                      "ddd",
			                                                                      "deCODEme",
			                                                                      "e",
			                                                                      "ePRO",
			                                                                      "etrials",
			                                                                      "flexcipio",
			                                                                      "gcc",
			                                                                      "gdb",
			                                                                      "glibc",
			                                                                      "gulp",
			                                                                      "iostream",
			                                                                      "jQuery",
			                                                                      "ksh",
			                                                                      "lit-html",
			                                                                      "m4",
			                                                                      "make",
			                                                                      "npm",
			                                                                      "quantum.country",
			                                                                      "rpm",
			                                                                      "sed",
			                                                                      "sh",
			                                                                      "systat",
			                                                                      "tgAAC94",
			                                                                      "tkdiff",
			                                                                      "vi",
			                                                                      "xUnit",
			                                                                      "yacc",
			                                                                      "zsh",
			                                                                      "π"));

	static final InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
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
		
		/* TODO temporary disabled
		final List<String> list = XMLHelper.getFirstLevelTextContent(e);
		if (list.size() == 0) return null;

		if (list.get(list.size() - 1).endsWith(":")) return new CheckStatus("TITLE \"" + e.getTextContent() + "\" must not finish with colon");
		*/
		
		return null;
	}

	private CheckStatus titleStartsWithUppercase(final Element e) {
		
        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) {
        	return null;
        }

        final Optional<String> firstWord = Arrays.stream(list.get(0).split(" ")).findFirst();
        if (firstWord.isEmpty() || (firstWord.get().length() == 0)) {
        	return null;
        }
        		
	    if (s_authorizedList.contains(firstWord.get())) {
	    	return null;
	    }
	    
	    if (Character.isLowerCase(firstWord.get().codePointAt(0))) {
	    	return new CheckStatus("TITLE \"" + e.getTextContent() + "\" must start with an uppercase");
	    }
		
		return null;
	}
}
