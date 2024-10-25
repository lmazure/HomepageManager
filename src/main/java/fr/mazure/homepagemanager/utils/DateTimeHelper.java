package fr.mazure.homepagemanager.utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Helper to manage dates, times, and durations
 */
public class DateTimeHelper {

    /**
     * Convert a TemporalAccessor to a LocalDate
     * @param accessor TemporalAccessor
     * @return LocalDate or empty if the conversion is impossible
     */
    public static Optional<LocalDate> convertTemporalAccessorToLocalDate(final TemporalAccessor accessor) {
        if (!accessor.isSupported(ChronoField.YEAR) ||
            !accessor.isSupported(ChronoField.MONTH_OF_YEAR) ||
            !accessor.isSupported(ChronoField.DAY_OF_MONTH)) {
            return Optional.empty();
        }
        final LocalDate date = LocalDate.of(accessor.get(ChronoField.YEAR), accessor.get(ChronoField.MONTH_OF_YEAR), accessor.get(ChronoField.DAY_OF_MONTH));
        return Optional.of(date);
    }

    /**
     * Round a duration to the nearest second
     *
     * @param duration duration
     * @return rounded duration
     */
    public static Duration roundDuration(final Duration duration) {
		return Duration.ofSeconds(duration.plus(Duration.ofMillis(500)).getSeconds());
	}
}
