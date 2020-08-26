package data.nodechecker.checker;

public class CheckStatus {
    
    final String _detail;
    
    /**
     * @param detail
     */
    public CheckStatus(final String detail) {
        _detail = detail;
    }
    
    /**
     * @return detailed information on the check violation
     */
    public String getDetail() {
        return _detail;
    }
}