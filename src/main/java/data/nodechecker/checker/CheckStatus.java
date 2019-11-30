package data.nodechecker.checker;

public class CheckStatus {
    
    final String a_detail;
    
    /**
     * @param detail
     */
    public CheckStatus(final String detail) {
        a_detail = detail;
    }
    
    /**
     * @return detailed information on the check violation
     */
    public String getDetail() {
        return a_detail;
    }
};