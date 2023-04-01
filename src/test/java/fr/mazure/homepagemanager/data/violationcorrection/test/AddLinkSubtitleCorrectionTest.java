package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.AddLinkSubtitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Tests of AddLinkSubtitleCorrection class
 */
public class AddLinkSubtitleCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void subtitleIsAdded() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>700,000 lines of code, 20 years, and one developer: How Dwarf Fortress is built</T><A>https://stackoverflow.blog/2021/12/31/700000-lines-of-code-20-years-and-one-developer-how-dwarf-fortress-is-built/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Tarn</FIRSTNAME><LASTNAME>Adams</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Ryan</FIRSTNAME><LASTNAME>Donovan</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>28</DAY></DATE><COMMENT>An interview of <AUTHOR><FIRSTNAME>Tarn</FIRSTNAME><LASTNAME>Adams</LASTNAME></AUTHOR>, the developer of Dwarf Fortress.</COMMENT></ARTICLE></ITEM>\r\n" +
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
            "<ITEM><ARTICLE><X><T>700,000 lines of code, 20 years, and one developer: How Dwarf Fortress is built</T><ST>Dwarf Fortress is one of those oddball passion projects</ST><A>https://stackoverflow.blog/2021/12/31/700000-lines-of-code-20-years-and-one-developer-how-dwarf-fortress-is-built/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Tarn</FIRSTNAME><LASTNAME>Adams</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Ryan</FIRSTNAME><LASTNAME>Donovan</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>28</DAY></DATE><COMMENT>An interview of <AUTHOR><FIRSTNAME>Tarn</FIRSTNAME><LASTNAME>Adams</LASTNAME></AUTHOR>, the developer of Dwarf Fortress.</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new AddLinkSubtitleCorrection("Dwarf Fortress is one of those oddball passion projects",
                                                                             "https://stackoverflow.blog/2021/12/31/700000-lines-of-code-20-years-and-one-developer-how-dwarf-fortress-is-built/");
        Assertions.assertEquals(expected, correction.apply(content));
    }
}
