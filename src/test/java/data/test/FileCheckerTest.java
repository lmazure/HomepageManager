package data.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.DataController;
import data.FileChecker;
import data.FileHandler.Status;

class FileCheckerTest {

    private static final String MESS_BOM  = "file should not have a UTF BOM";
    private static final String MESS_CTRL = "line contains a control character";
    private static final String MESS_WTSP = "line is finishing with a white space";
    private static final String MESS_PATH = "the name of the file does not appear in the <PATH> node (expected to see \"<PATH>HomepageManager/test.xml</PATH>\")";
    private static final String MESS_CRLF = "line should finish by \\r\\n instead of \\n";
    private static final String MESS_EMPT = "empty line";
    private static final String MESS_ODSP = "odd number of spaces at the beginning of the line";

    @SuppressWarnings("static-method")
    @Test
    void testNoError() {

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

        test(content);
    }

    @SuppressWarnings("static-method")
    @Test
    void testBomDetection() {

        final String content =
            "\uFEFF<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Content is not allowed in prolog.\")",
             1, MESS_BOM);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTabDetectionAtBeginning() {

        final String content =
            "\t<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 7; The processing instruction target matching \"[xX][mM][lL]\" is not allowed.\")",
             1, MESS_CTRL + " (x9) at column 1");
    }

    @SuppressWarnings("static-method")
    @Test
    void testNonBreakingSpaceAreNotReported() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>foo\u00A0bar\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTabDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "\t<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\t\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             5, MESS_CTRL + " (x9) at column 1",
             6, MESS_CTRL + " (x9) at column 60",
             6, MESS_WTSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTabDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>\t";

        test(content,
             9, MESS_CTRL + " (x9) at column 8",
             9, MESS_WTSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testSpaceDetectionAtBeginning() {

        final String content =
            "<?xml version=\"1.0\"?> \r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             1, MESS_WTSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testSpaceDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH> \r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE> \r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             5, MESS_WTSP,
             6, MESS_WTSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testSpaceDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE> ";

        test(content,
             9, MESS_WTSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testIncorrectPath() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/wrong.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             5, MESS_PATH);
    }

    @SuppressWarnings("static-method")
    @Test
    void testMissingCarriageReturn() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             4, MESS_CRLF);
    }

    @SuppressWarnings("static-method")
    @Test
    void testEmptyLineDetectionAtBeginning() {

        final String content =
            "\r\n" +
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 2; columnNumber: 6; The processing instruction target matching \"[xX][mM][lL]\" is not allowed.\")",
             1, MESS_EMPT);
    }

    @SuppressWarnings("static-method")
    @Test
    void testEmptyLineDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             6, MESS_EMPT);
    }

    @SuppressWarnings("static-method")
    @Test
    void testEmptyLineDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>\r\n";

        test(content,
             10, MESS_EMPT);
    }

    @SuppressWarnings("static-method")
    @Test
    void testWhiteLineDetectionAtBeginning() {

        final String content =
            " \r\n" +
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 2; columnNumber: 6; The processing instruction target matching \"[xX][mM][lL]\" is not allowed.\")",
             1, MESS_EMPT,
             1, MESS_WTSP,
             1, MESS_ODSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testWhiteLineDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "  \r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             6, MESS_EMPT,
             6, MESS_WTSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testWhiteLineDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>\r\n" +
            " ";

        test(content,
             10, MESS_EMPT,
             10, MESS_WTSP,
             10, MESS_ODSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testOddNumberOfSpaces() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            " <PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "  <TITLE>test</TITLE>\r\n" +
            "   <PATH>HomepageManager/test.xml</PATH>\r\n" +
            "    <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             3, MESS_ODSP,
             5, MESS_ODSP);
    }

    @SuppressWarnings("static-method")
    @Test
    void testBadGreaterThanCharacter() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY> </DATE>\r\n" +
            "<CONTENT>\r\n" +
            "  <BLIST><TITLE>My articles</TITLE>\r\n" +
            "    <ITEM><ARTICLE><X><T>>Antisocial Coding: My Year at GitHub</T><A>https://where.coraline.codes/blog/my-year-at-github/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Coraline Ada</FIRSTNAME><LASTNAME>Ehmke</LASTNAME></AUTHOR><DATE><YEAR>2017</YEAR><MONTH>7</MONTH><DAY>5</DAY></DATE><COMMENT>The author, a transgender, described her life in and firing from GitHub.</COMMENT></ARTICLE></ITEM>\r\n" +
            "    <ITEM><ARTICLE><X status=\"dead\"><T>example</T><A>https://example.com</A><L>en</L><F>HTML</F></X><DATE><YEAR>2021</YEAR></DATE><COMMENT>blabla</COMMENT></ARTICLE></ITEM>\r\n" +
            "  <!-- comment -->\r\n" +
            "  </BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             9, "the line contains a \">\"");
    }

    @SuppressWarnings("static-method")
    @Test
    void testSpaceInTag() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "  <BLIST><TITLE>My articles</TITLE>\r\n" +
            "    <ITEM><ARTICLE><X><T>Antisocial Coding: My Year at GitHub</T><A>https://where.coraline.codes/blog/my-year-at-github/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Coraline Ada</FIRSTNAME><LASTNAME>Ehmke</LASTNAME></AUTHOR><DATE><YEAR>2017</YEAR><MONTH>7</MONTH><DAY>5</DAY></DATE><COMMENT>The author, a transgender, described her life in and firing from GitHub.</COMMENT></ARTICLE></ITEM>\r\n" +
            "    <ITEM><ARTICLE><X status=\"dead\"><T>example</T><A>https://example.com</A><L>en</L><F>HTML</F></X><DATE><YEAR>2021</YEAR></DATE><COMMENT>blabla</COMMENT></ARTICLE></ITEM>\r\n" +
            "  <!-- comment -->\r\n" +
            "  </BLIST>\r\n" +
            "</CONTENT >\r\n" +
            "</PAGE>";

        test(content,
             13, "the line contains space in an XML tag \"</CONTENT >\"");
    }

    @SuppressWarnings("static-method")
    @Test
    void testSpaceInAttribute() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "  <BLIST><TITLE>My articles</TITLE>\r\n" +
            "    <ITEM><ARTICLE><X quality =\"1\"><T>Antisocial Coding: My Year at GitHub</T><A>https://where.coraline.codes/blog/my-year-at-github/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Coraline Ada</FIRSTNAME><LASTNAME>Ehmke</LASTNAME></AUTHOR><DATE><YEAR>2017</YEAR><MONTH>7</MONTH><DAY>5</DAY></DATE><COMMENT>The author, a transgender, described her life in and firing from GitHub.</COMMENT></ARTICLE></ITEM>\r\n" +
            "    <ITEM><ARTICLE><X quality= \"1\" status=\"dead\"><T>example</T><A>https://example.com</A><L>en</L><F>HTML</F></X><DATE><YEAR>2021</YEAR></DATE><COMMENT>blabla</COMMENT></ARTICLE></ITEM>\r\n" +
            "  <!-- comment -->\r\n" +
            "  </BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
                9, "the line contains space near \"=\" in an XML attribute \"<X quality =\"1\">\"",
                10, "the line contains space near \"=\" in an XML attribute \"<X quality= \"1\" status=\"dead\">\"");
    }

    @SuppressWarnings("static-method")
    @Test
    void testDoubleSpaceInAttribute() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "  <BLIST><TITLE>My articles</TITLE>\r\n" +
            "    <ITEM><ARTICLE><X  quality=\"1\"><T>Antisocial Coding: My Year at GitHub</T><A>https://where.coraline.codes/blog/my-year-at-github/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Coraline Ada</FIRSTNAME><LASTNAME>Ehmke</LASTNAME></AUTHOR><DATE><YEAR>2017</YEAR><MONTH>7</MONTH><DAY>5</DAY></DATE><COMMENT>The author, a transgender, described her life in and firing from GitHub.</COMMENT></ARTICLE></ITEM>\r\n" +
            "    <ITEM><ARTICLE><X quality=\"1\" status=\"dead\"><T>example</T><A>https://example.com</A><L>en</L><F>HTML</F></X><DATE><YEAR>2021</YEAR></DATE><COMMENT>blabla</COMMENT></ARTICLE></ITEM>\r\n" +
            "  <!-- comment -->\r\n" +
            "  </BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             9, "the line contains double space in an XML attribute \"<X  quality=\"1\">\"");
    }

    @SuppressWarnings("static-method")
    @Test
    void testXmlAttributeBetweenSingleQuote() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "  <BLIST><TITLE>My articles</TITLE>\r\n" +
            "    <ITEM><ARTICLE><X><T>Antisocial Coding: My Year at GitHub</T><A>https://where.coraline.codes/blog/my-year-at-github/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Coraline Ada</FIRSTNAME><LASTNAME>Ehmke</LASTNAME></AUTHOR><DATE><YEAR>2017</YEAR><MONTH>7</MONTH><DAY>5</DAY></DATE><COMMENT>The author, a transgender, described her life in and firing from GitHub.</COMMENT></ARTICLE></ITEM>\r\n" +
            "    <ITEM><ARTICLE><X status='dead'><T>example</T><A>https://example.com</A><L>en</L><F>HTML</F></X><DATE><YEAR>2021</YEAR></DATE><COMMENT>blabla</COMMENT></ARTICLE></ITEM>\r\n" +
            "  <!-- comment -->\r\n" +
            "  </BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             10, "the line contains an XML attribute between single quotes \"<X status='dead'>\"");
    }

    private static void test(final String content) {

        final List<FileChecker.Error> expected = new ArrayList<>();
        test(content, expected);
    }

    private static void test(final String content,
                             final int line0, final String message0) {

        final List<FileChecker.Error> expected = new ArrayList<>();
        expected.add(new FileChecker.Error(line0, message0));
        test(content, expected);
    }

    private static void test(final String content,
                             final int line0, final String message0,
                             final int line1, final String message1) {

        final List<FileChecker.Error> expected = new ArrayList<>();
        expected.add(new FileChecker.Error(line0, message0));
        expected.add(new FileChecker.Error(line1, message1));
        test(content, expected);
    }

    private static void test(final String content,
                             final int line0, final String message0,
                             final int line1, final String message1,
                             final int line2, final String message2) {

        final List<FileChecker.Error> expected = new ArrayList<>();
        expected.add(new FileChecker.Error(line0, message0));
        expected.add(new FileChecker.Error(line1, message1));
        expected.add(new FileChecker.Error(line2, message2));
        test(content, expected);
    }

    private static void test(final String content,
                             final int line0, final String message0,
                             final int line1, final String message1,
                             final int line2, final String message2,
                             final int line3, final String message3) {

        final List<FileChecker.Error> expected = new ArrayList<>();
        expected.add(new FileChecker.Error(line0, message0));
        expected.add(new FileChecker.Error(line1, message1));
        expected.add(new FileChecker.Error(line2, message2));
        expected.add(new FileChecker.Error(line3, message3));
        test(content, expected);
    }

    private static void test(final String content,
                             final List<FileChecker.Error> expected) {

        final FileChecker checker = new FileChecker(Paths.get("testdata"), Paths.get("tmp"), new DummyDataController());
        final List<FileChecker.Error> effective = checker.check(Paths.get("test.xml"), content);

        Assertions.assertEquals(normalize(expected), normalize(effective));
    }

    private static String normalize(final List<FileChecker.Error> errors) {
        return errors.stream()
                     .map(e -> String.format("%02d %s", Integer.valueOf(e.getLineNumber()), e.getErrorMessage()))
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
