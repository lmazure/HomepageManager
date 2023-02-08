package utils.internet;

/**
 *
 */
@SuppressWarnings("serial")
public class InvalidHttpCodeException extends Exception {

    /**
     * @param message
     */
    public InvalidHttpCodeException(final String message) {
        super(message);
    }
}