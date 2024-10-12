package fr.mazure.homepagemanager.utils;

import java.time.Instant;

/**
 * Logger
 */
public class Logger {

    /**
     * Level of the log
     */
    public enum Level {
        /**
         *
         */
        FATAL,
        /**
         *
         */
        ERROR,
        /**
         *
         */
        WARN,
        /**
         *
         */
        INFO,
        /**
         *
         */
        DEBUG,
        /**
         *
         */
        TRACE
    }

    /**
     * Create a new log
     *
     * @param level Level of the log
     * @return The created log
     */
    public static Log log(final Level level) {
        final Thread thread = Thread.currentThread();
        final Instant instant = Instant.now();
        return new Log(level, thread, instant);
    }
}
