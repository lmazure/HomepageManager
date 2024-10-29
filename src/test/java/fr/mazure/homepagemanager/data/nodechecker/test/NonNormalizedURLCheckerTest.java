package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Test of NonNormalizedURLChecker class
 */
class NonNormalizedURLCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void doubleSlashInUrl() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com//</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com//inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "URL \"https://www.example.com//\"contains \"//\"<<ImproperUrl>>",
                 "URL \"https://www.inspiredtester.com//inspired-tester-blog/embracing-change-testing-to-agile\"contains \"//\"<<ImproperUrl>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void doubleSlashInWebArchiveOrgUrlIsIgnored() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Hash Collisions (The Poisoned Message Attack)</T><ST>"The Story of Alice and her Boss"</ST><A>https://web.archive.org/web/20100327141611/http://th.informatik.uni-mannheim.de/people/lucks/HashCollisions/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Magnus</FIRSTNAME><LASTNAME>Daum</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Stefan</FIRSTNAME><LASTNAME>Lucks</LASTNAME></AUTHOR><DATE><YEAR>2005</YEAR><MONTH>6</MONTH><DAY>15</DAY></DATE><COMMENT>The authors have created two PostScript files with the same MD5 checksum.</COMMENT></ARTICLE></ITEM>
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }


    @SuppressWarnings("static-method")
    @Test
    void skashAtEndOfLexFridmanUrlIsDetected() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Elon Musk: Tesla Autopilot</T><A>https://lexfridman.com/elon-musk/</A><L>en</L><F>MP3</F><DURATION><MINUTE>32</MINUTE><SECOND>57</SECOND></DURATION></X><X><T>Elon Musk: Tesla Autopilot | Lex Fridman Podcast #18</T><A>https://www.youtube.com/watch?v=dEv99vxKjVI</A><L>en</L><F>MP4</F><DURATION><MINUTE>32</MINUTE><SECOND>44</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Elon</FIRSTNAME><LASTNAME>Musk</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Lex</FIRSTNAME><LASTNAME>Fridman</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>4</MONTH><DAY>12</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Elon</FIRSTNAME><LASTNAME>Musk</LASTNAME></AUTHOR> is very confident that Autopilot will be mush safer than a human driver.</COMMENT></ARTICLE></ITEM>
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "URL \"https://lexfridman.com/elon-musk/\" should not ends with a slash<<ImproperUrl>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
