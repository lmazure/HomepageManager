package utils;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tools for managing files
 *
 */
public class FileHelper {

    /**
     * create parent directory of the file
     * 
     * @param file
     */
    static public void createParentDirectory(final Path file) {
        file.getParent().toFile().mkdirs();
    }
    
    /**
     * delete the file
     * 
     * @param file
     */
    static public void deleteFile(final Path file) {
        if (file.toFile().exists()) {
            try {
                Files.delete(file);
                if (file.toFile().exists()) {
                    //System.out.println("Deleting file " + file + " - Argh! The file is still there!");
                } else {
                    //System.out.println("Deleting file " + file + " - The file is effectively deleted.");
                }
            } catch (final IOException e) {
                ExitHelper.exit(e);
            }
        } else {
            //System.out.println("Deleting file " + file + " - Nothing to do, the file does not exist.");
        }        
    }
    
    /**
     * Generate a new name from a file sourceFile which is in a directory sourceDirectory
     * The new name is in directory targetDirectory has the same name as the sourceFile except
     * with a suffix suffix and an extension extension
     * 
     * @param sourceDirectory
     * @param targetDirectory
     * @param sourceFile
     * @param suffix
     * @param extension
     * @return
     */
    static public Path computeTargetFile(final Path sourceDirectory,
                                         final Path targetDirectory,
                                         final Path sourceFile,
                                         final String suffix,
                                         final String extension) {
        final Path relativePath = sourceDirectory.relativize(sourceFile);
        final Path reportFilePath = targetDirectory.resolve(relativePath);
        final String s = reportFilePath.toString();
        return Paths.get(s.substring(0, s.lastIndexOf('.')).concat(suffix + "." + extension));     
   }
    
    static public String generateFileNameFromURL(final URL url) {
        
        final int MAX_FILENAME_LENGTH = 255;
        
        String s = url.toString()
                      .replaceFirst("://", "→")
                      .replaceAll(" ", "%20")
                      .replaceAll("/", "%2F")
                      .replaceAll(":", "%3A")
                      .replaceAll("\\?", "%3F");
        
        if (s.length() > MAX_FILENAME_LENGTH) {
            s = s.substring(0, MAX_FILENAME_LENGTH - 9) + "_" + Integer.toHexString(s.hashCode()); // avoid crash on Windows due to too long file name
        }

        return s;
    }
}
