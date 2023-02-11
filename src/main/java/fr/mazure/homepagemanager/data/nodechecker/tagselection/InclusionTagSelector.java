package fr.mazure.homepagemanager.data.nodechecker.tagselection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
 * Filter, defined as a white list, on the tags to be checked
 */
public class InclusionTagSelector implements TagSelector {

    private final Set<ElementType> _types;

    /**
     * @param typesToInclude types of the tags to be checked
     */
    public InclusionTagSelector(final ElementType typesToInclude[]) {
        _types = new HashSet<>(Arrays.asList(typesToInclude));
    }

    @Override
    public boolean isTagCheckable(final ElementType type) {
        return _types.contains(type);
    }
}
