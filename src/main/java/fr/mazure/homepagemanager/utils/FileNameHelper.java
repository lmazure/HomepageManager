package fr.mazure.homepagemanager.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helpers to generate some file names
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
     * Generate a directory name from a host name
     *
     * @param hostname directory name
     * @return host name
     */
    public static String generateDirectoryNameFromHostName(final String hostname) {
        try {
            return URLEncoder.encode(hostname, StandardCharsets.UTF_8.toString());
        } catch (final UnsupportedEncodingException e) {
            ExitHelper.exit(e);
            // NOTREACHED
            return null;
        }
    }

    /**
     * Generate a file name from a URL
     *
     * @param url URL
     * @return file name
     */
    public static String generateFileNameFromURL(final String url) {

        final int MAX_FILENAME_LENGTH = 245;

        String s;
        try {
            s = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        } catch (final UnsupportedEncodingException e) {
            ExitHelper.exit(e);
            // NOTREACHED
            return null;
        }

        if (s.length() > MAX_FILENAME_LENGTH) {
            // avoid crash on Windows due to too long file name
            return s.substring(0, MAX_FILENAME_LENGTH - 9) + "_" + Integer.toHexString(s.hashCode());
        }

        return s;
    }
}
