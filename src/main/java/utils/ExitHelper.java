package utils;

public class ExitHelper {

    static public void exit(final String message) {
        exit(message, null);
    }

    static public void exit(final Exception exception) {
        exit(null, exception);
    }

    static public void exit(final String message, final Exception exception) {

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
