package data.test;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import data.DataController;
import data.FileHandler.Status;
import data.NodeValueChecker;

class NodeValueCheckerTest {

    @Test
    void noError() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>Test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            fail("SAXException");
        }
    }

    @Test
    void detectLowercaseTitle() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";

        try {        
            test(content,
                 "TITLE \"test\" must start with an uppercase");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            fail("SAXException");
        }
    }


    @Test
    void detectTitleEndingWithColon() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>Test:</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        try {        
            test(content,
                 "TITLE \"Test:\" must not finish with colon");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            fail("SAXException");
        }
    }

    @Test
    void detectDoubleSpace() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>Test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "<BLIST><TITLE>Foo  bar</TITLE></BLIST>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        try {        
            test(content,
                 "\"Foo  bar\" should not contain a double space");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            fail("SAXException");
        }
    }

    @Test
    void ignoreDoubleSpaceDueToNodes() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>Test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "<BLIST><TITLE>Foo <KEY ID='Down'/> bar</TITLE></BLIST>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        try {        
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            fail("SAXException");
        }
    }
    
    static private void test(final String content) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();        
        test(content, expected);
    }

    static private void test(final String content,
                             final String detail0) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        test(content, expected);
    }

    /*static private void test(final String content,
                             final String detail0,
                             final String detail1) throws SAXException {
        
        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail1));
        test(content, expected);
    }*/

    /*static private void test(final String content,
                             final String detail0,
                             final String detail1,
                             final String detail2) throws SAXException {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail1));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail2));
        test(content, expected);
    }*/
    
    static private void test(final String content,
                             final List<NodeValueChecker.Error> expected) throws SAXException {

        final NodeValueChecker checker = new NodeValueChecker(Paths.get("home"), Paths.get("tmp"), new DummyDataController());
        final List<NodeValueChecker.Error> effective = checker.check(Paths.get("test.xml"), content);
    
        assertEquals(normalize(expected), normalize(effective));
    }
    
    static private String normalize(final List<NodeValueChecker.Error> errors) {
        return errors.stream()
                     .map(e -> e.getDetail())
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }
    
    static private class DummyDataController implements DataController {
    
        @Override
        public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do noting
        }
        
        @Override
        public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
            // do noting
        }
    }
}
