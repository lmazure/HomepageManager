package utils.xmlparsing;

import java.util.Optional;

public class DateData {

    private final Integer _dateYear;
    private final Optional<Integer> _dateMonth;
    private final Optional<Integer> _dateDay;
    
    public DateData(final Integer dateYear,
                    final Optional<Integer> dateMonth,
                    final Optional<Integer> dateDay) {
        _dateYear = dateYear;
        _dateMonth = dateMonth;
        _dateDay = dateDay;
    }

    /**
     * @return the dateYear
     */
    public Integer getDateYear() {
        return _dateYear;
    }

    /**
     * @return the dateMonth
     */
    public Optional<Integer> getDateMonth() {
        return _dateMonth;
    }

    /**
     * @return the dateDay
     */
    public Optional<Integer> getDateDay() {
        return _dateDay;
    }

}
