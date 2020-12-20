package data.nodechecker.checker.nodeChecker;

public class NodeCheckError {

    private final String _tag;
    private final String _value;
    private final String _violation;
    private final String _detail;

    public NodeCheckError(final String tag,
                          final String value,
                          final String violation,
                          final String detail) {
        _tag = tag;
        _value = value;
        _violation = violation;
        _detail = detail;
    }

    public String getTag() {
        return _tag;
    }

    public String getValue() {
        return _value;
    }

    public String getViolation() {
        return _violation;
    }

    public String getDetail() {
        return _detail;
    }
}