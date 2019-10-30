package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

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

        statusColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<ObservableFile, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(final TableColumn.CellDataFeatures<ObservableFile, String> p) {
                        return p.getValue().fileCheckProperty();
                    }
                });

        statusColumn.setCellFactory(
                new Callback<TableColumn<ObservableFile, String>, TableCell<ObservableFile, String>>() {
                    @Override
                    public TableCell<ObservableFile, String> call(final TableColumn<ObservableFile, String> p) {
                        return new ButtonCell<ObservableFile>(f -> displayLogFile(f));
                    }

                });
        
        allColumns.getColumns().add(statusColumn);

        return allColumns;
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
