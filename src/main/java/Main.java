import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import data.DataOrchestrator;
import data.FileCheckGenerator;
import data.FileHandler;
import data.HTMLFileGenerator;
import data.TrackedFile;
import utils.ExitHelper;

public class Main extends Application {

    static private Path _homepagePath;
    static private Path _tmpPath;
    static private HTMLFileGenerator _htmlFileGenerator;
    static private FileCheckGenerator _fileCheckGenerator;
 
    private TableView<TrackedFile> _table;
    private final ObservableList<TrackedFile> _data;
    
    public Main() {
        _data = FXCollections.observableArrayList();
    }
    
    public static void main(final String[] args) {
        
        if (args.length != 2) {
            ExitHelper.exit("Syntax: HomepageManager <homepage directory> <tmp directory>");
        }
 
        _homepagePath = Paths.get(args[0]);
        _tmpPath = Paths.get(args[1]);
        _htmlFileGenerator = new HTMLFileGenerator(_homepagePath, _tmpPath);
        _fileCheckGenerator = new FileCheckGenerator(_homepagePath, _tmpPath);

        launch(args);
    }
 
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Homepage Manager");
        stage.setWidth(900);
        stage.setHeight(500);
 
        _table = new TableView<TrackedFile>();
        final TableColumn<TrackedFile, String> fileColumn = new TableColumn<TrackedFile, String>("File");
        fileColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        final TableColumn<TrackedFile, Boolean> deletedColumn = new TableColumn<TrackedFile, Boolean>("Deleted");
        deletedColumn.setCellValueFactory(cellData -> cellData.getValue().deletedProperty());
        final TableColumn<TrackedFile, String> htmlStatusColumn = new TableColumn<TrackedFile, String>("HTML");
        htmlStatusColumn.setCellValueFactory(cellData -> cellData.getValue().htmlFileProperty());
        final TableColumn<TrackedFile, String> fileCheckColumn = new TableColumn<TrackedFile, String>("Check");
        fileCheckColumn.setCellValueFactory(cellData -> cellData.getValue().fileCheckProperty());
        _table.getColumns().addAll(fileColumn, deletedColumn, htmlStatusColumn, fileCheckColumn);
        _table.setItems(_data);
 
        final BorderPane border = new BorderPane();
        border.setCenter(_table);
        final Scene scene = new Scene(border);
        
        final Service<Void> calculateService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        final List<FileHandler> fileHandlers = new ArrayList<FileHandler>();
                        fileHandlers.add(_htmlFileGenerator);
                        fileHandlers.add(_fileCheckGenerator);
                        final DataOrchestrator main = new DataOrchestrator(_homepagePath, _data, fileHandlers);
                        main.start();
                        return null;
                    }
                };
            }
        };
        calculateService.start();
        
        stage.setScene(scene);
        stage.show();
    }
}