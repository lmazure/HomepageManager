package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of IncorrectSpaceChecker class
 */
class IncorrectSpaceCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void checkAllBasicCasesInEnglish() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Aab,xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab, xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ,xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab , xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab;xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab; xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ;xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ; xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab:xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab: xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab :xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab : xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab.xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab. xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab .xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab . xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab!xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab! xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab !xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ! xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab?xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab? xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ?xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ? xyz</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"Aab ! xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab !xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab !xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab , xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ,xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ,xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab . xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab .xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab .xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab : xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab :xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab :xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab ; xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ;xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ;xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab ? xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ?xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ?xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab!xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab,xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab.xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab:xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab;xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab?xyz\" is missing a space after punctuation<<MissingSpace>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void checkAllBasicCasesInFrench() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="fr">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Aab,xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab, xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ,xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab , xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab;xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab; xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ;xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ; xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab:xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab: xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab :xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab : xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab.xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab. xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab .xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab . xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab!xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab! xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab !xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ! xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab?xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab? xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ?xyz</TITLE></BLIST>\r
            <BLIST><TITLE>Aab ? xyz</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"Aab !xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab , xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ,xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab ,xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab . xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab .xyz\" has a space before a punctuation (at index 4)<<SpuriousSpace>>",
                 "\"Aab .xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab :xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab ;xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab ?xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab!xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab,xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab.xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab: xyz\" is missing a space before punctuation<<MissingSpace>>",
                 "\"Aab:xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab:xyz\" is missing a space before punctuation<<MissingSpace>>",
                 "\"Aab; xyz\" is missing a space before punctuation<<MissingSpace>>",
                 "\"Aab;xyz\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Aab;xyz\" is missing a space before punctuation<<MissingSpace>>",
                 "\"Aab?xyz\" is missing a space after punctuation<<MissingSpace>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectMissingSpaceInEnglish() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>He is Bob.She is Alice</TITLE></BLIST>\r
            <BLIST><TITLE>The string e.g. .NET should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string (e.g. Node.js) should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>Take care to not report something before a comma or a dot MANIFEST.MF, P.Anno.</TITLE></BLIST>\r
            <BLIST><TITLE>The string 12.34 should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string ".He is bright" should be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string .He is bright should be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string (isn’t it?) should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>Two things: a and b.</TITLE></BLIST>\r
            <BLIST><TITLE>Hello!</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"He is Bob.She is Alice\" is missing a space after punctuation<<MissingSpace>>",
                 "\"The string .He is bright should be reported.\" is missing a space after punctuation<<MissingSpace>>",
                 "\"The string .He is bright should be reported.\" has a space before a punctuation (at index 11)<<SpuriousSpace>>",
                 "\"The string \".He is bright\" should be reported.\" is missing a space after punctuation<<MissingSpace>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBeforePunctuationInEnglish() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>He is Bob. She is Alice.</TITLE></BLIST>\r
            <BLIST><TITLE>He is Boby . She is Alicy.</TITLE></BLIST>\r
            <BLIST><TITLE>There are three animals: sheep, pig, and cock.</TITLE></BLIST>\r
            <BLIST><TITLE>There are three animals : sheep, pig, and cock.</TITLE></BLIST>\r
            <BLIST><TITLE>There are three animals: sheep , pig, and cock.</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"He is Boby . She is Alicy.\" has a space before a punctuation (at index 11)<<SpuriousSpace>>",
                 "\"There are three animals : sheep, pig, and cock.\" has a space before a punctuation (at index 24)<<SpuriousSpace>>",
                 "\"There are three animals: sheep , pig, and cock.\" has a space before a punctuation (at index 31)<<SpuriousSpace>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectMissingSpaceInFrench() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="fr">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>He is Bob.She is Alice</TITLE></BLIST>\r
            <BLIST><TITLE>The string e.g. .NET should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string (e.g. Node.js) should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>Take care to not report something before a comma or a dot MANIFEST.MF, P.Anno.</TITLE></BLIST>\r
            <BLIST><TITLE>The string 12.34 should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string ".He is bright" should be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string .He is bright should be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>The string (isn’t it?) should not be reported.</TITLE></BLIST>\r
            <BLIST><TITLE>Two things: a and b.</TITLE></BLIST>\r
            <BLIST><TITLE>Hello!</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"He is Bob.She is Alice\" is missing a space after punctuation<<MissingSpace>>",
                 "\"The string .He is bright should be reported.\" is missing a space after punctuation<<MissingSpace>>",
                 "\"The string .He is bright should be reported.\" has a space before a punctuation (at index 11)<<SpuriousSpace>>",
                 "\"The string \".He is bright\" should be reported.\" is missing a space after punctuation<<MissingSpace>>",
                 "\"Two things: a and b.\" is missing a space before punctuation<<MissingSpace>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBeforePunctuationInFrench() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="fr">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>He is Bob. She is Alice.</TITLE></BLIST>\r
            <BLIST><TITLE>He is Boby . She is Alicy.</TITLE></BLIST>\r
            <BLIST><TITLE>There are three animals: sheep, pig, and cock.</TITLE></BLIST>\r
            <BLIST><TITLE>There are three animals : sheep, pig, and cock.</TITLE></BLIST>\r
            <BLIST><TITLE>There are three animals: sheep , pig, and cock.</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"He is Boby . She is Alicy.\" has a space before a punctuation (at index 11)<<SpuriousSpace>>",
                 "\"There are three animals: sheep , pig, and cock.\" has a space before a punctuation (at index 31)<<SpuriousSpace>>",
                 "\"There are three animals: sheep , pig, and cock.\" is missing a space before punctuation<<MissingSpace>>",
                 "\"There are three animals: sheep, pig, and cock.\" is missing a space before punctuation<<MissingSpace>>");
        } catch (final SAXException _) {
            Assertions.fail("SAXException");
        }
    }
}
