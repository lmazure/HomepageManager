package fr.mazure.homepagemanager.data;

import java.util.function.Predicate;

/**
 * Track the creation and deletion of violation
 */
public interface ViolationDataController {

    /**
     * add a new violation
     * @param violation violation to be added
     */
    void add(final Violation violation);

    /**
     * remove all violations matching the filter
     * @param violationFilter flter
     */
    void remove(final Predicate<Violation> violationFilter);
}
