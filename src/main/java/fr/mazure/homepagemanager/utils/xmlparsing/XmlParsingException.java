package fr.mazure.homepagemanager.utils.xmlparsing;

/**
* Exception indicating that an XML file is incorrect
*/
@SuppressWarnings("serial")
public class XmlParsingException extends Exception {

    /**
     * Constructor
     *
     * @param message Error message
     */
    public XmlParsingException(final String message) {
        super(message);
    }
}