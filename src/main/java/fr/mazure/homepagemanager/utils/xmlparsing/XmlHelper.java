package fr.mazure.homepagemanager.utils.xmlparsing;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;

/**
 * Tools to manage XML
 */
public class XmlHelper {

    /**
     * Transform a string into a XML strinf
     * @param str string
     * @return XML string
     */
    public static String transform(final String str) {
        return str.replace("&", "&amp;")
                  .replace("<","&lt;")
                  .replace(">","&gt;");
    }

    /**
     * build a DocumentBuilder
     *
     * @return the DocumentBuilder
     */
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

    /**
     * build a Validator
     *
     * @param schemaLocation path of the schema
     * @return the Validator
     */
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
     * Extract the text which is at the first level (i.e. not in child nodes)
     *
     * @param node node
     * @return text which isthe first level
     */
    public static List<String> getFirstLevelTextContent(final Node node) {
        final NodeList list = node.getChildNodes();
        final List<String> content = new ArrayList<>();
        for (int i = 0; i < list.getLength(); i++) {
            final Node child = list.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                content.add(child.getTextContent());
            }
        }
        return content;
    }

    /**
     * return the type of an element Node
     *
     * @param element element
     * @return type
     * @throws UnsupportedOperationException if not an element node
     */
    public static ElementType getElementType(final Element element) {
        if (element.getNodeType() != Node.ELEMENT_NODE) {
            throw new UnsupportedOperationException("XML node is not an element");
        }

        return ElementType.valueOf(element.getTagName());
    }

    /**
     * Return the value of the xml:lang attribute of an element
     *
     * @param element element
     * @return value of xml:lang attribute, or empty if there is none
     */
    public static Optional<Locale> getElementLanguage(final Element element) {

        if (element.hasAttribute("xml:lang")) {
            final String lang = element.getAttribute("xml:lang");
            return Optional.of(Locale.forLanguageTag(lang));
        }

        final Node parent = element.getParentNode();
        if (parent instanceof Document) {
            return Optional.empty();
        }

        return getElementLanguage((Element)parent);
    }
    /**
     * return the previous sibling element, otherwise return null
     *
     * @param element element
     * @return previous sibling element
     */
    public static Element getPreviousSiblingElement(final Element element) {
        return getSiblingElement(element, Node::getPreviousSibling);
    }

    /**
     * return the next sibling element, otherwise return null
     *
     * @param element element
     * @return next sibling element
     */
    public static Element getNextSiblingElement(final Element element) {
        return getSiblingElement(element, Node::getNextSibling);
    }

    private static Element getSiblingElement(final Element element,
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
     *
     * @param element starting element
     * @param type desired child element type
     * @return child elements having this type
     */
    public static List<Element> getChildrenByElementType(final Element element,
                                                         final ElementType type) {
        final NodeList list = element.getChildNodes();
        final List<Element> children = new ArrayList<>();
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
     *
     * @param element starting element
     * @param type desired descendant element type
     * @return descendant elements having this type
     */
    public static NodeList getDescendantsByElementType(final Element element,
                                                       final ElementType type) {
        return element.getElementsByTagName(type.toString());
    }

    /**
     * Test if an element is of given type
     *
     * @param element element to be tested
     * @param type type
     * @return true if the element if of type type
     */
    public static boolean isOfType(final Element element,
                                   final ElementType type) {
        return element.getTagName().equals(type.toString());
    }

    /**
     * Test if a node is an element of given type
     *
     * @param node node to be tested
     * @param type type
     * @return true if the element if of type type
     */
    public static boolean isOfType(final Node node,
                                   final ElementType type) {
        return node.getNodeName().equals(type.toString());
    }
}
