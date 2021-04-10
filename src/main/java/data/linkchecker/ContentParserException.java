package data.linkchecker;


@SuppressWarnings("serial")
public class ContentParserException extends Exception {

    public ContentParserException(final String message) {
        super(message);
    }

    public ContentParserException(final String message,
                                  final Throwable cause) {
        super(message, cause);
    }

}