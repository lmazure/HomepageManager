
public class ExitHelper {

    private Exception _exception;
    private String _message;
    
    private ExitHelper() {
    }
    
    static public ExitHelper of() {
        return new ExitHelper();
    }
    
    public ExitHelper exception(final Exception e) {
        _exception = e;
        return this;
    }
    
    public ExitHelper message(final String message) {
        _message = message;
        return this;
    }
    
    public void exit() {
        
        if (_message != null) {
            System.err.println(_message); 
        }
        
        if (_exception != null) {
            _exception.printStackTrace();            
        } else {
            final StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (StackTraceElement elem: stack) {
                System.err.println(elem.toString());                
            }
        }
        
        System.exit(1);
    }
}
