package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.Set;

/**
 * a component of a redirection pattern
 */
public class RedirectionMatcherElement {

    /**
     * How many time can this element appear in the redirection chain 
     */
    public enum Multiplicity {
        /**
         * one 
         */
        ONE,
        /**
         * one or more
         */
        ONE_OR_MANY
    }
    
    private final String _regexp;
    private final Set<Integer> _httpCodes;
    private final Multiplicity _multiplicity;

    /**
     * @param regexp the regular expression on the URL
     * @param httpCodes the possible valued for the received HTTP code
     * @param multiplicity the number of times this element may appear in the chain of redirections
     */
    public RedirectionMatcherElement(final String regexp,
                                     final Set<Integer> httpCodes,
                                     final Multiplicity multiplicity) {
        _regexp = regexp;
        _httpCodes = httpCodes;
        _multiplicity = multiplicity;
    }

    /**
     * @return the regexp
     */
    public String getRegexp() {
        return _regexp;
    }

    /**
     * @return theHTTP codes
     */
    public Set<Integer> getHttpCodes() {
        return _httpCodes;
    }

    /**
     * @return the multiplicity
     */
    public Multiplicity getMultiplicity() {
        return _multiplicity;
    }
}
