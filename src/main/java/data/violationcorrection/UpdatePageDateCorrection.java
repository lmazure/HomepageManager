package data.violationcorrection;

import java.util.Calendar;

/**
 * Correct the page by setting its date to day
 */
public class UpdatePageDateCorrection extends RegexpViolationCorrection {

    private final static Calendar s_now = Calendar.getInstance();
    private final static int s_now_year = s_now.get(Calendar.YEAR);
    private final static int s_now_month = s_now.get(Calendar.MONTH)+1;
    private final static int s_now_day = s_now.get(Calendar.DAY_OF_MONTH);

    /**
     * Constructor
     */
    public UpdatePageDateCorrection() {
        super("Update the page date",
              "</PATH>\r\n<DATE><YEAR>\\d{4}</YEAR><MONTH>\\d{1,2}</MONTH><DAY>\\d{1,2}</DAY></DATE>\r\n<CONTENT>",
              "</PATH>\r\n<DATE><YEAR>" + s_now_year + "</YEAR><MONTH>" + s_now_month + "</MONTH><DAY>" + s_now_day + "</DAY></DATE>\r\n<CONTENT>");
    }
}
