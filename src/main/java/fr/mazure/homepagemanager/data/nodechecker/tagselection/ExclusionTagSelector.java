package fr.mazure.homepagemanager.data.nodechecker.tagselection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;

/**
 * Filter, defined as a black list, on the tags to be checked
 */
public class ExclusionTagSelector implements TagSelector {

    private final Set<ElementType> _types;

    /**
     * @param typesToIgnore  types of the tags not to be checked
     */
    public ExclusionTagSelector(final ElementType typesToIgnore[]) {
        _types = new HashSet<>(Arrays.asList(typesToIgnore));
    }

    @Override
    public boolean isTagCheckable(final ElementType type) {
        return !_types.contains(type);
    }
}
