package fr.mazure.homepagemanager.data.violationcorrection.test;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateArticleDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdateArticleDateCorrection class
 */
public class UpdateArticleDateCorrectionTest {


    @SuppressWarnings("static-method")
    @Test
    void articleDateIsUpdated() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Visual Perception with Deep Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>57</MINUTE><SECOND>25</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2008</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r\n" +
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
            "<ITEM><ARTICLE><X><T>Visual Perception with Deep Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>57</MINUTE><SECOND>25</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2009</YEAR><MONTH>5</MONTH><DAY>11</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdateArticleDateCorrection(LocalDate.of(2008, 4, 10),
                                                                               LocalDate.of(2009, 5, 11),
                                                                               "https://www.youtube.com/watch?v=3boKlkPBckA");
        Assertions.assertEquals(expected, correction.apply(content));
    }
}
