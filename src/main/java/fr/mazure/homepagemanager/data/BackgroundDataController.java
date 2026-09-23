package fr.mazure.homepagemanager.data;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.FileHandler.Status;

/**
 * Track the checks performed in the background following the creation and deletion of file
 */
public interface BackgroundDataController extends DataController {

    /**
     * Called when the check of a file is updated
     *
     * @param file file
     * @param status status of the checks
     * @param outputFile report of the checks
     * @param reportFile error report of the checks
     */
    void handleUpdate(final Path file,
                      final Status status,
                      final Path outputFile,
                      final Path reportFile);
}
