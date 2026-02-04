package fr.mazure.homepagemanager.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static final Pattern DURATION_PATTERN = 
            Pattern.compile("(?:(\\d+)\\s*h(?:ours?)?)?\\s*(?:(\\d+)\\s*m(?:in(?:utes?)?)?)?\\s*(?:(\\d+)\\s*s(?:ec(?:onds?)?)?)?");
        
    /**
     * Convert a string to a duration
     *  "37 min 59 sec" -> PT37M59S
     *  "2 h 15 min 30 sec" -> PT2H15M30S
     *  "45 sec" -> PT45S
     *  "1 hour 30 minutes" -> PT1H30M
     *
     * @param input string
     * @return duration
     */
    public static Duration parseDuration(String input) {

        final Matcher matcher = DURATION_PATTERN.matcher(input.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration format: " + input);
        }
        
        final long hours = matcher.group(1) != null ? Long.parseLong(matcher.group(1)) : 0;
        final long minutes = matcher.group(2) != null ? Long.parseLong(matcher.group(2)) : 0;
        final long seconds = matcher.group(3) != null ? Long.parseLong(matcher.group(3)) : 0;
        
        return Duration.ofHours(hours)
                       .plusMinutes(minutes)
                       .plusSeconds(seconds);
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
     * Compare two temporal accessors
     *
     * @param accessor1 first temporal accessor
     * @param accessor2 second temporal accessor
     *
     * @return -1 if accessor1 is before accessor2, 0 if they are equal, 1 if accessor1 is after accessor2
     */
    public static int compareTemporalAccessors(final TemporalAccessor accessor1,
                                               final TemporalAccessor accessor2) {
        if (!accessor1.isSupported(ChronoField.YEAR)) {
            if (!accessor2.isSupported(ChronoField.YEAR)) {
                return 0;
            }
            return -1;
        }
        if (!accessor2.isSupported(ChronoField.YEAR)) {
            return 1;
        }
        if (accessor1.get(ChronoField.YEAR) < accessor2.get(ChronoField.YEAR)) {
            return -1;
        }
        if (accessor1.get(ChronoField.YEAR) > accessor2.get(ChronoField.YEAR)) {
            return 1;
        }
        if (!accessor1.isSupported(ChronoField.MONTH_OF_YEAR)) {
            if (!accessor2.isSupported(ChronoField.MONTH_OF_YEAR)) {
                return 0;
            }
            return -1;
        }
        if (!accessor2.isSupported(ChronoField.MONTH_OF_YEAR)) {
            return 1;
        }
        if (accessor1.get(ChronoField.MONTH_OF_YEAR) < accessor2.get(ChronoField.MONTH_OF_YEAR)) {
            return -1;
        }
        if (accessor1.get(ChronoField.MONTH_OF_YEAR) > accessor2.get(ChronoField.MONTH_OF_YEAR)) {
            return 1;
        }
        if (!accessor1.isSupported(ChronoField.DAY_OF_MONTH)) {
            if (!accessor2.isSupported(ChronoField.DAY_OF_MONTH)) {
                return 0;
            }
            return -1;
        }
        if (!accessor2.isSupported(ChronoField.DAY_OF_MONTH)) {
            return 1;
        }
        if (accessor1.get(ChronoField.DAY_OF_MONTH) < accessor2.get(ChronoField.DAY_OF_MONTH)) {
            return -1;
        }
        if (accessor1.get(ChronoField.DAY_OF_MONTH) > accessor2.get(ChronoField.DAY_OF_MONTH)) {
            return 1;
        }
        return 0;
    }

    /**
     * Get min of two temporal accessors
     *
     * @param accessor1 first temporal accessor
     * @param accessor2 second temporal accessor
     *
     * @return min temporal accessor
     */

    public static Optional<TemporalAccessor> getMinTemporalAccessor(final Optional<TemporalAccessor> accessor1,
                                                                    final Optional<TemporalAccessor> accessor2) {
        if (accessor1.isEmpty()) {
            return accessor2;
        }
        if (accessor2.isEmpty()) {
            return accessor1;
        }
        return compareTemporalAccessors(accessor1.get(), accessor2.get()) < 0 ? accessor1 : accessor2;
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
