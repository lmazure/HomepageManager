package utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tools for managing files
 *
 */
public class FileHelper {

    /**
     * Return the content of a section of a file
     *
     * @param fileSection Section of a file to read
     * @param charset encoding of the file
     * @return content of the file
     */
    public static String slurpFileSection(final FileSection fileSection,
                                          final Charset charset) {
        final CharsetDecoder decoder = charset.newDecoder();
        if (charset.equals(StandardCharsets.UTF_8)) {
            decoder.onMalformedInput(CodingErrorAction.REPLACE);
        }

        try (final RandomAccessFile reader = new RandomAccessFile(fileSection.file(), "r");
             final FileChannel channel = reader.getChannel()){
            final ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileSection.length());
            channel.position(fileSection.offset());
            channel.read(byteBuffer);
            byteBuffer.flip();
            final String string = decoder.decode(byteBuffer).toString();
            return string;
        } catch (final IOException e) {
            ExitHelper.exit("Failed to slurp file", e);
            // NOT REACHED
            return null;
        }
    }

    /**
     * Return the whole content of a file
     *
     * @param file file to read
     * @param charset encoding of the file
     * @return content of the file
     */
    public static String slurpFile(final File file,
                                   final Charset charset) {
        return FileHelper.slurpFileSection(new FileSection(file, 0, file.length()), charset);
    }

    /**
     * @param file file
     * @return canonical path of the file
     */
    public static String getCanonicalPath(final File file) {
        try {
            return file.getCanonicalPath();
        } catch (final IOException e) {
            ExitHelper.exit("Failed to get canonical path", e);
            // NOT REACHED
            return null;
        }
    }

    /**
     * create parent directory of the file
     *
     * @param file file whose directory is to be created
     */
    public static void createParentDirectory(final Path file) {

        final File parentDir = file.getParent().toFile();

        if (parentDir.isDirectory()) {
            return;
        }

        if (!parentDir.mkdirs()) {
            ExitHelper.exit("Failed to create directory " + parentDir);
        }
    }

    /**
     * Delete a directory and (recursively) its content
     *
     * @param directory directory to be deleted
     */
    public static void deleteDirectory(final File directory) {
        final File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (final File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    /**
     * delete a file
     *
     * @param file file to be deleted
     */
    public static void deleteFile(final Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (final IOException e) {
            ExitHelper.exit(e);
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
