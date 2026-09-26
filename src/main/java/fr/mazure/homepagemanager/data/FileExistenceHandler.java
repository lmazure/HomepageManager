package fr.mazure.homepagemanager.data;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * Track the creation and deletion of file
 */
public interface FileExistenceHandler {

    /**
     * Called when a file is created
     *
     * @param file file
     * @param modificationDateTime modification date and time of the file
     * @param size size of the file
     */
    void handleCreation(final Path file,
                        final FileTime modificationDateTime,
                        final long size);

    /**
     * Called when a file is deleted
     *
     * @param file file
     */
    void handleDeletion(final Path file);
}
