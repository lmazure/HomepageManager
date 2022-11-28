package utils;

import java.time.Instant;

/**
 *
 */
public class Logger {

    public enum Level {
        FATAL,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        TRACE
    }

    /**
     * @param level
     * @return
     */
    public static Log log(final Level level) {
        final Thread thread = Thread.currentThread();
        final Instant instant = Instant.now();
        return new Log(level, thread, instant);
    }
}
