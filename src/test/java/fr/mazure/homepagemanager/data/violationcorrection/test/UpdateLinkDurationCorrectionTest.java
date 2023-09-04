package fr.mazure.homepagemanager.data.violationcorrection.test;

import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkDurationCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdateLinkDurationCorrection class
 */
public class UpdateLinkDurationCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void linkDurationIsUpdated() {

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
            "<ITEM><ARTICLE><X><T>Visual Perception with Deep Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>56</MINUTE><SECOND>24</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2008</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdateLinkDurationCorrection(Duration.parse("PT57M25S"),
                                                                                Duration.parse("PT56M24S"),
                                                                                "https://www.youtube.com/watch?v=3boKlkPBckA");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new UpdateLinkDurationCorrection(Duration.parse("PT57M25S"),
                                                                                Duration.parse("PT56M24S"),
                                                                                "https://www.youtube.com/watch?v=3boKlkPBckA");
        Assertions.assertEquals("Update the link duration", correction.getDescription());
    }
}
