package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.RemoveLinkSubtitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Tests of RemoveLinkSubtitleCorrection class
 */
public class RemoveLinkSubtitleCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void subtitleIsRemoved() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>Java on Arm processors: Understanding AArch64 vs. x86</T><ST>Arm-based processors are increasingly popular and are in the news thanks to Apple’s latest notebooks and Oracle’s cloud services.</ST><A>https://blogs.oracle.com/javamagazine/post/java-on-arm-processors-understanding-aarch64-vs-x86</A><L>en</L><F>HTML</F><DATE><YEAR>2021</YEAR><MONTH>1</MONTH><DAY>16</DAY></DATE></X><AUTHOR><FIRSTNAME>Aleksei</FIRSTNAME><LASTNAME>Voitylov</LASTNAME></AUTHOR><DATE><YEAR>2018</YEAR><MONTH>9</MONTH></DATE><COMMENT>A description of the current Java support on ARMv8 and a small performance benchmark to compare with Intel.</COMMENT></ARTICLE></ITEM>\r\n" +
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
            "<ITEM><ARTICLE><X><T>Java on Arm processors: Understanding AArch64 vs. x86</T><A>https://blogs.oracle.com/javamagazine/post/java-on-arm-processors-understanding-aarch64-vs-x86</A><L>en</L><F>HTML</F><DATE><YEAR>2021</YEAR><MONTH>1</MONTH><DAY>16</DAY></DATE></X><AUTHOR><FIRSTNAME>Aleksei</FIRSTNAME><LASTNAME>Voitylov</LASTNAME></AUTHOR><DATE><YEAR>2018</YEAR><MONTH>9</MONTH></DATE><COMMENT>A description of the current Java support on ARMv8 and a small performance benchmark to compare with Intel.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new RemoveLinkSubtitleCorrection("Arm-based processors are increasingly popular and are in the news thanks to Apple’s latest notebooks and Oracle’s cloud services.",
                                                                                "https://blogs.oracle.com/javamagazine/post/java-on-arm-processors-understanding-aarch64-vs-x86");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new RemoveLinkSubtitleCorrection("Arm-based processors are increasingly popular and are in the news thanks to Apple’s latest notebooks and Oracle’s cloud services.",
                                                                                "https://blogs.oracle.com/javamagazine/post/java-on-arm-processors-understanding-aarch64-vs-x86");
        Assertions.assertEquals("Remove link subtitle", correction.getDescription());
    }
}
