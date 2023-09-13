package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.DataController;
import fr.mazure.homepagemanager.data.FileHandler.Status;
import fr.mazure.homepagemanager.utils.QuadriConsumer;
import javafx.scene.control.TableColumn;

/**
 *
 */
public abstract class GenericUiController implements DataController {

    private final QuadriConsumer<Path, Status, Path, Path> _callback;

    /**
     * @param callback
     */
    public GenericUiController(final QuadriConsumer<Path, Status, Path, Path> callback) {
        _callback = callback;
    }

    /**
     * @return
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

    protected void callCallback(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _callback.accept(file, status, outputFile, reportFile);
    }
}
