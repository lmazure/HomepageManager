package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.BackgroundDataController;
import fr.mazure.homepagemanager.data.FileHandler.Status;
import fr.mazure.homepagemanager.utils.QuadriConsumer;

/**
 * Base class of the controllers managing the columns of a check performed in the background
 */
public abstract class GenericBackgroundUiController extends GenericUiController implements BackgroundDataController {

    /**
     * Constructor
     *
     * @param callback callback executed when the status of the check of a file is updated
     */
    public GenericBackgroundUiController(final QuadriConsumer<Path, Status, Path, Path> callback) {
        super(callback);
    }

    @Override
    public void handleUpdate(final Path file,
                             final Status status,
                             final Path outputFile,
                             final Path reportFile) {
        callCallback(file, status, outputFile, reportFile);
    }
}
