package data;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public interface FileExistenceHandler {

    public void handleCreation(final Path file, final FileTime modificationDateTime, final long size);

    public void handleDeletion(final Path file);
}
