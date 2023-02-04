package data.linkchecker;

/**
 * Violation of a link content check
 */
public class LinkContentCheck {

    private final String _checkName;
    private final String _description;

    /**
     * Constructor
     * @param checkName Name of the check
     * @param description Description of the check violation
     */
    public LinkContentCheck(final String checkName,
                            final String description) {
        _checkName = checkName;
        _description = description;
    }

    /**
     * @return Name of the check
     */
    public String getCheckName() {
        return _checkName;
    }

    /**
     * @return Description of the check violation
     */
    public String getDescription() {
        return _description;
    }
}
