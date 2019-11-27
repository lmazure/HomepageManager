package data.nodechecker.tagSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Laurent
 *
 */
public class InclusionTagSelector implements TagSelector {

	final private Set<String> a_tags;
	
	/**
	 * @param tagsToCheck
	 */
	public InclusionTagSelector(final String tagsToCheck[]) {
		a_tags = new HashSet<String>(Arrays.asList(tagsToCheck));
	}
	
	@Override
	public boolean isTagCheckable(final String tag) {
		return a_tags.contains(tag);
	}
}
