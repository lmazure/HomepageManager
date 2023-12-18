package fr.mazure.homepagemanager.data.violationcorrection.test;

import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdatePageDateCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdatePageDateCorrection class
 */
public class UpdatePageDateCorrectionTest {

    private static final Calendar s_now = Calendar.getInstance();
    private static final int s_now_year = s_now.get(Calendar.YEAR);
    private static final int s_now_month = s_now.get(Calendar.MONTH)+1;
    private static final int s_now_day = s_now.get(Calendar.DAY_OF_MONTH);

    // This test will not work if it crosses midnight.
    @SuppressWarnings("static-method")
    @Test
    void dateIsUpdated() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            </CONTENT>\r
            </PAGE>""";

        final String expected =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" +
            "<TITLE>test</TITLE>\r\n" +
            "<PATH>dummy-dir/test.xml</PATH>\r\n" +
            "<DATE><YEAR>" + s_now_year + "</YEAR><MONTH>" + s_now_month + "</MONTH><DAY>" + s_now_day + "</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        final ViolationCorrection correction = new UpdatePageDateCorrection();
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new UpdatePageDateCorrection();
        Assertions.assertEquals("Update the page date", correction.getDescription());
    }
}
