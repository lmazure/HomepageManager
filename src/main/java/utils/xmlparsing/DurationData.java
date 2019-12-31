package utils.xmlparsing;

import java.util.Optional;

public class DurationData {

    private final Integer _seconds;
    private final Optional<Integer> _minutes;
    private final Optional<Integer> _hours;
    
    public DurationData(final Integer seconds,
                        final Optional<Integer> minutes,
                        final Optional<Integer> hours) {
        _seconds = seconds;
        _minutes = minutes;
        _hours = hours;
    }

    public Integer getSeconds() {
        return _seconds;
    }

    public Optional<Integer> getMinutes() {
        return _minutes;
    }

    public Optional<Integer> getHours() {
        return _hours;
    }
}
