package data.nodechecker.checker.nodeChecker;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;


/**
 * @author Laurent
 *
 */
public class XMLSchemaValidationChecker extends NodeChecker {

	final static InclusionTagSelector s_selector = new InclusionTagSelector( new String[] {
			NodeChecker.PAGE
			} );

	final File a_fileToBeChecked;
	
	/**
	 * @param file
	 */
	public XMLSchemaValidationChecker(final File file) {
		
		super();
		
		a_fileToBeChecked = file;
	}
	
	/**
	 * @see lmzr.homepagechecker.checker.nodeChecker.NodeChecker#getTagSelector()
	 */
	@Override
	public TagSelector getTagSelector() {
		return s_selector;
	}

	/**
	 * @see lmzr.homepagechecker.checker.nodeChecker.NodeChecker#getRules()
	 */
	@Override
	public NodeRule[] getRules() {
		final NodeRule a[]= new NodeRule[1];
		a[0] = new NodeRule() { @Override
		public CheckStatus checkElement(final Element e) { return validate();}
		                    @Override
							public String getDescription() { return "the XML Schema is not respected"; } };

        return a;
	}

	private CheckStatus validate() {
		
	    final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final File schemaLocation = new File(a_fileToBeChecked.getParentFile().getParentFile()
        		                             + File.separator
        		                             + "css"
        		                             + File.separator
        		                             + "schema.xsd");
        Schema schema = null;
        
        try {
			schema = factory.newSchema(schemaLocation);
		} catch (final SAXException e1) {
			System.err.println("failed to create the schema" + e1.getMessage());
			e1.printStackTrace();
			return null;
		}

		final Validator validator = schema.newValidator();

        final Source source = new StreamSource(a_fileToBeChecked);
        
        try {
			validator.validate(source);
        } catch (final SAXException e1) {
			return new CheckStatus("not valid: " + e1. getMessage() +"cause: " + e1. getMessage());
		} catch (final IOException e1) {
			return new CheckStatus("cannot read:  " + e1.getMessage());
		}

		return null;
	}
}
