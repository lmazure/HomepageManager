package fr.mazure.homepagemanager.data;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.FileHandler.Status;

/**
 *
 */
public interface BackgroundDataController extends DataController {

    /**
     * @param file
     * @param status
     * @param outputFile
     * @param reportFile
     */
    void handleUpdate(final Path file,
                      final Status status,
                      final Path outputFile,
                      final Path reportFile);
}
