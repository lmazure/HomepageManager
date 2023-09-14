package fr.mazure.homepagemanager.data.linkchecker;

/**
 * Exception used when trying to parse a payload, but this one is not structured as expected
 */
@SuppressWarnings("serial")
public class ContentParserException extends Exception {

    /**
     * @param message error message
     */    public ContentParserException(final String message) {
        super(message);
    }

     /**
      * @param message erro message
      * @param cause original exception
      */
    public ContentParserException(final String message,
                                  final Throwable cause) {
        super(message, cause);
    }
}