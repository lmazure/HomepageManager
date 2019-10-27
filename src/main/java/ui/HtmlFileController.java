package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class HtmlFileController implements UiController {

    final private ObservableFileList _list;
    
    public HtmlFileController(final ObservableFileList list) {
        _list = list;
    }
    
    @Override
    public TableColumn<ObservableFile, ?> getColumns() {
        
        final TableColumn<ObservableFile, String> allColumns = new TableColumn<ObservableFile, String>("HTML");

        final TableColumn<ObservableFile, String> htmlStatusColumn = new TableColumn<ObservableFile, String>("HTML");
        htmlStatusColumn.setCellValueFactory(cellData -> cellData.getValue().htmlFileProperty());
        htmlStatusColumn.setPrefWidth(150);
        allColumns.getColumns().add(htmlStatusColumn);
        
        return allColumns;
    }
    
    @Override
    public void handleCreation(final Path file, final Status status) {
        _list.getFile(file).setHtmlFileStatus(status);
    }

    @Override
    public void handleDeletion(final Path file, final Status status) {
        _list.getFile(file).setHtmlFileStatus(status);
    }
}
