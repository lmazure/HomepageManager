package data.nodechecker.tagSelection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import utils.xmlparsing.NodeType;

/**
 * @author Laurent
 *
 */
public class ExclusionTagSelector implements TagSelector {

    final private Set<String> _tags;

    /**
     * @param tagsToIgnore
     */
    public ExclusionTagSelector(final NodeType tagsToIgnore[]) {
        _tags = Stream.of(tagsToIgnore)
                          .map(NodeType::toString)
                       .collect(Collectors.toSet());
    }

    @Override
    public boolean isTagCheckable(final String tag) {
        return !_tags.contains(tag);
    }
}
