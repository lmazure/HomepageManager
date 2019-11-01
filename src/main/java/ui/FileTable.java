package ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.DataOrchestrator;
import data.FileChecker;
import data.FileHandler;
import data.HTMLGenerator;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileTable extends Application {

    static private Path _homepagePath;
    static private Path _tmpPath;

    @Override
    public void start(final Stage stage) {

        final ObservableFileList list = new ObservableFileList();
        final HtmlGenerationController htmlFileController = new HtmlGenerationController(list);
        final FileCheckController fileCheckController = new FileCheckController(list);
        final HTMLGenerator htmlFileGenerator = new HTMLGenerator(_homepagePath, _tmpPath, htmlFileController);
        final FileChecker fileCheckGenerator = new FileChecker(_homepagePath, _tmpPath, fileCheckController);

        stage.setTitle("Homepage Manager");
        stage.setWidth(900);
        stage.setHeight(500);
 
        final TableView<ObservableFile> _table = new TableView<ObservableFile>();
        final TableColumn<ObservableFile, String> fileColumn = new TableColumn<ObservableFile, String>("File");
        fileColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fileColumn.setPrefWidth(250);
        final TableColumn<ObservableFile, Boolean> deletedColumn = new TableColumn<ObservableFile, Boolean>("Deleted");
        deletedColumn.setCellValueFactory(cellData -> cellData.getValue().deletedProperty());
        _table.getColumns().add(fileColumn);
        _table.getColumns().add(deletedColumn);
        _table.getColumns().add(fileCheckController.getColumns());
        _table.getColumns().add(htmlFileController.getColumns());
        _table.setItems(list.getObservableList());
 
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
                        fileHandlers.add(htmlFileGenerator);
                        fileHandlers.add(fileCheckGenerator);
                        final DataOrchestrator main = new DataOrchestrator(_homepagePath, list, fileHandlers);
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

    public void display(final Path homepagePath, final Path tmpPath) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        launch();
    }
}
