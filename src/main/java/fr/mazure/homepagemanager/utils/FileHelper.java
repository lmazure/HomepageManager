package fr.mazure.homepagemanager.utils;

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

/**
 * Tools for managing files
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
            return decoder.decode(byteBuffer).toString();
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
        return slurpFileSection(new FileSection(file, 0, file.length()), charset);
    }

    /**
     * Return the whole content of a file (charset is UTF8)
     *
     * @param file file to read
     * @return content of the file
     */
    public static String slurpFile(final File file) {
        return slurpFile(file, StandardCharsets.UTF_8);
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
     * Write a file (charset is UTF8)
     * @param file File to write
     * @param content Content of the file
     */
    public static void writeFile(final Path file,
                                 final String content) {
        try {
            Files.writeString(file, content, StandardCharsets.UTF_8);
        } catch (final IOException e) {
            ExitHelper.exit("Failed to write file", e);
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
     * delete a directory and (recursively) its content
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
     * delete simply a file, stop the program if failure
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
}
