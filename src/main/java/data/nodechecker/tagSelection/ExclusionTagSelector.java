package data.nodechecker.tagSelection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import utils.xmlparsing.ElementType;

public class ExclusionTagSelector implements TagSelector {

    private final Set<ElementType> _types;

    /**
     * @param typesToIgnore
     */
    public ExclusionTagSelector(final ElementType typesToIgnore[]) {
        _types = new HashSet<ElementType>(Arrays.asList(typesToIgnore));
    }

    @Override
    public boolean isTagCheckable(final ElementType type) {
        return !_types.contains(type);
    }
}
