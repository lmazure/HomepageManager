package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Test of EllipsisChecker class
 */
class EllipsisCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void incorrectEllipses() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished...</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "\"Context Driven Testing and Agile are a good match, but this blog is too polished...\" should not contain \"...\"<<ImproperEllipsis>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void doubleDot() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished..</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "\"Context Driven Testing and Agile are a good match, but this blog is too polished..\" contains \"..\"<<DoubleDot>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void wongApostrophe() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but th' blog is too polished.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "' should be’<<ImproperApostrophe>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }


    @SuppressWarnings("static-method")
    @Test
    void wongApostropheInStringsFromOutsidIsIgnored() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing 'to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>A paper "<X><T>Don't Make Your LLM an Evaluation Benchmark Cheater</T><A>https://arxiv.org/abs/2311.01964</A><L>en</L><F>PDF</F></X>" states the obvious: leaking benchmark data in the training data will result in better benchmark scores.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
