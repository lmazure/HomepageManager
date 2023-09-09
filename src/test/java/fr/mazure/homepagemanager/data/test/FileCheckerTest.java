package fr.mazure.homepagemanager.data.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.FileContentChecker;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.filechecker.FileChecker;
import fr.mazure.homepagemanager.utils.FileHelper;

/**
 * Tests of FileChecker
 *
 */
public class FileCheckerTest {

    private static final String s_mess_bom  = "file should not have a UTF BOM";
    private static final String s_mess_ctrl = "line contains a control character";
    private static final String s_mess_white_space = "line is finishing with a white space";
    private static final String s_mess_path = "the name of the file does not appear in the <PATH> node (expected to see \"<PATH>dummy-dir/test.xml</PATH>\")";
    private static final String s_mess_crlf = "line should finish by \\r\\n instead of \\n";
    private static final String s_mess_empty_line = "empty line";
    private static final String s_mess_odd_space_indentation = "odd number of spaces at the beginning of the line";

    @Test
    void testNoError() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content);
    }

    @Test
    void testBomDetection() {

        final String content =
            "\uFEFF<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "SchemaViolation", 0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 1; Content is not allowed in prolog.\")",
             "MissingBom", 1, s_mess_bom);
    }

    @Test
    void testTabDetectionAtBeginning() {

        final String content =
            "\t<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "SchemaViolation", 0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 1; columnNumber: 7; The processing instruction target matching \"[xX][mM][lL]\" is not allowed.\")",
             "ControlCharacter", 1, s_mess_ctrl + " (x9) at column 1");
    }

    @Test
    void testNonBreakingSpaceAreNotReported() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>foo\u00A0bar\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content);
    }

    @Test
    void testTabDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "\t<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\t\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "ControlCharacter", 5, s_mess_ctrl + " (x9) at column 1",
             "ControlCharacter", 6, s_mess_ctrl + " (x9) at column 60",
             "WhiteSpaceAtLineEnd", 6, s_mess_white_space);
    }

    @Test
    void testTabDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>\t";

        test(content,
             "ControlCharacter", 9, s_mess_ctrl + " (x9) at column 8",
             "WhiteSpaceAtLineEnd", 9, s_mess_white_space);
    }

    @Test
    void testSpaceDetectionAtBeginning() {

        final String content =
            "<?xml version=\"1.0\"?> \r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "WhiteSpaceAtLineEnd", 1, s_mess_white_space);
    }

    @Test
    void testSpaceDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH> \r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE> \r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "WhiteSpaceAtLineEnd", 5, s_mess_white_space,
             "WhiteSpaceAtLineEnd", 6, s_mess_white_space);
    }

    @Test
    void testSpaceDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE> ";

        test(content,
             "WhiteSpaceAtLineEnd", 9, s_mess_white_space);
    }

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
             "WrongPath", 5, s_mess_path);
    }

    @Test
    void testMissingCarriageReturn() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "BadEndOfLine", 4, s_mess_crlf);
    }

    @Test
    void testEmptyLineDetectionAtBeginning() {

        final String content =
            "\r\n" +
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "SchemaViolation", 0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 2; columnNumber: 6; The processing instruction target matching \"[xX][mM][lL]\" is not allowed.\")",
             "EmptyLine", 1, s_mess_empty_line);
    }

    @Test
    void testEmptyLineDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "EmptyLine", 6, s_mess_empty_line);
    }

    @Test
    void testEmptyLineDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>\r\n";

        test(content,
             "EmptyLine", 10, s_mess_empty_line);
    }

    @Test
    void testWhiteLineDetectionAtBeginning() {

        final String content =
            " \r\n" +
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "SchemaViolation", 0, "the file violates the schema (\"org.xml.sax.SAXParseException; lineNumber: 2; columnNumber: 6; The processing instruction target matching \"[xX][mM][lL]\" is not allowed.\")",
             "EmptyLine", 1, s_mess_empty_line,
             "WhiteSpaceAtLineEnd", 1, s_mess_white_space,
             "OddIndentation", 1, s_mess_odd_space_indentation);
    }

    @Test
    void testWhiteLineDetectionInMiddle() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "  \r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "EmptyLine", 6, s_mess_empty_line,
             "WhiteSpaceAtLineEnd", 6, s_mess_white_space);
    }

    @Test
    void testWhiteLineDetectionAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>\r\n" +
            " ";

        test(content,
             "EmptyLine", 10, s_mess_empty_line,
             "WhiteSpaceAtLineEnd", 10, s_mess_white_space,
             "OddIndentation", 10, s_mess_odd_space_indentation);
    }

    @Test
    void testOddNumberOfSpaces() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            " <PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "  <TITLE>test</TITLE>\r\n" +
            "   <PATH>dummy-dir/test.xml</PATH>\r\n" +
            "    <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        test(content,
             "OddIndentation", 3, s_mess_odd_space_indentation,
             "OddIndentation", 5, s_mess_odd_space_indentation);
    }

    @Test
    void testBadGreaterThanCharacter() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
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
             "GreaterThanCharacter", 9, "the line contains a \">\"");
    }

    @Test
    void testSpaceInTag() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
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
             "SpaceInXmlNode", 13, "the line contains space in an XML tag \"</CONTENT >\"");
    }

    @Test
    void testSpaceInAttribute() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
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
             "SpaceInAttributeSetting", 9, "the line contains space near \"=\" in an XML attribute \"<X quality =\"1\">\"",
             "SpaceInAttributeSetting", 10, "the line contains space near \"=\" in an XML attribute \"<X quality= \"1\" status=\"dead\">\"");
    }

    @Test
    void testDoubleSpaceInAttribute() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
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
             "DoubleSpaceInXmlNode", 9, "the line contains double space in an XML attribute \"<X  quality=\"1\">\"");
    }

    @Test
    void testXmlAttributeBetweenSingleQuote() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
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
                "AttributeBetweenSingleQuotes", 10, "the line contains an XML attribute between single quotes \"<X status='dead'>\"");
       }

        @Test
        void testLocalLinks() {

            final String content =
                "<?xml version=\"1.0\"?>\r\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
                "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
                "<TITLE>test</TITLE>\r\n" +
                "<PATH>dummy-dir/test.xml</PATH>\r\n" +
                "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
                "<CONTENT>\r\n" +
                "<ANCHOR>here</ANCHOR>\r\n" +
                "<X><T>link 1 → OK</T><A>test.html#here</A><L>en</L><F>HTML</F></X>\r\n" +
                "<X><T>link 2 → OK</T><A>../dummy-dir/test.html#here</A><L>en</L><F>HTML</F></X>\r\n" +
                "<X><T>link 3 → KO</T><A>tast.html#here</A><L>en</L><F>HTML</F></X>\r\n" +
                "<X><T>link 4 → KO</T><A>test.html#hare</A><L>en</L><F>HTML</F></X>\r\n" +
                "</CONTENT>\r\n" +
                "</PAGE>";

            test(content,
                 "IncorrectLocalLink", 0, "the file \"H:\\Documents\\tmp\\hptmp\\test\\FileCheckerTest\\dummy-dir\\tast.xml\" does not exist",
                 "IncorrectLocalLink", 0, "the file \"H:\\Documents\\tmp\\hptmp\\test\\FileCheckerTest\\dummy-dir\\test.xml\" does not contain the anchor \"hare\"");
        }

    private void test(final String content) {

        final List<FileContentChecker.Error> expected = new ArrayList<>();
        test(content, expected);
    }

    private void test(final String content,
                      final String checkName0, final int line0, final String message0) {

        final List<FileContentChecker.Error> expected = new ArrayList<>();
        expected.add(new FileContentChecker.Error(checkName0, line0, message0));
        test(content, expected);
    }

    private void test(final String content,
                      final String checkName0, final int line0, final String message0,
                      final String checkName1, final int line1, final String message1) {

        final List<FileContentChecker.Error> expected = new ArrayList<>();
        expected.add(new FileContentChecker.Error(checkName0, line0, message0));
        expected.add(new FileContentChecker.Error(checkName1, line1, message1));
        test(content, expected);
    }

    private void test(final String content,
                      final String checkName0, final int line0, final String message0,
                      final String checkName1, final int line1, final String message1,
                      final String checkName2, final int line2, final String message2) {

        final List<FileContentChecker.Error> expected = new ArrayList<>();
        expected.add(new FileContentChecker.Error(checkName0, line0, message0));
        expected.add(new FileContentChecker.Error(checkName1, line1, message1));
        expected.add(new FileContentChecker.Error(checkName2, line2, message2));
        test(content, expected);
    }

    private void test(final String content,
                      final String checkName0, final int line0, final String message0,
                      final String checkName1, final int line1, final String message1,
                      final String checkName2, final int line2, final String message2,
                      final String checkName3, final int line3, final String message3) {

        final List<FileContentChecker.Error> expected = new ArrayList<>();
        expected.add(new FileContentChecker.Error(checkName0, line0, message0));
        expected.add(new FileContentChecker.Error(checkName1, line1, message1));
        expected.add(new FileContentChecker.Error(checkName2, line2, message2));
        expected.add(new FileContentChecker.Error(checkName3, line3, message3));
        test(content, expected);
    }

    private void test(final String content,
                      final List<FileContentChecker.Error> expected) {

        final FileChecker checker = new FileChecker(Paths.get("testdata"));
        final Path root = TestHelper.getTestDatapath(getClass());
        final Path path = root.resolve(Paths.get("dummy-dir","test.xml"));
        FileHelper.createParentDirectory(path);
        FileHelper.writeFile(path, content);
        final List<FileContentChecker.Error> effective = checker.check(path);
        FileHelper.deleteFile(path);

        Assertions.assertEquals(normalize(expected), normalize(effective));
    }

    private static String normalize(final List<FileContentChecker.Error> errors) {
        return errors.stream()
                     .map(e -> String.format("<<%s>> [line=%02d] %s", e.checkName(), Integer.valueOf(e.lineNumber()), e.errorMessage()))
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }
}
