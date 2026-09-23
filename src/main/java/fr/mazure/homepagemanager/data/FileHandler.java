package fr.mazure.homepagemanager.data;
import java.nio.file.Path;

/**
 * Handle the processing of a file
 */
public interface FileHandler {

    /**
     * Status of the file handling
     */
    public enum Status {
        /**
         * the file is still being handled, not error yet
         */
        HANDLING_NO_ERROR,
        /**
         * the file is still being handled, an error already occurred
         */
        HANDLING_WITH_ERROR,
        /**
         * the file handling was done successfully
         */
        HANDLED_WITH_SUCCESS,
        /**
         * the file handling was done with error(s)
         */
        HANDLED_WITH_ERROR,
        /**
         * the file could not be handled
         */
        FAILED_TO_HANDLE
    }

    /**
     * Called when a file is created
     *
     * @param file file
     */
    void handleCreation(final Path file);

    /**
     * Called when a file is deleted
     *
     * @param file file
     */
    void handleDeletion(final Path file);

    /**
     * @param file file
     * @return path of the output file
     */
    Path getOutputFile(final Path file);

    /**
     * @param file file
     * @return path of the report file
     */
    Path getReportFile(final Path file);

    /**
     * @param file file
     * @return true if the output file must be regenerated, false otherwise
     */
    boolean outputFileMustBeRegenerated(final Path file);
}
