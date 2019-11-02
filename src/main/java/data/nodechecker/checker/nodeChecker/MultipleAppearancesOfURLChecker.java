package data.nodechecker.checker.nodeChecker;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


/**
 * @author Laurent
 *
 */
public class MultipleAppearancesOfURLChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
			NodeChecker.A
			} );
	private static final Map<String, Set<String>> s_records = Collections.synchronizedMap(new HashMap<String, Set<String>>());
	
	final String a_fileToBeChecked;
	
	/**
	 * @param file
	 */
	public MultipleAppearancesOfURLChecker(final File file) {
		
		super();
		
		a_fileToBeChecked = file.getName();
	}
	
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
							public CheckStatus checkElement(final Element e) { return checkMultipleAppearanceOfURL(e);}
				            @Override
							public String getDescription() { return "A given URL should only appear once"; } };
		return a;
	}

	private CheckStatus checkMultipleAppearanceOfURL(final Element e) {

		final String s = e.getTextContent();
		
		if (!s.contains(":")) { // TODO create a method to check if a URL is local
		    // ignore local links
		    return null;
		}

		if ( s_records.containsKey(s) ) {
			final Set<String> record = s_records.get(s);
			record.add(a_fileToBeChecked);
			String message = "URL \""
			    + s
			    + "\" is present on pages:";
			for ( String w : record ) {
				message += " " + w;
			}
			return new CheckStatus(message);
		}
		
        final Set<String> record = new HashSet<String>();
        record.add(a_fileToBeChecked);
        s_records.put(s, record);
        return null;
	}

}
