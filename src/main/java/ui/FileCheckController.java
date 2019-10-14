package ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class FileCheckController implements UiController {
    
    final private ObservableFileList _list;
    
    public FileCheckController(final ObservableFileList list) {
        _list = list;
    }
    
    @Override
    public List<TableColumn<ObservableFile, ?>> getColumns() {
        
        final TableColumn<ObservableFile, String> fileCheckColumn = new TableColumn<ObservableFile, String>("Check");
        fileCheckColumn.setCellValueFactory(cellData -> cellData.getValue().fileCheckProperty());
        
        final List<TableColumn<ObservableFile, ?>> list = new ArrayList<TableColumn<ObservableFile, ?>>();
        list.add(fileCheckColumn);
        
        return list;
    }
 
    @Override
    public void handleCreation(final Path file, final Status status) {
        _list.getFile(file).setFileCheckStatus(status);
    }

    @Override
    public void handleDeletion(final Path file, final Status status) {
        _list.getFile(file).setFileCheckStatus(status);
    }
}
