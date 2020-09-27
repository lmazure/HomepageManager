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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.xmlparsing.ElementType;

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
            Logger.log(Logger.Level.ERROR)
                  .append("Failed to configure the XML parser")
                  .append(pce)
                  .submit();
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

    /**
     * Return the child elements having a given node type
     * @param element
     * @param type
     * @return
     */
    public static List<Element> getChildrenByNodeType(final Element element,
                                                      final ElementType type) {
        final NodeList list = element.getChildNodes();
        final List<Element> children = new ArrayList<Element>(); 
        for (int i = 0; i < list.getLength(); i++) {
            final Node child = list.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                final Element e = (Element)child;
                if (isOfType(e, type)) {
                    children.add(e);
                }
            }
        }
        return children;
    }

    /**
     * Return the descendant elements having a given node type
     * @param element
     * @param type
     * @return
     */
    public static NodeList getDescendantsByNodeType(final Element element,
                                                    final ElementType type) {
        return element.getElementsByTagName(type.toString());
    }

    /**
     * Test if an element is of given type
     * @param element
     * @param type
     * @return
     */
    public static boolean isOfType(final Element element,
                                   final ElementType type) {
        return element.getTagName().equals(type.toString());
    }

    /**
     * Test if a node is an element of given type
     * @param node
     * @param type
     * @return
     */
    public static boolean isOfType(final Node node,
                                   final ElementType type) {
        return node.getNodeName().equals(type.toString());
    }
}
