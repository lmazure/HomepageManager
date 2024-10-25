package fr.mazure.homepagemanager.utils.internet;

/**
 *
 */
@SuppressWarnings("serial")
public class InvalidHttpCodeException extends Exception {

    /**
     * Constructor
     *
     * @param message Message of the exception
     */
    public InvalidHttpCodeException(final String message) {
        super(message);
    }
}