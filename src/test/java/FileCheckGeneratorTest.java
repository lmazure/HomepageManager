import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

class FileCheckGeneratorTest {

    @Test
    void testBomDetection() {
        
        final FileCheckGenerator gen = new FileCheckGenerator(Paths.get(""), Paths.get(""));
        final String content = "\uFEFF<CONTENT>\n"
                             + "</CONTENT>";
        final List<FileCheckGenerator.Error> errors = gen.check(Paths.get("test.xml"), content);
        
        assertEquals(2, errors.size());
        assertEquals(1, errors.get(0).getLineNumber());
        assertEquals("file should not have a UTF BOM", errors.get(0).getErrorMessage());
        assertEquals(0, errors.get(1).getLineNumber());
        assertEquals("the name of the file does not appear in the <PATH> node", errors.get(1).getErrorMessage());
    }
}
