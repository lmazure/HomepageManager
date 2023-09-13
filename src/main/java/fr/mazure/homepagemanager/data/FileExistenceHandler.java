package fr.mazure.homepagemanager.data;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 *
 */
public interface FileExistenceHandler {

    /**
     * @param file
     * @param modificationDateTime
     * @param size
     */
    void handleCreation(final Path file,
                        final FileTime modificationDateTime,
                        final long size);

    /**
     * @param file
     */
    void handleDeletion(final Path file);
}
