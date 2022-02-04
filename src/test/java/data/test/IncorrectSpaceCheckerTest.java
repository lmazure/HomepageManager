package data.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class IncorrectSpaceCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void checkAllBasicCasesInEnglish() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Aab,xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab, xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ,xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab , xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab;xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab; xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ;xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ; xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab:xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab: xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab :xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab : xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab.xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab. xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab .xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab . xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab!xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab! xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab !xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ! xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab?xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab? xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ?xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ? xyz</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"Aab ! xyz\" has a space before a punctuation",
                 "\"Aab !xyz\" has a space before a punctuation",
                 "\"Aab !xyz\" is missing a space after punctuation",
                 "\"Aab , xyz\" has a space before a punctuation",
                 "\"Aab ,xyz\" has a space before a punctuation",
                 "\"Aab ,xyz\" is missing a space after punctuation",
                 "\"Aab . xyz\" has a space before a punctuation",
                 "\"Aab .xyz\" has a space before a punctuation",
                 "\"Aab .xyz\" is missing a space after punctuation",
                 "\"Aab : xyz\" has a space before a punctuation",
                 "\"Aab :xyz\" has a space before a punctuation",
                 "\"Aab :xyz\" is missing a space after punctuation",
                 "\"Aab ; xyz\" has a space before a punctuation",
                 "\"Aab ;xyz\" has a space before a punctuation",
                 "\"Aab ;xyz\" is missing a space after punctuation",
                 "\"Aab ? xyz\" has a space before a punctuation",
                 "\"Aab ?xyz\" has a space before a punctuation",
                 "\"Aab ?xyz\" is missing a space after punctuation",
                 "\"Aab!xyz\" is missing a space after punctuation",
                 "\"Aab,xyz\" is missing a space after punctuation",
                 "\"Aab.xyz\" is missing a space after punctuation",
                 "\"Aab:xyz\" is missing a space after punctuation",
                 "\"Aab;xyz\" is missing a space after punctuation",
                 "\"Aab?xyz\" is missing a space after punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }


    @SuppressWarnings("static-method")
    @Test
    void checkAllBasicCasesInFrench() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"fr\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>Aab,xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab, xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ,xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab , xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab;xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab; xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ;xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ; xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab:xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab: xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab :xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab : xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab.xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab. xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab .xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab . xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab!xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab! xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab !xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ! xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab?xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab? xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ?xyz</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Aab ? xyz</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"Aab !xyz\" is missing a space after punctuation",
                 "\"Aab , xyz\" has a space before a punctuation",
                 "\"Aab ,xyz\" has a space before a punctuation",
                 "\"Aab ,xyz\" is missing a space after punctuation",
                 "\"Aab . xyz\" has a space before a punctuation",
                 "\"Aab .xyz\" has a space before a punctuation",
                 "\"Aab .xyz\" is missing a space after punctuation",
                 "\"Aab :xyz\" is missing a space after punctuation",
                 "\"Aab ;xyz\" is missing a space after punctuation",
                 "\"Aab ?xyz\" is missing a space after punctuation",
                 "\"Aab!xyz\" is missing a space after punctuation",
                 "\"Aab,xyz\" is missing a space after punctuation",
                 "\"Aab.xyz\" is missing a space after punctuation",
                 "\"Aab: xyz\" is missing a space before punctuation",
                 "\"Aab:xyz\" is missing a space after punctuation",
                 "\"Aab:xyz\" is missing a space before punctuation",
                 "\"Aab; xyz\" is missing a space before punctuation",
                 "\"Aab;xyz\" is missing a space after punctuation",
                 "\"Aab;xyz\" is missing a space before punctuation",
                 "\"Aab?xyz\" is missing a space after punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectMissingSpaceInEnglish() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>He is Bob.She is Alice</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string e.g. .NET should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string (e.g. Node.js) should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Take care to not report something before a comma or a dot MANIFEST.MF, P.Anno.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string 12.34 should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string \".He is bright\" should be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string .He is bright should be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string (isn't it?) should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Two things: a and b.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Hello!</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"He is Bob.She is Alice\" is missing a space after punctuation",
                 "\"The string .He is bright should be reported.\" is missing a space after punctuation",
                 "\"The string .He is bright should be reported.\" has a space before a punctuation",
                 "\"The string \".He is bright\" should be reported.\" is missing a space after punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBeforePunctuationInEnglish() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"en\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>He is Bob. She is Alice.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>He is Boby . She is Alicy.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals: sheep, pig, and cock.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals : sheep, pig, and cock.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals: sheep , pig, and cock.</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"He is Boby . She is Alicy.\" has a space before a punctuation",
                 "\"There are three animals : sheep, pig, and cock.\" has a space before a punctuation",
                 "\"There are three animals: sheep , pig, and cock.\" has a space before a punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectMissingSpaceInFrench() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"fr\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>He is Bob.She is Alice</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string e.g. .NET should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string (e.g. Node.js) should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Take care to not report something before a comma or a dot MANIFEST.MF, P.Anno.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string 12.34 should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string \".He is bright\" should be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string .He is bright should be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>The string (isn't it?) should not be reported.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Two things: a and b.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>Hello!</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"He is Bob.She is Alice\" is missing a space after punctuation",
                 "\"The string .He is bright should be reported.\" is missing a space after punctuation",
                 "\"The string .He is bright should be reported.\" has a space before a punctuation",
                 "\"The string \".He is bright\" should be reported.\" is missing a space after punctuation",
                 "\"Two things: a and b.\" is missing a space before punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectSpaceBeforePunctuationInFrench() {

        final String content =
            "<?xml version=\"1.0\"?>\r\n" +
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" +
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\" xml:lang=\"fr\">\r\n" +
            "<TITLE>Test</TITLE>\r\n" +
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" +
            "<CONTENT>\r\n" +
            "<BLIST><TITLE>He is Bob. She is Alice.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>He is Boby . She is Alicy.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals: sheep, pig, and cock.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals : sheep, pig, and cock.</TITLE></BLIST>\r\n" +
            "<BLIST><TITLE>There are three animals: sheep , pig, and cock.</TITLE></BLIST>\r\n" +
            "</CONTENT>\r\n" +
            "</PAGE>";

        try {
            test(content,
                 "\"He is Boby . She is Alicy.\" has a space before a punctuation",
                 "\"There are three animals: sheep , pig, and cock.\" has a space before a punctuation",
                 "\"There are three animals: sheep , pig, and cock.\" is missing a space before punctuation",
                 "\"There are three animals: sheep, pig, and cock.\" is missing a space before punctuation");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
