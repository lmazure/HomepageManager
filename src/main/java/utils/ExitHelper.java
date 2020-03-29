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
            final StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (StackTraceElement elem: stack) {
                log.append("\n" + elem.toString());
            }
        }
        
        log.submit();
        
        System.exit(1);
    }
}
