import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {
    
    static public void createAndTruncateFile(final Path file) {
        file.getParent().toFile().mkdirs();
        try (FileWriter writer = new FileWriter(file.toFile(), false)) {
            writer.flush();
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        System.out.println("created file " + file);
    }
    
    static public void deleteFile(final Path file) {
        if (file.toFile().exists()) {        
            try {
                Files.delete(file);
            } catch (final IOException e) {
                ExitHelper.exit(e);
            }
        }
        
        System.out.println("deleted file " + file);
    }
}
