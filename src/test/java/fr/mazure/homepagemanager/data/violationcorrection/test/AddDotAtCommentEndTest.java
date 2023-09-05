package fr.mazure.homepagemanager.data.violationcorrection.test;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.AddDotAtCommentEnd;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Tests of the AddDotAtCommentEnd class
 *
 */
public class AddDotAtCommentEndTest {

    @SuppressWarnings("static-method")
    @Test
    void commentWithNoNode() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires Margarett Mitchell</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final String expected =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires Margarett Mitchell.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new AddDotAtCommentEnd(List.of("Two months later, Google also fires Margarett Mitchell"));
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void commentWithOneNodeAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR></COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final String expected =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new AddDotAtCommentEnd(List.of("Two months later, Google also fires "));
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void commentWithTwoNodesAtEnd() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR> and <AUTHOR><FIRSTNAME>John</FIRSTNAME><LASTNAME>Doe</LASTNAME></AUTHOR></COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final String expected =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR> and <AUTHOR><FIRSTNAME>John</FIRSTNAME><LASTNAME>Doe</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new AddDotAtCommentEnd(List.of("Two months later, Google also fires ", " and "));
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void commentWithOneNodeAtBeginning() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR> has been fired</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final String expected =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR> has been fired.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new AddDotAtCommentEnd(List.of(" has been fired"));
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void commentWithtwoNodesAtBeginning() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR> and <AUTHOR><FIRSTNAME>John</FIRSTNAME><LASTNAME>Doe</LASTNAME></AUTHOR> have been fired</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final String expected =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Google démantèle son éthique</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR> and <AUTHOR><FIRSTNAME>John</FIRSTNAME><LASTNAME>Doe</LASTNAME></AUTHOR> have been fired.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new AddDotAtCommentEnd(List.of(" and ", " have been fired"));
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new AddDotAtCommentEnd(List.of(" and ", " have been fired"));
        Assertions.assertEquals("Add a dot at comment end", correction.getDescription());
    }
}