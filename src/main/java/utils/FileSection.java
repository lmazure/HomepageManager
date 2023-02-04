package utils;

import java.io.File;

/**
 * Section of a file
 * @param file The file
 * @param offset The offset of the first byte of the section
 * @param length The number of bytes of the section
 *
 */
public record FileSection(File file,
                          long offset,
                          long length) {
    //
}
