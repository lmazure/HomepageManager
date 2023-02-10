package fr.mazure.homepagemanager.data;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

/**
 * @author Laurent
 *
 */
public interface FileExistenceHandler {

    /**
     * @param file
     * @param modificationDateTime
     * @param size
     */
    public void handleCreation(final Path file,
                               final FileTime modificationDateTime,
                               final long size);

    /**
     * @param file
     */
    public void handleDeletion(final Path file);
}
