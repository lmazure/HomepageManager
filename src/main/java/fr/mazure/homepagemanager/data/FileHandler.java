package fr.mazure.homepagemanager.data;
import java.nio.file.Path;

/**
 *
 */
public interface FileHandler {

    /**
     *
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
     * @param file
     */
    void handleCreation(final Path file);

    /**
     * @param file
     */
    void handleDeletion(final Path file);

    /**
     * @param file
     * @return
     */
    Path getOutputFile(final Path file);

    /**
     * @param file
     * @return
     */
    Path getReportFile(final Path file);

    /**
     * @param file
     * @return
     */
    boolean outputFileMustBeRegenerated(final Path file);
}
