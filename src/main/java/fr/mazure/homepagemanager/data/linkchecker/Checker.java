package fr.mazure.homepagemanager.data.linkchecker;

import java.util.List;

/**
 * Interface for checkers
 */
public interface Checker {

    /**
     * Perform the check
     *
     * @return List of violations
     * @throws ContentParserException Failure to extract the information
     */
    public List<LinkContentCheck> check() throws ContentParserException;
}
