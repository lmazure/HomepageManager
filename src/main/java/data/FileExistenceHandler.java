package data;

import java.nio.file.Path;

public interface FileExistenceHandler {

    public void handleCreation(final Path file);

    public void handleDeletion(final Path file);
}
