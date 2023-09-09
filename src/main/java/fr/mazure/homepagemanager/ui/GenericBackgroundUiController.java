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
    public GenericBackgroundUiController(QuadriConsumer<Path, Status, Path, Path> callback) {
        super(callback);
    }

    @Override
    public void handleUpdate(Path file, Status status, Path outputFile, Path reportFile) {
        callCallback(file, status, outputFile, reportFile);
    }
}
