package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkSubtitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdateLinkSubtitleCorrection class
 */
class UpdateLinkSubtitleCorrectionTest {

     @SuppressWarnings("static-method")
     @Test
     void subtitleIsUpdated() {

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
            <ITEM><ARTICLE><X><T>WCF #5: A Most Elegant Data Structure</T><ST>ZZA data structure from the 1960’s is still pretty cool today.</ST><A>https://medium.com/@pragdave/wcf-5-a-most-elegant-data-structure-fff8d43a3f69</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Dave</FIRSTNAME><LASTNAME>Thomas</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>28</DAY></DATE><COMMENT>A classical description of doubly-linked list.</COMMENT></ARTICLE></ITEM>\r
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
            <ITEM><ARTICLE><X><T>WCF #5: A Most Elegant Data Structure</T><ST>A data structure from the 1960’s is still pretty cool today.</ST><A>https://medium.com/@pragdave/wcf-5-a-most-elegant-data-structure-fff8d43a3f69</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Dave</FIRSTNAME><LASTNAME>Thomas</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>28</DAY></DATE><COMMENT>A classical description of doubly-linked list.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

         final ViolationCorrection correction = new UpdateLinkSubtitleCorrection("ZZA data structure from the 1960’s is still pretty cool today.",
                                                                                 "A data structure from the 1960’s is still pretty cool today.",
                                                                                 "https://medium.com/@pragdave/wcf-5-a-most-elegant-data-structure-fff8d43a3f69");
         Assertions.assertEquals(expected, correction.apply(content));
     }

     @SuppressWarnings("static-method")
     @Test
     void description() {
         final ViolationCorrection correction = new UpdateLinkSubtitleCorrection("ZZA data structure from the 1960’s is still pretty cool today.",
                                                                                 "A data structure from the 1960’s is still pretty cool today.",
                                                                                 "https://medium.com/@pragdave/wcf-5-a-most-elegant-data-structure-fff8d43a3f69");
         Assertions.assertEquals("Update a link subtitle", correction.getDescription());
     }
}
