package fr.mazure.homepagemanager.data;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Record of all violations
 */
public class ViolationManager {

    final Set<Violation> _set;

    /**
     * Constructor
     */
    public ViolationManager() {
        _set = new HashSet<>();
    }

    /**
     * Add a violation
     * @param violation Violation
     */
    public void add(final Violation violation) {
        _set.add(violation);
    }

    /**
     * Remove all violations matching a filter
     * @param filter Filter
     */
    public void remove(final Predicate<Violation> filter) {
        for (final Violation violation: _set) {
            if (filter.test(violation)) {
                _set.remove(violation);
            }
        }
    }
}
