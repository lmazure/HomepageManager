import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class FileCheckGeneratorTest {

    @Test
    void testNoError() {
        
        final String content =
            "<?xml version=\"1.0\"?>\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\n" + 
            "<TITLE>test</TITLE>\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\n" + 
            "<CONTENT>\n" + 
            "</CONTENT>\n" + 
            "</PAGE>";
        
        test(content);
    }

    @Test
    void testBomDetection() {
        
        final String content =
            "\uFEFF<?xml version=\"1.0\"?>\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\n" + 
            "<TITLE>test</TITLE>\n" + 
            "<PATH>HomepageManager/test.xml</PATH>\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\n" + 
            "<CONTENT>\n" + 
            "</CONTENT>\n" + 
            "</PAGE>";
        
        test(content,
             1, "file should not have a UTF BOM");
    }

    @Test
    void testIncorrectPath() {
        
        final String content =
            "<?xml version=\"1.0\"?>\n" + 
            "<?xml-stylesheet type=\"text/xsl\" href=\"../css/strict.xsl\"?>\n" + 
            "<PAGE xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"../css/schema.xsd\">\n" + 
            "<TITLE>test</TITLE>\n" + 
            "<PATH>HomepageManager/wrong.xml</PATH>\n" + 
            "<DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\n" + 
            "<CONTENT>\n" + 
            "</CONTENT>\n" + 
            "</PAGE>";
        
        test(content,
             5, "the name of the file does not appear in the <PATH> node");
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
                             final List<FileCheckGenerator.Error> expected) {

        final FileCheckGenerator gen = new FileCheckGenerator(Paths.get("home"), Paths.get("tmp"));
        final List<FileCheckGenerator.Error> effective = gen.check(Paths.get("test.xml"), content);
        
        assertEquals(normalize(expected), normalize(effective));
    }
    
    static private String normalize(final List<FileCheckGenerator.Error> errors) {
        return errors.stream()
                     .map(e -> String.format("%02d %s", e.getLineNumber(), e.getErrorMessage()))
                     .sorted()
                     .collect(Collectors.joining("\n"));
    }
}
