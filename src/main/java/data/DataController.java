package data;

import java.nio.file.Path;

import data.FileHandler.Status;

/**
 * @author Laurent
 *
 */
public interface DataController {

    /**
     * @param file
     * @param status
     * @param outputFile
     * @param reportFile
     */
    public void handleCreation(final Path file,
                               final Status status,
                               final Path outputFile,
                               final Path reportFile);

    /**
     * @param file
     * @param status
     * @param outputFile
     * @param reportFile
     */
    public void handleDeletion(final Path file,
                               final Status status,
                               final Path outputFile,
                               final Path reportFile);
}
