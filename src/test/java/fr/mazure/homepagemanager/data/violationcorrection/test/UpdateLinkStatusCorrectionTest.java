package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkStatusCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Test of UpdateLinkStatusCorrection class
 */
public class UpdateLinkStatusCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void fromDeadToOkWithoutSubtitle() {

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
        	<ITEM><ARTICLE><X status="dead"><T>Getting started with JML</T><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
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
        	<ITEM><ARTICLE><X><T>Getting started with JML</T><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
        	</BLIST>\r
        	</CONTENT>\r
        	</PAGE>""";

        final ViolationCorrection correction = new UpdateLinkStatusCorrection(LinkStatus.DEAD,
                                                                              LinkStatus.OK,
                                                                              "https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void fromOkToDeadWithoutSubtitle() {

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
        	<ITEM><ARTICLE><X><T>Getting started with JML</T><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
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
        	<ITEM><ARTICLE><X status="dead"><T>Getting started with JML</T><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
        	</BLIST>\r
        	</CONTENT>\r
        	</PAGE>""";

        final ViolationCorrection correction = new UpdateLinkStatusCorrection(LinkStatus.OK,
                                                                              LinkStatus.DEAD,
                                                                              "https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf");
        Assertions.assertEquals(expected, correction.apply(content));
    }


    @SuppressWarnings("static-method")
    @Test
    void fromDeadToOkWithSubtitle() {

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
        	<ITEM><ARTICLE><X status="dead"><T>Getting started with JML</T><ST>Improve your Java programs with JML annotation</ST><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
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
        	<ITEM><ARTICLE><X><T>Getting started with JML</T><ST>Improve your Java programs with JML annotation</ST><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
        	</BLIST>\r
        	</CONTENT>\r
        	</PAGE>""";

        final ViolationCorrection correction = new UpdateLinkStatusCorrection(LinkStatus.DEAD,
                                                                              LinkStatus.OK,
                                                                              "https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf");
        Assertions.assertEquals(expected, correction.apply(content));
    }


    @SuppressWarnings("static-method")
    @Test
    void fromOkToZombieWithQuality() {

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
        	<ITEM><ARTICLE><X quality="-1"><T>2D animation with image-based paths</T><ST>Take the heavy coding out of fixed-object animation</ST><A>https://www.ibm.com/developerworks/java/library/j-animat/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Barry</FIRSTNAME><MIDDLENAME>A.</MIDDLENAME><LASTNAME>Feigenbaum</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Tom</FIRSTNAME><LASTNAME>Brunet</LASTNAME></AUTHOR><DATE><YEAR>2004</YEAR><MONTH>1</MONTH><DAY>9</DAY></DATE><COMMENT>The paths of some sprites is recorded using some colour encoding. Not a big deal…</COMMENT></ARTICLE></ITEM>\r
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
        	<ITEM><ARTICLE><X quality="-1" status="zombie"><T>2D animation with image-based paths</T><ST>Take the heavy coding out of fixed-object animation</ST><A>https://www.ibm.com/developerworks/java/library/j-animat/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Barry</FIRSTNAME><MIDDLENAME>A.</MIDDLENAME><LASTNAME>Feigenbaum</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Tom</FIRSTNAME><LASTNAME>Brunet</LASTNAME></AUTHOR><DATE><YEAR>2004</YEAR><MONTH>1</MONTH><DAY>9</DAY></DATE><COMMENT>The paths of some sprites is recorded using some colour encoding. Not a big deal…</COMMENT></ARTICLE></ITEM>\r
        	</BLIST>\r
        	</CONTENT>\r
        	</PAGE>""";

        final ViolationCorrection correction = new UpdateLinkStatusCorrection(LinkStatus.OK,
                                                                              LinkStatus.ZOMBIE,
                                                                              "https://www.ibm.com/developerworks/java/library/j-animat/");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void fromZombieToOkWithQuality() {

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
        	<ITEM><ARTICLE><X quality="-1" status="zombie"><T>2D animation with image-based paths</T><ST>Take the heavy coding out of fixed-object animation</ST><A>https://www.ibm.com/developerworks/java/library/j-animat/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Barry</FIRSTNAME><MIDDLENAME>A.</MIDDLENAME><LASTNAME>Feigenbaum</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Tom</FIRSTNAME><LASTNAME>Brunet</LASTNAME></AUTHOR><DATE><YEAR>2004</YEAR><MONTH>1</MONTH><DAY>9</DAY></DATE><COMMENT>The paths of some sprites is recorded using some colour encoding. Not a big deal…</COMMENT></ARTICLE></ITEM>\r
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
        	<ITEM><ARTICLE><X quality="-1"><T>2D animation with image-based paths</T><ST>Take the heavy coding out of fixed-object animation</ST><A>https://www.ibm.com/developerworks/java/library/j-animat/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Barry</FIRSTNAME><MIDDLENAME>A.</MIDDLENAME><LASTNAME>Feigenbaum</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Tom</FIRSTNAME><LASTNAME>Brunet</LASTNAME></AUTHOR><DATE><YEAR>2004</YEAR><MONTH>1</MONTH><DAY>9</DAY></DATE><COMMENT>The paths of some sprites is recorded using some colour encoding. Not a big deal…</COMMENT></ARTICLE></ITEM>\r
        	</BLIST>\r
        	</CONTENT>\r
        	</PAGE>""";

        final ViolationCorrection correction = new UpdateLinkStatusCorrection(LinkStatus.ZOMBIE,
                                                                              LinkStatus.OK,
                                                                              "https://www.ibm.com/developerworks/java/library/j-animat/");
        Assertions.assertEquals(expected, correction.apply(content));
    }


    @SuppressWarnings("static-method")
    @Test
    void fromOkToDeadWithSubtitle() {

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
        	<ITEM><ARTICLE><X><T>Getting started with JML</T><ST>Improve your Java programs with JML annotation</ST><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
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
        	<ITEM><ARTICLE><X status="dead"><T>Getting started with JML</T><ST>Improve your Java programs with JML annotation</ST><A>https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf</A><L>en</L><F>PDF</F></X><AUTHOR><FIRSTNAME>Joe</FIRSTNAME><LASTNAME>Verzulli</LASTNAME></AUTHOR><DATE><YEAR>2003</YEAR><MONTH>3</MONTH><DAY>18</DAY></DATE><COMMENT>A presentation of JML: an assertion mechanism.</COMMENT></ARTICLE></ITEM>\r
        	</BLIST>\r
        	</CONTENT>\r
        	</PAGE>""";

        final ViolationCorrection correction = new UpdateLinkStatusCorrection(LinkStatus.OK,
                                                                              LinkStatus.DEAD,
                                                                              "https://moodle-arquivo.ciencias.ulisboa.pt/1415/pluginfile.php/99900/mod_page/content/9/doc/j-jml-pdf.pdf");
        Assertions.assertEquals(expected, correction.apply(content));
    }
}
