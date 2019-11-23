package ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.DataOrchestrator;
import data.FileChecker;
import data.FileHandler;
import data.HTMLGenerator;
import data.NodeValueChecker;
import data.RobottxtGenerator;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FileTable extends Application {

    static private Path _homepagePath;
    static private Path _tmpPath;
    static private ObservableFileList _list;

    @Override
    public void start(final Stage stage) {

        final HtmlGenerationController htmlFileController = new HtmlGenerationController(_list, _homepagePath);
        final HTMLGenerator htmlFileGenerator = new HTMLGenerator(_homepagePath, _tmpPath, htmlFileController);
        final FileCheckController fileCheckController = new FileCheckController(_list);
        final FileChecker fileCheckGenerator = new FileChecker(_homepagePath, _tmpPath, fileCheckController);
        final NodeValueCheckController nodeCheckController = new NodeValueCheckController(_list);
        final NodeValueChecker nodeValueCheckGenerator = new NodeValueChecker(_homepagePath, _tmpPath, nodeCheckController);

        stage.setTitle("Homepage Manager");
        stage.setWidth(1100);
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
        _table.getColumns().add(nodeCheckController.getColumns());
        _table.getColumns().add(htmlFileController.getColumns());
        _table.setItems(_list.getObservableFileList());
 
        final BorderPane border = new BorderPane();
        border.setCenter(_table);
        
        final Button globalFileGeneration = new Button("Generate global files");
        globalFileGeneration.setOnAction(event -> generateGlobalFiles());
        border.setBottom(globalFileGeneration);
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
                        fileHandlers.add(nodeValueCheckGenerator);
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

    public void display(final Path homepagePath,
                        final Path tmpPath) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _list = new ObservableFileList();
        launch();
    }
    
    private void generateGlobalFiles() {        
        RobottxtGenerator.generate(_homepagePath, _list.getFileList());
    }
}
