package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkUrlCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdateLinkUrlCorrection class
 */
public class UpdateLinkUrlCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void urlIsUpdated() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>SoCraTes 2014—A Vibrant German Software Craftsmanship Community</T><A>https://8thlight.com/blog/doug-bradbury/2014/08/12/socrates-2014-a-vibrant-german-software-craftsmanship-community.html</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Doug</FIRSTNAME><LASTNAME>Bradbury</LASTNAME></AUTHOR><DATE><YEAR>2014</YEAR><MONTH>8</MONTH><DAY>12</DAY></DATE><COMMENT>Some positive feedback about this \"unconference\".</COMMENT></ARTICLE></ITEM>\r\n" +
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
            "<ITEM><ARTICLE><X><T>SoCraTes 2014—A Vibrant German Software Craftsmanship Community</T><A>https://8thlight.com/insights/socrates-2014-a-vibrant-german-software-craftsmanship-community</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Doug</FIRSTNAME><LASTNAME>Bradbury</LASTNAME></AUTHOR><DATE><YEAR>2014</YEAR><MONTH>8</MONTH><DAY>12</DAY></DATE><COMMENT>Some positive feedback about this \"unconference\".</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdateLinkUrlCorrection("https://8thlight.com/blog/doug-bradbury/2014/08/12/socrates-2014-a-vibrant-german-software-craftsmanship-community.html",
                                                                           "https://8thlight.com/insights/socrates-2014-a-vibrant-german-software-craftsmanship-community");
        Assertions.assertEquals(expected, correction.apply(content));    }
}