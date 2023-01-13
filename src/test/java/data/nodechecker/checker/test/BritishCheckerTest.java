package data.nodechecker.checker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of BritishChecker class
 *
 */
public class BritishCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void detectAmericanSpelling() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"fr\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST>\r\n" +
            "<ITEM><ARTICLE><X><T>When are two strings equal?</T><A>http://java.sun.com</A><L>en</L><F>HTML</F></X><COMMENT>A quick introduction to the common string pool and the literal strings consolidation in a same package.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE><X><T>When are two strings equal?</T><A>http://java.sun.com</A><L>en</L><F>HTML</F></X><COMMENT>M102 was improperly recorded in Messier catalog, it is a galaxy seen almost exactly edge-on.</COMMENT></ARTICLE></ITEM>\r\\n" +
            "<ITEM><ARTICLE><X><T>When are two strings equal?</T><A>http://java.sun.com</A><L>en</L><F>HTML</F></X><COMMENT>Evaluating the magnetic dipole moment of neutrinos with the color–magnitude diagram of M5.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE><X><T>When are two strings equal?</T><A>http://java.sun.com</A><L>en</L><F>HTML</F></X><COMMENT>Some samples stored in the Oak Ridge National Laboratory.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE><X><T>When are two strings equal?</T><A>http://java.sun.com</A><L>en</L><F>HTML</F></X><COMMENT>The production of Californium at the Oak Ridge National Laboratory and an experiment with it.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "COMMENT \"Evaluating the magnetic dipole moment of neutrinos with the color–magnitude diagram of M5.\" contains american word \"color\", it should be \"colour\"\n" +
                 "COMMENT \"M102 was improperly recorded in Messier catalog, it is a galaxy seen almost exactly edge-on.\" contains american word \"catalog\", it should be \"catalogue\"");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
