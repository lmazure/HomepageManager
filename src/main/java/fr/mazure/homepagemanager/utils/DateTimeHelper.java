package fr.mazure.homepagemanager.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Helper to manage dates, times, and durations
 */
public class DateTimeHelper {

    private static final ZoneId s_parisZone = ZoneId.of("Europe/Paris");

    /**
     * Convert a string in ISO8601 format to a LocalDate
     *
     * @param str string
     * @return LocalDate
     */
    public static LocalDate convertISO8601StringToDateTime(final String str) {
        final Instant instant = Instant.parse(str);
        final ZonedDateTime franceDateTime = instant.atZone(s_parisZone);
        return LocalDate.from(franceDateTime);
    }

    /**
     * Convert a long to a LocalDate
     *
     * @param value long
     * @return LocalDate
     */
    public static LocalDate convertLongToDateTime(final long value) {
        final Instant instant = Instant.ofEpochMilli(value);
        final ZonedDateTime franceDateTime = instant.atZone(s_parisZone);
        return LocalDate.from(franceDateTime);
    }

    /**
     * Convert a TemporalAccessor to a LocalDate
     *
     * @param accessor TemporalAccessor
     * @return LocalDate or empty if the conversion is impossible
     */
    public static Optional<LocalDate> convertTemporalAccessorToLocalDate(final Optional<TemporalAccessor> accessor) {
        if (accessor.isEmpty()) {
	        return Optional.empty();
        }
        if (!accessor.get().isSupported(ChronoField.YEAR) ||
            !accessor.get().isSupported(ChronoField.MONTH_OF_YEAR) ||
            !accessor.get().isSupported(ChronoField.DAY_OF_MONTH)) {
            return Optional.empty();
        }

        return Optional.of(convertTemporalAccessorToLocalDate(accessor.get()));
    }

    /**
     * Convert a TemporalAccessor to a LocalDate
     *
     * @param accessor TemporalAccessor
     * @return LocalDate 
     */
    public static LocalDate convertTemporalAccessorToLocalDate(final TemporalAccessor accessor) {
        return LocalDate.of(accessor.get(ChronoField.YEAR), accessor.get(ChronoField.MONTH_OF_YEAR), accessor.get(ChronoField.DAY_OF_MONTH));
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
