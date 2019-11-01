package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class FileCheckController implements UiController {
    
    final private ObservableFileList _list;
    
    public FileCheckController(final ObservableFileList list) {
        _list = list;
    }
    
    @Override
    public TableColumn<ObservableFile, ?> getColumns() {
        
        final TableColumn<ObservableFile, String> allColumns = new TableColumn<ObservableFile, String>("Check");
        
        TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(170);
        statusColumn.setCellValueFactory(f -> f.getValue().fileCheckProperty());
        statusColumn.setCellFactory(p -> { return new ButtonCell<ObservableFile>(f -> ActionHelper.displayFile(f.getFileCheckReportFile()));});
        
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
 
    @Override
    public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _list.getFile(file).setFileCheckStatus(status, outputFile, reportFile);
    }

    @Override
    public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _list.getFile(file).setFileCheckStatus(status, outputFile, reportFile);
    }
}
