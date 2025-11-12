package fr.mazure.homepagemanager.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Tools for managing exceptions
 */
public class ThrowableHelper {

    /**
     * Get a detailed exception info
     *
     * @param throwable exception
     * @return detailed exception info
     */
    public static String getDetailedExceptionInfo(Throwable throwable) {
        final StringBuilder sb = new StringBuilder();

        // Basic exception info
        sb.append("Exception Type: ").append(throwable.getClass().getName()).append("\n");
        sb.append("Message: ").append(throwable.getMessage()).append("\n");

        // Stack trace
        sb.append("\nStack Trace:\n");
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        sb.append(sw.toString());

        // Suppressed exceptions
        final Throwable[] suppressed = throwable.getSuppressed();
        if (suppressed.length > 0) {
            sb.append("\nSuppressed Exceptions:\n");
            for (final Throwable s : suppressed) {
                sb.append(getDetailedExceptionInfo(s)).append("\n");
            }
        }

        // Cause chain
        final Throwable cause = throwable.getCause();
        if (cause != null) {
            sb.append("\nCaused by:\n");
            sb.append(getDetailedExceptionInfo(cause));
        }

        return sb.toString();
    }
}
