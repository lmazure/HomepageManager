package data.nodechecker.tagSelection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.xmlparsing.NodeType;

/**
 * @author Laurent
 *
 */
public class InclusionTagSelector implements TagSelector {

	final private Set<String> a_tags;
	
	/**
	 * @param tagsToCheck
	 */
	public InclusionTagSelector(final NodeType tagsToCheck[]) {
		a_tags = Stream.of(tagsToCheck)
				       .map(NodeType::toString)
				       .collect(Collectors.toSet());
	}
	
	@Override
	public boolean isTagCheckable(final String tag) {
		return a_tags.contains(tag);
	}
}
