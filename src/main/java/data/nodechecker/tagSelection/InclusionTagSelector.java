package data.nodechecker.tagSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import utils.xmlparsing.ElementType;

public class InclusionTagSelector implements TagSelector {

    private final Set<ElementType> _types;

    /**
     * @param tagsToCheck
     */
    public InclusionTagSelector(final ElementType typesToIgnore[]) {
        _types = new HashSet<>(Arrays.asList(typesToIgnore));
    }

    @Override
    public boolean isTagCheckable(final ElementType type) {
        return _types.contains(type);
    }
}
