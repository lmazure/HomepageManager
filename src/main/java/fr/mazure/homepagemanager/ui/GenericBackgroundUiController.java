package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.BackgroundDataController;
import fr.mazure.homepagemanager.data.FileHandler.Status;
import fr.mazure.homepagemanager.utils.QuadriConsumer;

/**
*
*/
public abstract class GenericBackgroundUiController extends GenericUiController implements BackgroundDataController {

    /**
     * @param callback
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
