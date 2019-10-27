import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import data.DataController;
import data.FileCheckGenerator;
import data.FileHandler.Status;

class FileCheckGeneratorTest {

    static private String MESS_BOM  = "file should not have a UTF BOM";
    static private String MESS_CTRL = "line contains a control character";
    static private String MESS_WTSP = "line is finishing with a white space";
    static private String MESS_PATH = "the name of the file does not appear in the <PATH> node (expected to see \"<PATH>HomepageManager/test.xml</PATH>\")";
    static private String MESS_CRLF = "line should finish by \\r\\n instead of \\n";
    static private String MESS_EMPT = "empty line";
    
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

    @Test
    void testBomDetection() {
        
        final String content =
            "\uFEFF<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             1, MESS_BOM);
    }


    @Test
    void testTabDetectionAtBeginning() {
        
        final String content =
            "\t<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             1, MESS_CTRL);
    }
    
    @Test
    void testTabDetectionInMiddle() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "\t<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\t\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             5, MESS_CTRL,
             6, MESS_CTRL,
             6, MESS_WTSP);
    }
    
    @Test
    void testTabDetectionAtEnd() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>\t";
        
        test(content,
             9, MESS_CTRL,
             9, MESS_WTSP);
    }

    @Test
    void testSpaceDetectionAtBeginning() {
        
        final String content =
            "<?xml version=\"1.0\"?> \r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             1, MESS_WTSP);
    }
    
    @Test
    void testSpaceDetectionInMiddle() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH> \r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE> \r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             5, MESS_WTSP,
             6, MESS_WTSP);
    }
    
    @Test
    void testSpaceDetectionAtEnd() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE> ";
        
        test(content,
             9, MESS_WTSP);
    }

    @Test
    void testIncorrectPath() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/wrong.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             5, MESS_PATH);
    }
    
    @Test
    void testMissingCarriageReturn() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             4, MESS_CRLF);
    }
 
    @Test
    void testEmptyLineDetectionAtBeginning() {
        
        final String content =
            "\r\n" +
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             1, MESS_EMPT);
    }
    
    @Test
    void testEmptyLineDetectionInMiddle() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            "\r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             6, MESS_EMPT);
    }
    
    @Test
    void testEmptyLineDetectionAtEnd() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>\r\n";
        
        test(content,
             10, MESS_EMPT);
    }

    @Test
    void testWhiteLineDetectionAtBeginning() {
        
        final String content =
            " \r\n" +
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             1, MESS_EMPT,
             1, MESS_WTSP);
    }
    
    @Test
    void tesWhiteLineDetectionInMiddle() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" +
            " \r\n" +
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>";
        
        test(content,
             6, MESS_EMPT,
             6, MESS_WTSP);
    }
    
    @Test
    void testWhiteLineDetectionAtEnd() {
        
        final String content =
            "<?xml version=\"1.0\"?>\r\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\r\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\r\n" + 
            "<TITLE>test</TITLE>\r\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\r\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r\n" + 
            "<CONTENT>\r\n" + 
            "</CONTENT>\r\n" + 
            "</PAGE>\r\n" +
            " ";
        
        test(content,
             10, MESS_EMPT,
             10, MESS_WTSP);
    }
    static private void test(final String content) {

        final List<FileCheckGenerator.Error> expected= new ArrayList<FileCheckGenerator.Error>();        
        test(content, expected);
    }

    static private void test(final String content,
                             final int line0, final String message0) {

        final List<FileCheckGenerator.Error> expected= new ArrayList<FileCheckGenerator.Error>();
        expected.add(new FileCheckGenerator.Error(line0, message0));
        test(content, expected);
    }

    static private void test(final String content,
                             final int line0, final String message0,
                             final int line1, final String message1) {
        
        final List<FileCheckGenerator.Error> expected= new ArrayList<FileCheckGenerator.Error>();
        expected.add(new FileCheckGenerator.Error(line0, message0));
        expected.add(new FileCheckGenerator.Error(line1, message1));
        test(content, expected);
    }

    static private void test(final String content,
            final int line0, final String message0,
            final int line1, final String message1,
            final int line2, final String message2) {

        final List<FileCheckGenerator.Error> expected= new ArrayList<FileCheckGenerator.Error>();
        expected.add(new FileCheckGenerator.Error(line0, message0));
        expected.add(new FileCheckGenerator.Error(line1, message1));
        expected.add(new FileCheckGenerator.Error(line2, message2));
        test(content, expected);
    }

    static private void test(final String content,
                             final List<FileCheckGenerator.Error> expected) {

        final FileCheckGenerator gen = new FileCheckGenerator(Paths.get("home"), Paths.get("tmp"), new DummyDataController());
        final List<FileCheckGenerator.Error> effective = gen.check(Paths.get("test.xml"), content);
        
        assertEquals(normalize(expected), normalize(effective));
    }
    
    static private String normalize(final List<FileCheckGenerator.Error> errors) {
        return errors.stream()
                     .map(e -> String.format("%02d %s", e.getLineNumber(), e.getErrorMessage()))
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }
    
    static private class DummyDataController implements DataController {

        @Override
        public void handleCreation(Path file, Status status) {
            // do noting
        }

        @Override
        public void handleDeletion(Path file, Status status) {
            // do noting
        }
        
    }
}
