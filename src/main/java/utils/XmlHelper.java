package utils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

public class XmlHelper {

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
     * return the type of an element Node
     * @param element
     * @return
     * @throws UnsupportedOperationException if not an element node
     */
    public static ElementType getElementType(final Element element) {
        if (element.getNodeType() != Node.ELEMENT_NODE) {
            throw new UnsupportedOperationException("XML node is not an element");
        }

        return ElementType.valueOf(element.getTagName());
    }

    /**
     * return the previous sibling element, otherwise return null
     * @param element
     * @return
     */
    public static Element getPreviousSiblingElement(final Element element) {
        return getSiblingElement(element, Node::getPreviousSibling);
    }

    /**
     * return the next sibling element, otherwise return null
     * @param element
     * @return
     */
    public static Element getNextSiblingElement(final Element element) {
        return getSiblingElement(element, Node::getNextSibling);
    }

    public static Element getSiblingElement(final Element element,
                                            final Function<Node, Node> siblingFunction) {
        Node sibling = element;
        while ((sibling = siblingFunction.apply(sibling)) != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                return (Element)sibling;
            }
        }
        return null;
    }

    /**
     * Return the child elements having a given node type
     * @param element
     * @param type
     * @return
     */
    public static List<Element> getChildrenByElementType(final Element element,
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
    public static NodeList getDescendantsByElementType(final Element element,
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
