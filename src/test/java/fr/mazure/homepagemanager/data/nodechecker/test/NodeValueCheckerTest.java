package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of some NodeChecker subclasses
 * (this should be dispatched in test class per NodeChecker subclass)
 */
public class NodeValueCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void noError() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectLowercaseTitle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "TITLE \"test\" must start with an uppercase<<LowercaseTitle>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    @Disabled
    void detectTitleEndingWithColon() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test:</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "TITLE \"Test:\" must not finish with colon");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectDoubleSpace() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Foo  bar</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"Foo  bar\" should not contain a double space<<DoubleSpace>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceInArticleTitles() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>My articles</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Fuz  baz</T><A>https://example.com/page</A><L>en</L><F>HTML</F></X></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceDueToNodes() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Foo <KEY id='Down'/> bar</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceDueToIndentationBetweenNodes() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<DEFINITIONTABLE>\n" +
            "  <ROW>\n" +
            "    <TERM><CODEROUTINE>foo bar</CODEROUTINE></TERM>\n" +
            "  </ROW>\n" +
            "  <DESC><BLIST><TITLE>Display</TITLE>\r\n" +
            "      <ITEM>alpha</ITEM>\r\n" +
            "      <ITEM>beta</ITEM>\r\n" +
            "      <ITEM>gamma</ITEM>\r\n" +
            "    </BLIST></DESC>\r\n" +
            "</DEFINITIONTABLE>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceDueToIndentationInsideNode() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<DEFINITIONTABLE>\r\n" +
            "  <ROW>\r\n" +
            "    <TERM><MODIFIERKEY id='Ctrl'/><KEY id='N'/><BR/>\r\n" +
            "      double left click on tab menubar<BR/>\r\n" +
            "      <CODEROUTINE>New Untitled File</CODEROUTINE></TERM>\r\n" +
            "    <DESC>open new empty editor</DESC>\r\n" +
            "  </ROW>\r\n" +
            "</DEFINITIONTABLE>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectDoubleSpaceInIndentation() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<DEFINITIONTABLE>\r\n" +
            "  <ROW>\r\n" +
            "    <TERM><MODIFIERKEY id='Ctrl'/><KEY id='N'/><BR/>\r\n" +
            "      double left  click on tab menubar<BR/>\r\n" +
            "      <CODEROUTINE>New Untitled File</CODEROUTINE></TERM>\r\n" +
            "    <DESC>open new empty editor</DESC>\r\n" +
            "  </ROW>\r\n" +
            "</DEFINITIONTABLE>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"\n" +
                 "      double left  click on tab menubar\n" +
                 "      New Untitled File\" should not contain a double space<<DoubleSpace>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreMissingSpaceDueToCode() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Explanation of the <CODEROUTINE>class.method</CODEROUTINE> method</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreMissingSpaceDueToSlash() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>XFree86/X.org</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreMissingSpaceDueToUrl() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>What about analytics.katalon.com and NaturalNews.com?</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBetweenTags() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR> <MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "node DATE (2010 923) shall not contain space between tags<<SpaceBetweenTags>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void supportArticleWithPublicationDateButNoCreationDate() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticlWithCreationDateMoreRecentThanPage() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page\" (2010-09-23) is after page date (2010-09-22)<<ArticleCreationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationDateMoreRecentThanPage() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page\" (2010-09-23) is after page date (2010-09-22)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink1MoreRecentThanPage() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>" +
            "<X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page1\" (2010-09-27) is after page date (2010-09-26)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink2MoreRecentThanPage() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page3\" (2010-09-27) is after page date (2010-09-26)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink1ButNoCreationDateMoreRecentThanPage() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>" +
            "<X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page1\" (2010-09-27) is after page date (2010-09-26)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink1BeforeCreationDate() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE></X>" +
            "<X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page1\" (2010-09-22) is before creation date (2010-09-23)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink3BeforeCreationDate() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page3\" (2010-09-22) is before creation date (2010-09-23)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsNoIndentationCorrectDatesAreNotReported() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title1</T><A>http://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title2</T><A>http://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title3</T><A>http://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsNoIndentationDetectArticleWithNoDateAfterArticleWithDate() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Article \"https://example.com/page2\" has no date while being after article \"https://example.com/page1\" which has a date<<ArticleWithNoDateBeforeArticleWihDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsNoIndentationDetectArticleAfterArticleWithDateMoreRecent() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page3\" (2010-09-02) is before creation date (2010-09-03) of previous article \"https://example.com/page2\"<<ArticleBeforeIsOlder>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationCorrectDatesAreNotReported() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationDetectArticleWithNoDateAfterArticleWithDate() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Article \"https://example.com/page2\" has no date while being after article \"https://example.com/page1\" which has a date<<ArticleWithNoDateBeforeArticleWihDate>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationDetectArticleAfterArticleWithDateMoreRecent() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page3\" (2010-09-02) is before creation date (2010-09-03) of previous article \"https://example.com/page2\"<<ArticleBeforeIsOlder>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationAndChainIgnoreArticleAfterArticleWithDateMoreRecent() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='https://example.com/page1'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is comment 3.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationAndChainDetectArticleAfterArticleChainWithDateMoreRecent() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>4</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='https://example.com/page1'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page3\" (2010-09-02) is before creation date (2010-09-04) of previous article \"https://example.com/page1\"<<ArticleBeforeIsOlder>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreCorrectPredAttribute() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='https://example.com/page1'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void reportIncorrectPredAttribute() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='https://example.com/badpage'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Article has 'predecessor' article equal to \"https://example.com/badpage\" while previous article has URL \"https://example.com/page1\"<<IncorrectPredecessorArticle>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void correctWellKnownAuthorsAreIgnored() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>The Happy Twin - with Ben Sparks</T><A>https://www.numberphile.com/podcast/ben-sparks</A><L>en</L><F>MP3</F><DURATION><HOUR>1</HOUR><MINUTE>2</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Brady</FIRSTNAME><LASTNAME>Haran</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>5</MONTH><DAY>27</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR> describes his life.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>#118 – Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown</T><A>https://lexfridman.com/grant-sanderson-2/</A><L>en</L><F>MP3</F><DURATION><HOUR>2</HOUR><MINUTE>8</MINUTE><SECOND>52</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Lex</FIRSTNAME><LASTNAME>Fridman</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>23</DAY></DATE><COMMENT>A long interview.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void wellKnownAuthorIsMissing() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>The Happy Twin - with Ben Sparks</T><A>https://www.numberphile.com/podcast/ben-sparks</A><L>en</L><F>MP3</F><DURATION><HOUR>1</HOUR><MINUTE>2</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>5</MONTH><DAY>27</DAY></DATE><COMMENT>Ben Sparks describes his life.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>#118 – Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown</T><A>https://lexfridman.com/grant-sanderson-2/</A><L>en</L><F>MP3</F><DURATION><HOUR>2</HOUR><MINUTE>8</MINUTE><SECOND>52</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>23</DAY></DATE><COMMENT>A long interview.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "The list of authors of article \"https://lexfridman.com/grant-sanderson-2/\" (▭ first=Grant ▭ last=Sanderson ▭ ▭) does not contain the expected list for the site (▭ first=Lex ▭ last=Fridman ▭ ▭)<<IncorrectAuthorList>>",
                 "The list of authors of article \"https://www.numberphile.com/podcast/ben-sparks\" (▭ first=Ben ▭ last=Sparks ▭ ▭) does not contain the expected list for the site (▭ first=Brady ▭ last=Haran ▭ ▭)<<IncorrectAuthorList>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void wellKnownAuthorIsNotAlone() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "The list of authors of article \"https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile\" (▭ first=Leah ▭ last=Stockley ▭ ▭;▭ first=Grant ▭ last=Sanderson ▭ ▭) is not equal to the expected list for the site (▭ first=Leah ▭ last=Stockley ▭ ▭)<<IncorrectAuthorList>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void duplicatedAuthor() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>Tric Trac Show #35 - Maud CHALMEL &amp; Thibaut de la Touane</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Guillaume</FIRSTNAME><LASTNAME>Chifoumi</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>François</FIRSTNAME><LASTNAME>Décamp</LASTNAME></AUTHOR><AUTHOR><GIVENNAME>Tarsa</GIVENNAME></AUTHOR><AUTHOR><GIVENNAME>Muss Ino</GIVENNAME></AUTHOR><AUTHOR><FIRSTNAME>Pénélope</FIRSTNAME></AUTHOR><AUTHOR><GIVENNAME>Tarsa</GIVENNAME></AUTHOR><AUTHOR><FIRSTNAME>Maud</FIRSTNAME><LASTNAME>Chalmel</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Thibaut</FIRSTNAME><LASTNAME>de la Touane</LASTNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Heat, Clockworker, Super Mega Lucky Box, Turing Machine, the games of Triton Noir, a visit of Volumique workshop, and a interview of <AUTHOR><FIRSTNAME>Maud</FIRSTNAME><LASTNAME>Chalmel</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "The list of authors of article \"https://www.youtube.com/watch?v=aW61yxnQvio\" contains duplicated author: ▭ ▭ ▭ ▭ ▭ given=Tarsa<<DuplicatedAuthor>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void missedPunctuationAtCommentEnd() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. (But this blog is too polished.)</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match. (But this blog is too polished)</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "COMMENT \"Context Driven Testing and Agile are a good match, but this blog is too polished\" must end with a punctuation<<MissingPuctuation>>",
                 "COMMENT \"Context Driven Testing and Agile are a good match. (But this blog is too polished)\" must end with a punctuation<<MissingPuctuation>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void doubleSlashInUrl() {

        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.example.com//</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com//inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
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
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>Hash Collisions (The Poisoned Message Attack)</T><ST>\"The Story of Alice and her Boss\"</ST><A>https://web.archive.org/web/20100327141611/http://th.informatik.uni-mannheim.de/people/lucks/HashCollisions/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Magnus</FIRSTNAME><LASTNAME>Daum</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Stefan</FIRSTNAME><LASTNAME>Lucks</LASTNAME></AUTHOR><DATE><YEAR>2005</YEAR><MONTH>6</MONTH><DAY>15</DAY></DATE><COMMENT>The authors have created two PostScript files with the same MD5 checksum.</COMMENT></ARTICLE></ITEM>\n" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
