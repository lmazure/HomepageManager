package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of DurationChecker class
 *
 */public class DurationCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void leadingZeroIsDetected() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>15</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>The Happy Twin - with Ben Sparks</T><A>https://www.numberphile.com/podcast/ben-sparks</A><L>en</L><F>MP3</F><DURATION><HOUR>1</HOUR><MINUTE>02</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Brady</FIRSTNAME><LASTNAME>Haran</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>5</MONTH><DAY>27</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR> describes his life.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>#118 – Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown</T><A>https://lexfridman.com/grant-sanderson-2/</A><L>en</L><F>MP3</F><DURATION><HOUR>02</HOUR><MINUTE>8</MINUTE><SECOND>52</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Lex</FIRSTNAME><LASTNAME>Fridman</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>23</DAY></DATE><COMMENT>A long interview.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #35 - Maud CHALMEL &amp; Thibaut de la Touane</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>06</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Guillaume</FIRSTNAME><LASTNAME>Chifoumi</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>François</FIRSTNAME><LASTNAME>Décamp</LASTNAME></AUTHOR><AUTHOR><GIVENNAME>Tarsa</GIVENNAME></AUTHOR><AUTHOR><GIVENNAME>Muss Ino</GIVENNAME></AUTHOR><AUTHOR><FIRSTNAME>Pénélope</FIRSTNAME></AUTHOR><AUTHOR><FIRSTNAME>Maud</FIRSTNAME><LASTNAME>Chalmel</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Thibaut</FIRSTNAME><LASTNAME>de la Touane</LASTNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Heat, Clockworker, Super Mega Lucky Box, Turing Machine, the games of Triton Noir, a visit of Volumique workshop, and a interview of <AUTHOR><FIRSTNAME>Maud</FIRSTNAME><LASTNAME>Chalmel</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "HOUR starts with 0 +(\"02\")<<IncorrectDuration>>",
                 "MINUTE starts with 0 +(\"02\")<<IncorrectDuration>>",
                 "SECOND starts with 0 +(\"06\")<<IncorrectDuration>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
