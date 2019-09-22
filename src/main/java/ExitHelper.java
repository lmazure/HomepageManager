
public class ExitHelper {

    static public void exit(final String message) {
        exit(message, null);
    }

    static public void exit(final Exception exception) {
        exit(null, exception);
    }

    static public void exit(final String message, final Exception exception) {
        
        if (message != null) {
            System.err.println(message);
        }
        
        if (exception != null) {
            exception.printStackTrace();
        } else {
            final StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (StackTraceElement elem: stack) {
                System.err.println(elem.toString());
            }
        }
        
        System.exit(1);
    }
}
