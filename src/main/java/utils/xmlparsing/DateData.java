package utils.xmlparsing;

import java.util.Optional;

public class DateData {

    private final Integer _year;
    private final Optional<Integer> _month;
    private final Optional<Integer> _day;
    
    public DateData(final Integer year,
                    final Optional<Integer> month,
                    final Optional<Integer> day) {
        _year = year;
        _month = month;
        _day = day;
    }

    public Integer getYear() {
        return _year;
    }

    public Optional<Integer> getMonth() {
        return _month;
    }

    public Optional<Integer> getDay() {
        return _day;
    }
}
