package data;

import java.nio.file.Path;

import data.FileHandler.Status;

public interface DataController {

    public void handleCreation(final Path file, final Status status);

    public void handleDeletion(final Path file, final Status status);
}
