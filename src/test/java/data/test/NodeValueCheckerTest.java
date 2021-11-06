package data.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import data.DataController;
import data.FileHandler.Status;
import utils.ExitHelper;
import data.NodeValueChecker;
import data.nodechecker.checker.nodeChecker.NodeCheckError;

public class NodeValueCheckerTest {

    @SuppressWarnings("static-method")
    @Test
    void noError() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "TITLE \"test\" must start with an uppercase");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Foo  bar</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"Foo  bar\" should not contain a double space");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>My articles</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Fuz  baz</T><A>url</A><L>en</L><F>HTML</F></X></ARTICLE></ITEM>\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<DEFINITIONTABLE>\n" +
            "  <ROW>\n" +
            "    <TERM><CODEROUTINE>foo bar</CODEROUTINE></TERM>\n" +
            "  </ROW>\n" +
            "  <DESC>\r\n" +
            "    <BLIST><TITLE>Display</TITLE>\r\n" +
            "      <ITEM>alpha</ITEM>\r\n" +
            "      <ITEM>beta</ITEM>\r\n" +
            "      <ITEM>gamma</ITEM>\r\n" +
            "    </BLIST>\r\n" +
            "  </DESC>\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
                 "      New Untitled File\" should not contain a double space");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>What about analytics.katalon.com?</TITLE></BLIST>\r\n" +
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
    void detectMissingSpace() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>He is Bob.She is Alice</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string e.g. .NET should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string (e.g. Node.js) should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Take care to not report something before a comma or a dot MANIFEST.MF, P.Anno.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string 12.34 should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string \".He is bright\" should be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string .He is bright should be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string (isn't it?) should not be reported.</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"He is Bob.She is Alice\" is missing a space",
                 "\"The string .He is bright should be reported.\" is missing a space",
                 "\"The string .He is bright should be reported.\" has a space before a punctuation",
                 "\"The string \".He is bright\" should be reported.\" is missing a space");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBeforePunctuation() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>He is Bob. She is Alice.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>He is Boby . She is Alicy.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals: sheep, pig, and cock.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals : sheep, pig, and cock.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals: sheep , pig, and cock.</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"He is Boby . She is Alicy.\" has a space before a punctuation",
                 "\"There are three animals : sheep, pig, and cock.\" has a space before a punctuation",
                 "\"There are three animals: sheep , pig, and cock.\" has a space before a punctuation");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>URL</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR> <MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "node DATE (2010 923) shall not be contain space between tags");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>URL</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>URL</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"URL\" (2010-09-23) is after page date (2010-09-22)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>URL</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL\" (2010-09-23) is after page date (2010-09-22)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>" +
            "<X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL1\" (2010-09-27) is after page date (2010-09-26)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL3\" (2010-09-27) is after page date (2010-09-26)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>26</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>27</DAY></DATE></X>" +
            "<X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL1\" (2010-09-27) is after page date (2010-09-26)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE></X>" +
            "<X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL1\" (2010-09-22) is before creation date (2010-09-23)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE>" +
            "<X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>24</DAY></DATE></X>" +
            "<X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X>" +
            "<X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE></X>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL3\" (2010-09-22) is before creation date (2010-09-23)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title2</T><A>URL1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title1</T><A>URL2</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Article \"URL2\" has no date while being after article \"URL1\" which has a date");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title3</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "<ITEM><ARTICLE><X><T>title2</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"URL3\" (2010-09-02) is before creation date (2010-09-03) of previous article \"URL2\"");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL2</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title3</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Article \"URL2\" has no date while being after article \"URL1\" which has a date");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title3</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"URL3\" (2010-09-02) is before creation date (2010-09-03) of previous article \"URL2\"");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><COMMENT>This is comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='URL1'><X><T>title3</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is comment 3.</COMMENT></ARTICLE></ITEM>" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>4</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='URL1'><X><T>title3</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"URL3\" (2010-09-02) is before creation date (2010-09-04) of previous article \"URL1\"");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='URL1'><X><T>title3</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "  <ITEM><ARTICLE><X><T>title1</T><A>URL1</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>1</DAY></DATE><COMMENT>This is a comment 1.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE predecessor='badURL'><X><T>title3</T><A>URL2</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE><COMMENT>This is a comment 2.</COMMENT></ARTICLE></ITEM>" +
            "  <ITEM><ARTICLE><X><T>title2</T><A>URL3</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>3</DAY></DATE><COMMENT>This is a comment 3.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Article has 'predecessor' article equal to \"badURL\" while previous article has URL \"URL1\"");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
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
                 "The list of authors of article \"https://lexfridman.com/grant-sanderson-2/\" (▭ first=Grant ▭ last=Sanderson ▭ ▭) does not contain the expected list for the site (▭ first=Lex ▭ last=Fridman ▭ ▭)",
                 "The list of authors of article \"https://www.numberphile.com/podcast/ben-sparks\" (▭ first=Ben ▭ last=Sparks ▭ ▭) does not contain the expected list for the site (▭ first=Brady ▭ last=Haran ▭ ▭)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "The list of authors of article \"https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile\" (▭ first=Leah ▭ last=Stockley ▭ ▭;▭ first=Grant ▭ last=Sanderson ▭ ▭) is not equal to the expected list for the site (▭ first=Leah ▭ last=Stockley ▭ ▭)");
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
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
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
                 "COMMENT \"Context Driven Testing and Agile are a good match, but this blog is too polished\" must end with a punctuation",
                 "COMMENT \"Context Driven Testing and Agile are a good match. (But this blog is too polished)\" must end with a punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    private static void test(final String content,
                             final String... details) throws SAXException {

        final String expected = Arrays.stream(details)
                                      .sorted()
                                      .collect(Collectors.joining("\n"));

        List<NodeCheckError> effective = null;
        final NodeValueChecker checker = new NodeValueChecker(Paths.get("home"), Paths.get("tmp"), new DummyDataController());
        try {
            final Path tempFile = File.createTempFile("NodeValueCheckerTest", ".xml").toPath();
            Files.writeString(tempFile, content);
            effective = checker.check(tempFile);
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        Assertions.assertEquals(expected, normalize(effective));
    }

    private static String normalize(final List<NodeCheckError> errors) {
        return errors.stream()
                     .map(e -> e.getDetail())
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }

    private static class DummyDataController implements DataController {

        @Override
        public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do nothing
        }

        @Override
        public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do nothing
        }
    }
}
