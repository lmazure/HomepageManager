package ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class HtmlFileController implements UiController {

    final private ObservableFileList _list;
    
    public HtmlFileController(final ObservableFileList list) {
        _list = list;
    }
    
    @Override
    public List<TableColumn<ObservableFile, ?>> getColumns() {
        
        final TableColumn<ObservableFile, String> htmlStatusColumn = new TableColumn<ObservableFile, String>("HTML");
        htmlStatusColumn.setCellValueFactory(cellData -> cellData.getValue().htmlFileProperty());
        htmlStatusColumn.setPrefWidth(150);
        
        final List<TableColumn<ObservableFile, ?>> list = new ArrayList<TableColumn<ObservableFile, ?>>();
        list.add(htmlStatusColumn);
        
        return list;
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
