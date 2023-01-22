package data;

/**
 *
 */
public class Violation {

    private final String _file;
    private final String _type;
    private final String _rule;
    private final ViolationLocation _location;
    private final String _description;
    private final ViolationCorrections[] _correction;

    /**
     * @param file
     * @param type
     * @param rule
     * @param location
     * @param description
     * @param correction
     */
    public Violation(final String file,
                     final String type,
                     final String rule,
                     final ViolationLocation location,
                     final String description,
                     final ViolationCorrections[] correction) {
        _file = file;
        _type = type;
        _rule = rule;
        _location = location;
        _description = description;
        _correction = correction;
    }
    /**
     * @return the file
     */
    public String getFile() {
        return _file;
    }

    /**
     * @return the type
     */
    public String getType() {
        return _type;
    }

    /**
     * @return the rule
     */
    public String getRule() {
        return _rule;
    }

    /**
     * @return the location
     */
    public ViolationLocation getLocation() {
        return _location;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * @return the correction
     */
    public ViolationCorrections[] getCorrection() {
        return _correction;
    }
}
