package utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLHelper {

    public static List<String> getFirstLevelTextContent(final Node node) {
        final NodeList list = node.getChildNodes();
        final List<String> content = new ArrayList<String>();
        for (int i = 0; i < list.getLength(); i++) {
            final Node child = list.item(i);
            if (child.getNodeType() == Node.TEXT_NODE)
                content.add(child.getTextContent());
        }
        return content;
    }

    public static DocumentBuilder buildDocumentBuilder() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException pce){
            System.out.println("Failed to configure the XML parser");
            pce.printStackTrace();
        }
        return builder;
    }

    public static Validator buildValidator(final Path schemaLocation) {
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = null;
        try {
            schema = factory.newSchema(schemaLocation.toFile());
        } catch (final SAXException e) {
            ExitHelper.exit(e);
        }
        assert(schema != null);
        return schema.newValidator();
    }
}
