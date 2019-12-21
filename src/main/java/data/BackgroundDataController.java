package data;

import java.nio.file.Path;

import data.FileHandler.Status;

public interface BackgroundDataController extends DataController {

    public void handleUpdate(final Path file, final Status status, final Path outputFile, final Path reportFile);
}
