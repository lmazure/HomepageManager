package data;

/**
 * Class defining the location of a violation as on a given line
 */
public class ViolationLocationLine extends ViolationLocation {

    private final int _lineNumber;
    
    /**
     * Constructor
     * @param lineNumber line where is the violation
     */
    public ViolationLocationLine(final int lineNumber) {
        _lineNumber = lineNumber;
    }

    @Override
    final String getDescription() {
        return "line " + _lineNumber;
    }

}
