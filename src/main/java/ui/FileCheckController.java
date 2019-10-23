package ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import data.FileHandler.Status;
import javafx.scene.control.Button;
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
        fileCheckColumn.setPrefWidth(150);

        final TableColumn<ObservableFile, Button> fileCheckColumn2 = new TableColumn<ObservableFile, Button>("Check2");
        fileCheckColumn2.setCellFactory(ActionButtonTableCell.<ObservableFile>forTableColumn(
            f -> (f.getFileCheckStatus() == null) ? Optional.empty()
                                                  : Optional.<String>of(f.getFileCheckStatus()),
            f -> displayLogFile(f)));
        fileCheckColumn2.setPrefWidth(170);
        
        final List<TableColumn<ObservableFile, ?>> list = new ArrayList<TableColumn<ObservableFile, ?>>();
        list.add(fileCheckColumn);
        list.add(fileCheckColumn2);
        
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
    
    static private void displayLogFile(final ObservableFile file) {
        System.out.println("display : " + file.getName());
    }
}
