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

    private static final int s_max_filename_length = 245;

    /**
     * Generate a new name from a file sourceFile which is in a directory sourceDirectory
     * The new name is in directory targetDirectory has the same name as the sourceFile except
     * with a suffix suffix and an extension extension
     *
     * @param sourceDirectory directory where is the source file
     * @param targetDirectory target where to put the new file
     * @param sourceFile name of source file
     * @param suffix suffix to add to the new file name
     * @param extension extension to add to the new file name
     * @return name of the new file
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
     * @param prefix filename prefix
     * @param url URL
     *
     * @return filename
     */
    public static String generateFileNameFromURL(final String prefix,
                                                 final String url) {

        String s;
        try {
            s = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        } catch (final UnsupportedEncodingException e) {
            ExitHelper.exit(e);
            // NOTREACHED
            return null;
        }

        final int prefixLength = prefix.length();
        if ((prefixLength + s.length()) > s_max_filename_length) {
            // avoid crash on Windows due to too long file name
            return prefix + s.substring(0, s_max_filename_length - (prefixLength + 9)) + "_" + Integer.toHexString(s.hashCode());
        }

        return s;
    }
}
