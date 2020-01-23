package utils;

@SuppressWarnings("serial")
public class InvalidHttpCodeException extends Exception {
    
    public InvalidHttpCodeException(final String message) {
        super(message);
    }
}