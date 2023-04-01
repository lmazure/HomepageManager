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
            "<ITEM><ARTICLE><X><T>L'éthique des algorithmes en sérieux danger</T><A>https://www.youtube.com/watch?v=Ddr-BZ9W180</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>7</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Timnit</FIRSTNAME><LASTNAME>Gebru</LASTNAME></AUTHOR> has been fired by Google, <AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR> states once again the importance of research on the ethics of AI.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE predecessor=\"https://www.youtube.com/watch?v=Ddr-BZ9W180\"><X><T>Google démantèle son éthique (et tout le monde s'en fout...)</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\r\n" +
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
            "<ITEM><ARTICLE><X><T>L'éthique des algorithmes en sérieux danger</T><A>https://www.youtube.com/watch?v=Ddr-BZ9W180</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>7</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Timnit</FIRSTNAME><LASTNAME>Gebru</LASTNAME></AUTHOR> has been fired by Google, <AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR> states once again the importance of research on the ethics of AI.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE predecessor=\"https://www.youtube.com/watch?v=Ddr-BZ9W180\"><X><T>Google démantèle son éthique (et tout le monde s'en fout...)</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE><X><T>SoCraTes 2014—A Vibrant German Software Craftsmanship Community</T><A>https://8thlight.com/insights/socrates-2014-a-vibrant-german-software-craftsmanship-community</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Doug</FIRSTNAME><LASTNAME>Bradbury</LASTNAME></AUTHOR><DATE><YEAR>2014</YEAR><MONTH>8</MONTH><DAY>12</DAY></DATE><COMMENT>Some positive feedback about this \"unconference\".</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdateLinkUrlCorrection("https://8thlight.com/blog/doug-bradbury/2014/08/12/socrates-2014-a-vibrant-german-software-craftsmanship-community.html",
                                                                           "https://8thlight.com/insights/socrates-2014-a-vibrant-german-software-craftsmanship-community");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void urlAndPredecessorAreUpdated() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><ARTICLE><X><T>L'éthique des algorithmes en sérieux danger</T><A>https://www.youtube.com/watch?v=Ddr-BZ9W180</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>7</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Timnit</FIRSTNAME><LASTNAME>Gebru</LASTNAME></AUTHOR> has been fired by Google, <AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR> states once again the importance of research on the ethics of AI.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE predecessor=\"https://www.youtube.com/watch?v=Ddr-BZ9W180\"><X><T>Google démantèle son éthique (et tout le monde s'en fout...)</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\r\n" +
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
            "<ITEM><ARTICLE><X><T>L'éthique des algorithmes en sérieux danger</T><A>https://www.mytube.com/watch?v=Ddr-BZ9W180</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>7</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Timnit</FIRSTNAME><LASTNAME>Gebru</LASTNAME></AUTHOR> has been fired by Google, <AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR> states once again the importance of research on the ethics of AI.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE predecessor=\"https://www.mytube.com/watch?v=Ddr-BZ9W180\"><X><T>Google démantèle son éthique (et tout le monde s'en fout...)</T><A>https://www.youtube.com/watch?v=HbFadtOxs4k</A><L>fr</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>57</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>2</MONTH><DAY>22</DAY></DATE><COMMENT>Two months later, Google also fires <AUTHOR><FIRSTNAME>Margarett</FIRSTNAME><LASTNAME>Mitchell</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\r\n" +
            "<ITEM><ARTICLE><X><T>SoCraTes 2014—A Vibrant German Software Craftsmanship Community</T><A>https://8thlight.com/blog/doug-bradbury/2014/08/12/socrates-2014-a-vibrant-german-software-craftsmanship-community.html</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Doug</FIRSTNAME><LASTNAME>Bradbury</LASTNAME></AUTHOR><DATE><YEAR>2014</YEAR><MONTH>8</MONTH><DAY>12</DAY></DATE><COMMENT>Some positive feedback about this \"unconference\".</COMMENT></ARTICLE></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdateLinkUrlCorrection("https://www.youtube.com/watch?v=Ddr-BZ9W180",
                                                                           "https://www.mytube.com/watch?v=Ddr-BZ9W180");
        Assertions.assertEquals(expected, correction.apply(content));
    }
}