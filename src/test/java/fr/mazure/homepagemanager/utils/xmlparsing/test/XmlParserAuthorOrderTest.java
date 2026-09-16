package fr.mazure.homepagemanager.utils.xmlparsing.test;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParser;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 * Tests of {@link XmlParser#parseAuthorElement} focusing on the {@code order} attribute.
 */
class XmlParserAuthorOrderTest {

    private static AuthorData parse(final String authorXml) throws XmlParsingException {
        final String xml = "<PAGE>" + authorXml + "</PAGE>";
        try {
            final DocumentBuilder builder = XmlHelper.buildDocumentBuilder();
            final Document document = builder.parse(new ByteArrayInputStream(xml.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
            final NodeList list = XmlHelper.getDescendantsByElementType(document.getDocumentElement(), ElementType.AUTHOR);
            Assertions.assertEquals(1, list.getLength());
            return XmlParser.parseAuthorElement((Element) list.item(0));
        } catch (final XmlParsingException e) {
            throw e;
        } catch (final Exception e) {
            throw new AssertionError("Failed to parse XML: " + e.getMessage(), e);
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void absentAttributeDefaultsToWestern() throws XmlParsingException {
        final AuthorData data = parse("<AUTHOR><FIRSTNAME>Martin</FIRSTNAME><LASTNAME>Fowler</LASTNAME></AUTHOR>");
        Assertions.assertEquals(AuthorData.NameOrder.WESTERN, data.getOrder());
    }

    @SuppressWarnings("static-method")
    @Test
    void explicitWesternAttribute() throws XmlParsingException {
        final AuthorData data = parse("<AUTHOR order=\"western\"><FIRSTNAME>Martin</FIRSTNAME><LASTNAME>Fowler</LASTNAME></AUTHOR>");
        Assertions.assertEquals(AuthorData.NameOrder.WESTERN, data.getOrder());
    }

    @SuppressWarnings("static-method")
    @Test
    void explicitEasternAttribute() throws XmlParsingException {
        final AuthorData data = parse("<AUTHOR order=\"eastern\"><FIRSTNAME>An</FIRSTNAME><LASTNAME>Nguyen</LASTNAME></AUTHOR>");
        Assertions.assertEquals(AuthorData.NameOrder.EASTERN, data.getOrder());
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalOrderValueThrows() {
        Assertions.assertThrows(XmlParsingException.class, () -> parse("<AUTHOR order=\"northern\"><FIRSTNAME>An</FIRSTNAME><LASTNAME>Nguyen</LASTNAME></AUTHOR>"));
    }
}
