package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of the CommentChecker class
 */
class CommentCheckerTest extends NodeValueCheckerTestBase {
    @SuppressWarnings("static-method")
    @Test
    void missingPunctuationAtCommentEnd() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. (But this blog is too polished.)</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. (But this blog is too polished)</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. [But this blog is too polished.]</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. [But this blog is too polished]</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. "But this blog is too polished."</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. "But this blog is too polished"</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "COMMENT \"Context Driven Testing and Agile are a good match, but this blog is too polished\" must end with a punctuation mark<<MissingPuctuation>>",
                 "COMMENT \"Context Driven Testing and Agile are a good match. (But this blog is too polished)\" must end with a punctuation mark<<MissingPuctuation>>",
                 "COMMENT \"Context Driven Testing and Agile are a good match. [But this blog is too polished]\" must end with a punctuation mark<<MissingPuctuation>>",
                 "COMMENT \"Context Driven Testing and Agile are a good match. \"But this blog is too polished\"\" must end with a punctuation mark<<MissingPuctuation>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }
}
