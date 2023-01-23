package data.linkchecker;

/**
 * Violation of a link content check
 */
public class LinkContentCheck {

    private final String _description;

    /**
     * Constructor
     * @param description Description of the check violation
     */
    public LinkContentCheck(final String description) {

        _description = description;
    }

    /**
     * @return Description of the check violation
     */
    public String getDescription() {
        return _description;
    }
}
