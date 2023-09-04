package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateFeedFormatCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.xmlparsing.FeedFormat;

/**
 * Test of UpdateFeedFormatCorrection class
 */
public class UpdateFeedFormatCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void feedFormatIsUpdated() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Articles and videos</TITLE>\r\n" +
            "<ITEM><X><T>Get It</T><A>https://www.youtube.com/channel/UCNAaM25o2k2Guv7Lrf-xuDw</A><L>en</L><F>HTML</F><FEED><A>https://www.youtube.com/feeds/videos.xml?channel_id=UCNAaM25o2k2Guv7Lrf-xuDw</A><F>RSS</F></FEED></X></ITEM>\r\n" +
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
            "<ITEM><X><T>Get It</T><A>https://www.youtube.com/channel/UCNAaM25o2k2Guv7Lrf-xuDw</A><L>en</L><F>HTML</F><FEED><A>https://www.youtube.com/feeds/videos.xml?channel_id=UCNAaM25o2k2Guv7Lrf-xuDw</A><F>Atom</F></FEED></X></ITEM>\r\n" +
            "</BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdateFeedFormatCorrection(FeedFormat.RSS,
                                                                              FeedFormat.Atom,
                                                                              "https://www.youtube.com/feeds/videos.xml?channel_id=UCNAaM25o2k2Guv7Lrf-xuDw");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new UpdateFeedFormatCorrection(FeedFormat.RSS,
                                                                              FeedFormat.Atom,
                                                                              "https://www.youtube.com/feeds/videos.xml?channel_id=UCNAaM25o2k2Guv7Lrf-xuDw");
        Assertions.assertEquals("Update the feed format", correction.getDescription());
    }
}