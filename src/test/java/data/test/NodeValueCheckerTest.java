package data.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import data.DataController;
import data.FileHandler.Status;
import utils.ExitHelper;
import data.NodeValueChecker;

class NodeValueCheckerTest {

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
            "<BLIST><TITLE>My articles</TITLE></BLIST>\r\n" +
            "<ITEM><ARTICLE><X><T>Fuz  baz</T><A>url</A><L>en</L><F>HTML</F></X></ARTICLE></ITEM>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

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
            "<BLIST><TITLE>Foo <KEY ID='Down'/> bar</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }


    @Test
    void ignoreDoubleSpaceDueToIndentation() {

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
                 "\"The string \".He is bright\" should be reported.\" is missing a space");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

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

    @Test
    void detectArticleMoreRecentThanPage() {
        
        final String content =
            "<?xml version=\"1.0\"?>" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">" +
            "<TITLE>TypeScript</TITLE>" +
            "<PATH>links/typescript.xml</PATH>" +
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>" +
            "<CONTENT>" +
            "<ITEM><ARTICLE><X><T>title</T><A>URL</A><L>en</L><F>HTML</F></X><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>comment</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Creation date of article \"URL\" (2010-09-23) is after page date (2010-09-22)");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

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
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>comment</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL1\" (2010-09-27) is after page date (2010-09-26)");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

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
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>comment</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL3\" (2010-09-27) is after page date (2010-09-26)");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

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
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>comment</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL1\" (2010-09-22) is before creation date (2010-09-23)");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

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
            "<DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE><COMMENT>comment</COMMENT></ARTICLE></ITEM>" +
            "</CONTENT>" +
            "</PAGE>";
        try {
            test(content,
                 "Publication date of article \"URL3\" (2010-09-22) is before creation date (2010-09-23)");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    static private void test(final String content) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        test(content, expected);
    }

    static private void test(final String content,
                             final String detail0) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        test(content, expected);
    }

    /*static private void test(final String content,
                             final String detail0,
                             final String detail1) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail1));
        test(content, expected);
    }*/

    static private void test(final String content,
                             final String detail0,
                             final String detail1,
                             final String detail2) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail1));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail2));
        test(content, expected);
    }

    static private void test(final String content,
                             final List<NodeValueChecker.Error> expected) throws SAXException {

        List<NodeValueChecker.Error> effective = null;

        final NodeValueChecker checker = new NodeValueChecker(Paths.get("home"), Paths.get("tmp"), new DummyDataController());
        try {
            final Path tempFile = File.createTempFile("NodeValueCheckerTest", ".xml").toPath();
            Files.writeString(tempFile, content);
            effective = checker.check(tempFile);
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        Assertions.assertEquals(normalize(expected), normalize(effective));
    }

    static private String normalize(final List<NodeValueChecker.Error> errors) {
        return errors.stream()
                     .map(e -> e.getDetail())
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }

    static private class DummyDataController implements DataController {

        @Override
        public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do noting
        }

        @Override
        public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do noting
        }
    }
}
