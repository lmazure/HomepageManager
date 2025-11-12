package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of some NodeChecker subclasses
 * (this should be dispatched in test class per NodeChecker subclass)
 */
class NodeValueCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void noError() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectLowercaseTitle() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "TITLE \"test\" must start with an uppercase<<LowercaseTitle>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    @Disabled
    void detectTitleEndingWithColon() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test:</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "TITLE \"Test:\" must not finish with colon");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreMissingSpaceDueToCode() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Explanation of the <CODEROUTINE>class.method</CODEROUTINE> method</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreMissingSpaceDueToSlash() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>XFree86/X.org</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreMissingSpaceDueToUrl() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>What about analytics.katalon.com and NaturalNews.com?</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBetweenTags() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR> <MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "node DATE (2010 923) shall not contain space between tags<<SpaceBetweenTags>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void supportArticleWithPublicationDateButNoCreationDate() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticlWithCreationDateMoreRecentThanPage() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page\" (2010-09-23) is after page date (2010-09-22)<<ArticleCreationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationDateMoreRecentThanPage() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page\" (2010-09-23) is after page date (2010-09-22)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink1MoreRecentThanPage() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE>\
            <X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>\
            <X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>\
            <X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page1\" (2010-09-27) is after page date (2010-09-26)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink2MoreRecentThanPage() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE>\
            <X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>\
            <X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>\
            <X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page3\" (2010-09-27) is after page date (2010-09-26)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink1ButNoCreationDateMoreRecentThanPage() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE>\
            <X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>\
            <X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>\
            <X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>\
            <COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page1\" (2010-09-27) is after page date (2010-09-26)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink1BeforeCreationDate() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE>\
            <X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE></X>\
            <X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>\
            <X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page1\" (2010-09-22) is before creation date (2010-09-23)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectArticleWithPublicationLink3BeforeCreationDate() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE>\
            <X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>\
            <X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X>\
            <X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE></X>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Publication date of article \"https://example.com/page3\" (2010-09-22) is before creation date (2010-09-23)<<ArticlePublicationDateAfterPageCreationDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsNoIndentationCorrectDatesAreNotReported() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
            <ITEM><ARTICLE><X><T>title1</T><A>http://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>title2</T><A>http://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>title3</T><A>http://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsNoIndentationDetectArticleWithNoDateAfterArticleWithDate() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
            <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Article \"https://example.com/page2\" has no date while being after article \"https://example.com/page1\" which has a date<<ArticleWithNoDateBeforeArticleWihDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsNoIndentationDetectArticleAfterArticleWithDateMoreRecent() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
            <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page3\" (2010-09-02) is before creation date (2010-09-03) of previous article \"https://example.com/page2\"<<ArticleBeforeIsOlder>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationCorrectDatesAreNotReported() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationDetectArticleWithNoDateAfterArticleWithDate() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Article \"https://example.com/page2\" has no date while being after article \"https://example.com/page1\" which has a date<<ArticleWithNoDateBeforeArticleWihDate>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationDetectArticleAfterArticleWithDateMoreRecent() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page3\" (2010-09-02) is before creation date (2010-09-03) of previous article \"https://example.com/page2\"<<ArticleBeforeIsOlder>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationAndChainIgnoreArticleAfterArticleWithDateMoreRecent() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><COMMENT>This is comment 1.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE predecessor='https://example.com/page1'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is comment 2.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is comment 3.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void whenThereIsIndentationAndChainDetectArticleAfterArticleChainWithDateMoreRecent() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>4</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE predecessor='https://example.com/page1'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Creation date of article \"https://example.com/page3\" (2010-09-02) is before creation date (2010-09-04) of previous article \"https://example.com/page1\"<<ArticleBeforeIsOlder>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreCorrectPredAttribute() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE predecessor='https://example.com/page1'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void reportIncorrectPredecessorAttribute() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <BLIST>\
              <ITEM><ARTICLE><X><T>title1</T><A>https://example.com/page1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE predecessor='https://example.com/badpage'><X><T>title3</T><A>https://example.com/page2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>\
              <ITEM><ARTICLE><X><T>title2</T><A>https://example.com/page3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>\
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Article has 'predecessor' article equal to \"https://example.com/badpage\" while previous article has URL \"https://example.com/page1\"<<IncorrectPredecessorArticle>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

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
            </BLIST>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "COMMENT \"Context Driven Testing and Agile are a good match, but this blog is too polished\" must end with a punctuation<<MissingPuctuation>>",
                 "COMMENT \"Context Driven Testing and Agile are a good match. (But this blog is too polished)\" must end with a punctuation<<MissingPuctuation>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }
}
