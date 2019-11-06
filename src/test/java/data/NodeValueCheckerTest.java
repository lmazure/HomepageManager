package data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import data.FileHandler.Status;

class NodeValueCheckerTest {

    @Test
    void testNoError() {
        
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
        
        test(content);
    }
    
    static private void test(final String content) {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();        
        test(content, expected);
    }

    /*static private void test(final String content,
                             final String detail0) {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        test(content, expected);
    }*/

    /*static private void test(final String content,
                             final String detail0,
                             final String detail1) {
        
        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail1));
        test(content, expected);
    }*/

    /*static private void test(final String content,
                             final String detail0,
                             final String detail1,
                             final String detail2) {

        final List<NodeValueChecker.Error> expected= new ArrayList<NodeValueChecker.Error>();
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail0));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail1));
        expected.add(new NodeValueChecker.Error("tag", "value", "violation", detail2));
        test(content, expected);
    }*/
    
    static private void test(final String content,
                             final List<NodeValueChecker.Error> expected) {

        final NodeValueChecker checker = new NodeValueChecker(Paths.get("home"), Paths.get("tmp"), new DummyDataController());
        final List<NodeValueChecker.Error> effective = checker.check(Paths.get("test.xml"));
    
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
