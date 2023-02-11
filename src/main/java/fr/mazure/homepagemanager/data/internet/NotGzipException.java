package fr.mazure.homepagemanager.data.internet;

/**
 * Exception used when trying to read a GZIP payload, but it it not gzipped
 */
@SuppressWarnings("serial")
public class NotGzipException extends Exception {

    /**
     * @param message error message
     */
    public NotGzipException(final String message) {
        super(message);
    }

    /**
     * @param message erro message
     * @param cause original exception
     */
    public NotGzipException(final String message,
                            final Throwable cause) {
        super(message, cause);
    }
}