package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

/**
 * Tests of DoubleSpaceChecker class
 */
class DoubleSpaceCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void detectDoubleSpace() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Foo  bar</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 "\"Foo  bar\" should not contain a double space (in \"Foo  bar\")<<DoubleSpace>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceInArticleTitles() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>My articles</TITLE>\r
            <ITEM><ARTICLE><X><T>Fuz  baz</T><A>https://example.com/page</A><L>en</L><F>HTML</F></X><COMMENT>This is a comment.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceDueToNodes() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Foo <KEY id='Down'/> bar</TITLE></BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceDueToIndentationBetweenNodes() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <DEFINITIONTABLE>
              <ROW>
                <TERM><CODEROUTINE>foo bar</CODEROUTINE></TERM>
              </ROW>
              <DESC><BLIST><TITLE>Display</TITLE>\r
                  <ITEM>alpha</ITEM>\r
                  <ITEM>beta</ITEM>\r
                  <ITEM>gamma</ITEM>\r
                </BLIST></DESC>\r
            </DEFINITIONTABLE>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void ignoreDoubleSpaceDueToIndentationInsideNode() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <DEFINITIONTABLE>\r
              <ROW>\r
                <TERM><MODIFIERKEY id='Ctrl'/><KEY id='N'/><BR/>\r
                  double left click on tab menubar<BR/>\r
                  <CODEROUTINE>New Untitled File</CODEROUTINE></TERM>\r
                <DESC>open new empty editor</DESC>\r
              </ROW>\r
            </DEFINITIONTABLE>\r
            <U><B>Static Import</B></U><BR/>\r
              The static import construct allows unqualified access to static members without inheriting from the type containing the static members. Instead, the program imports the members, either individually:\r
                <CODESAMPLE>import static java.lang.Math.PI;<BR/>\r
                import static java.lang.Math.cos;</CODESAMPLE>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectDoubleSpaceInIndentation() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <DEFINITIONTABLE>\r
              <ROW>\r
                <TERM><MODIFIERKEY id='Ctrl'/><KEY id='N'/><BR/>\r
                  double left  click on tab menubar<BR/>\r
                  <CODEROUTINE>New Untitled File</CODEROUTINE></TERM>\r
                <DESC>open new empty editor</DESC>\r
              </ROW>\r
            </DEFINITIONTABLE>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 """
                    "
                          double left  click on tab menubar
                          New Untitled File" should not contain a double space (in "
                          double left  click on tab menubar")<<DoubleSpace>>""");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectDoubleSpaceAtBeginningOfSegment() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <CODEROUTINE>package name</CODEROUTINE>  remove the binaries of package the package\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 """
                    "
                    package name  remove the binaries of package the package
                    " should not contain a double space (in "  remove the binaries of package the package
                    ")<<DoubleSpace>>""");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void detectDoubleSpaceAtEndOfSegment() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\r
            <TITLE>Test</TITLE>\r
            <PATH>HomepageManager/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            remove the binaries of package the package  <CODEROUTINE>package name</CODEROUTINE>\r
            </CONTENT>\r
            </PAGE>""";

        try {
            test(content,
                 """
                    "
                    remove the binaries of package the package  package name
                    " should not contain a double space (in "
                    remove the binaries of package the package  ")<<DoubleSpace>>""");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
 }
