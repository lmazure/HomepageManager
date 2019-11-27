package data.nodechecker.tagSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Laurent
 *
 */
public class ExclusionTagSelector implements TagSelector {

    final private Set<String> a_tags;
	
	/**
	 * @param tagsToIgnore
	 */
	public ExclusionTagSelector(final String tagsToIgnore[]) {
        a_tags = new HashSet<String>(Arrays.asList(tagsToIgnore));
	}
	
	@Override
	public boolean isTagCheckable(final String tag) {
        return !a_tags.contains(tag);
	}
}
