package data;

import java.nio.file.Path;

import data.FileHandler.Status;

public interface DataController {

    public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile);

    public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile);
}
