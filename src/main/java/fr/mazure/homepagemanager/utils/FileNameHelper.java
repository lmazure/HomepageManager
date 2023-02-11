package fr.mazure.homepagemanager.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helpers fo generate some file names
 */
public class FileNameHelper {
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
    public static Path computeTargetFile(final Path sourceDirectory,
                                         final Path targetDirectory,
                                         final Path sourceFile,
                                         final String suffix,
                                         final String extension) {
        final Path relativePath = sourceDirectory.relativize(sourceFile);
        final Path reportFilePath = targetDirectory.resolve(relativePath);
        final String s = reportFilePath.toString();
        return Paths.get(s.substring(0, s.lastIndexOf('.')).concat(suffix + "." + extension));
   }

    /**
     * @param url URL
     * @return file name
     */
    public static String generateFileNameFromURL(final String url) {

        final int MAX_FILENAME_LENGTH = 245;

        final String s = url.replaceFirst("://", "→")
                            .replaceAll(" ", "%20")
                            .replaceAll("/", "%2F")
                            .replaceAll(":", "%3A")
                            .replaceAll("\\?", "%3F");

        if (s.length() > MAX_FILENAME_LENGTH) {
            // avoid crash on Windows due to too long file name
            return s.substring(0, MAX_FILENAME_LENGTH - 9) + "_" + Integer.toHexString(s.hashCode());
        }

        return s;
    }
}
