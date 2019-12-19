package ui;

import java.nio.file.Path;

import data.DataController;
import data.FileHandler.Status;
import javafx.scene.control.TableColumn;
import utils.QuadriConsumer;

public abstract class UiController implements DataController {
    
    final private QuadriConsumer<Path, Status, Path, Path> _callback;
    
    public UiController(final QuadriConsumer<Path, Status, Path, Path> callback) {
        _callback = callback;
    }
    
    public abstract TableColumn<ObservableFile, ?> getColumns();
    
    @Override
    public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _callback.accept(file, status, outputFile, reportFile);
    }

    @Override
    public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _callback.accept(file, status, outputFile, reportFile);
    }
}
