import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.FileCheckController;
import ui.ObservableFileList;
import ui.HtmlFileController;
import ui.ObservableFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import data.DataOrchestrator;
import data.FileCheckGenerator;
import data.FileHandler;
import data.HTMLFileGenerator;
import utils.ExitHelper;

public class Main extends Application {

    static private Path _homepagePath;
    static private Path _tmpPath;
    static private ObservableFileList _list;
    static private HTMLFileGenerator _htmlFileGenerator;
    static private FileCheckGenerator _fileCheckGenerator;
    static private HtmlFileController _htmlFileController;
    static private FileCheckController _fileCheckController;
 
    private TableView<ObservableFile> _table;
    
    public Main() {
    }
    
    public static void main(final String[] args) {
        
        if (args.length != 2) {
            ExitHelper.exit("Syntax: HomepageManager <homepage directory> <tmp directory>");
        }
 
        _homepagePath = Paths.get(args[0]);
        _tmpPath = Paths.get(args[1]);
        _list = new ObservableFileList();
        _htmlFileController = new HtmlFileController(_list);
        _fileCheckController = new FileCheckController(_list);
        _htmlFileGenerator = new HTMLFileGenerator(_homepagePath, _tmpPath, _htmlFileController);
        _fileCheckGenerator = new FileCheckGenerator(_homepagePath, _tmpPath, _fileCheckController);

        launch(args);
    }
 
    @Override
    public void start(final Stage stage) {
        stage.setTitle("Homepage Manager");
        stage.setWidth(900);
        stage.setHeight(500);
 
        _table = new TableView<ObservableFile>();
        final TableColumn<ObservableFile, String> fileColumn = new TableColumn<ObservableFile, String>("File");
        fileColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        final TableColumn<ObservableFile, Boolean> deletedColumn = new TableColumn<ObservableFile, Boolean>("Deleted");
        deletedColumn.setCellValueFactory(cellData -> cellData.getValue().deletedProperty());
        _table.getColumns().add(fileColumn);
        _table.getColumns().add(deletedColumn);
        _table.getColumns().addAll(_fileCheckController.getColumns());
        _table.getColumns().addAll(_htmlFileController.getColumns());
        _table.setItems(_list.getObservableList());
 
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
                        final DataOrchestrator main = new DataOrchestrator(_homepagePath, _list, fileHandlers);
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