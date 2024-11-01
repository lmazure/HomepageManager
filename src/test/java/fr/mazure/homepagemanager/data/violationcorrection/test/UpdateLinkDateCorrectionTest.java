package fr.mazure.homepagemanager.data.violationcorrection.test;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdateLinkDateCorrection class
 */
class UpdateLinkDateCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void articleDateIsUpdated() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Visual Perception with Deep Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>57</MINUTE><SECOND>25</SECOND></DURATION><DATE><YEAR>2024</YEAR><MONTH>1</MONTH><DAY>2</DAY></DATE></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2008</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final String expected =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Visual Perception with Deep Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>57</MINUTE><SECOND>25</SECOND></DURATION><DATE><YEAR>2024</YEAR><MONTH>3</MONTH><DAY>4</DAY></DATE></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2008</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final ViolationCorrection correction = new UpdateLinkDateCorrection(LocalDate.of(2024, 1, 2),
                                                                            LocalDate.of(2024, 3, 4),
                                                                            "https://www.youtube.com/watch?v=3boKlkPBckA");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new UpdateLinkDateCorrection(LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 3, 4),
                "https://www.youtube.com/watch?v=3boKlkPBckA");
        Assertions.assertEquals("Update the link date", correction.getDescription());
    }
}
