package ui;

import java.nio.file.Path;

import data.BackgroundDataController;
import data.FileHandler.Status;
import utils.QuadriConsumer;

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
