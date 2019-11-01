package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class HtmlGenerationController implements UiController {

    final private ObservableFileList _list;
    
    public HtmlGenerationController(final ObservableFileList list) {
        _list = list;
    }
    
    @Override
    public TableColumn<ObservableFile, ?> getColumns() {
        
        final TableColumn<ObservableFile, String> allColumns = new TableColumn<ObservableFile, String>("HTML");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(70);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<ObservableFile>("display", f -> ActionHelper.displayFile(f.getHtmlFileOuputFile()));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(170);
        statusColumn.setCellValueFactory(f -> f.getValue().htmlGenerationProperty());
        statusColumn.setCellFactory(p -> { return new UpdatableButtonCell<ObservableFile>(f -> ActionHelper.displayFile(f.getHtmlFileReportFile()));});
        allColumns.getColumns().add(statusColumn);
        
        return allColumns;
    }
    
    @Override
    public void handleCreation(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _list.getFile(file).setHtmlGenerationStatus(status, outputFile, reportFile);
    }

    @Override
    public void handleDeletion(final Path file, final Status status, final Path outputFile, final Path reportFile) {
        _list.getFile(file).setHtmlGenerationStatus(status, outputFile, reportFile);
    }
}
