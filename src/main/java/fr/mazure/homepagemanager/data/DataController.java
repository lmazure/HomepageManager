package fr.mazure.homepagemanager.data;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.FileHandler.Status;

/**
 * Track the checks following the creation and deletion of file
 */
public interface DataController {

    /**
     * Called when a fle is created
     * @param file file
     * @param status status of the checks
     * @param outputFile report of the checks
     * @param reportFile error report of the checks
     */
    public void handleCreation(final Path file,
                               final Status status,
                               final Path outputFile,
                               final Path reportFile);

    /**
     * Called when a fle is delete
     * @param file file
     * @param status status of the checks
     * @param outputFile report of the checks
     * @param reportFile error report of the checks
     */
    public void handleDeletion(final Path file,
                               final Status status,
                               final Path outputFile,
                               final Path reportFile);
}
