package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.DataController;
import fr.mazure.homepagemanager.data.FileHandler.Status;
import fr.mazure.homepagemanager.utils.QuadriConsumer;
import javafx.scene.control.TableColumn;

/**
 * Base class of the controllers managing the columns of a check
 */
public abstract class GenericUiController implements DataController {

    private final QuadriConsumer<Path, Status, Path, Path> _callback;

    /**
     * Constructor
     *
     * @param callback callback executed when the status of the check of a file is updated
     */
    public GenericUiController(final QuadriConsumer<Path, Status, Path, Path> callback) {
        _callback = callback;
    }

    /**
     * @return the columns to be added to the file table
     */
    public abstract TableColumn<ObservableFile, ?> getColumns();

    @Override
    public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        callCallback(file, status, outputFile, reportFile);
    }

    @Override
    public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        callCallback(file, status, outputFile, reportFile);
    }

    /**
     * Call the callback
     *
     * @param file file
     * @param status status of the checks
     * @param outputFile report of the checks
     * @param reportFile error report of the checks
     */
    protected void callCallback(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _callback.accept(file, status, outputFile, reportFile);
    }
}
