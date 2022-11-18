package utils;

/**
 * Method for ending the program
 *
 */
public class ExitHelper {

    /**
     * @param message message to display at exit
     */
    public static void exit(final String message) {
        exit(message, null);
    }

    /**
     * @param exception exception to display at exit
     */
    public static void exit(final Exception exception) {
        exit(null, exception);
    }

    /**
     * @param message message to display at exit
     * @param exception exception to display at exit
     */
    public static void exit(final String message,
                            final Exception exception) {

        final Log log = Logger.log(Logger.Level.FATAL);

        if (message != null) {
            log.append(message);
        }

        if (exception != null) {
           log.append(exception);
        } else {
            log.append(Thread.currentThread().getStackTrace());
        }

        log.submit();

        System.exit(1);
    }
}
