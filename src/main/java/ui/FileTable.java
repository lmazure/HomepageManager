package ui;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import data.DataOrchestrator;
import data.FileChecker;
import data.FileHandler;
import data.HTMLGenerator;
import data.LinkChecker;
import data.NodeValueChecker;
import data.SiteFilesGenerator;
import data.jsongenerator.JsonGenerator;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        final LinkCheckController linkCheckController = new LinkCheckController(_list);
        final LinkChecker linkCheckGenerator = new LinkChecker(_homepagePath, _tmpPath, linkCheckController);
        final List<FileHandler> fileHandlers = Arrays.asList(htmlFileGenerator,
                                                             fileCheckGenerator,
                                                             nodeValueCheckGenerator,
                                                             linkCheckGenerator);
        final List<GenericUiController> uiControllers = Arrays.asList(htmlFileController,
                                                                      fileCheckController,
                                                                      nodeCheckController,
                                                                      linkCheckController);

        stage.setTitle("Homepage Manager");
        stage.setWidth(1100);
        stage.setHeight(500);
 
        final BorderPane border = new BorderPane();
        border.setCenter(buildTable(uiControllers));
        border.setBottom(builtButtons());
        final Scene scene = new Scene(border);
        
        final Service<Void> calculateService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        final DataOrchestrator dataOrchestrator = new DataOrchestrator(_homepagePath, _list, fileHandlers);
                        dataOrchestrator.start();
                        return null;
                    }
                };
            }
        };
        calculateService.start();
        
        stage.setScene(scene);
        stage.show();
    }

    private TableView<ObservableFile> buildTable(final List<GenericUiController> uiControllers) {
        
        final TableView<ObservableFile> table = new TableView<ObservableFile>();
        
        final TableColumn<ObservableFile, String> fileColumn = new TableColumn<ObservableFile, String>("File");
        fileColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fileColumn.setPrefWidth(250);
        table.getColumns().add(fileColumn);
        
        final TableColumn<ObservableFile, Boolean> deletedColumn = new TableColumn<ObservableFile, Boolean>("Deleted");
        deletedColumn.setCellValueFactory(cellData -> cellData.getValue().deletedProperty());
        table.getColumns().add(deletedColumn);
        
        for (final GenericUiController uiController: uiControllers) {
            table.getColumns().add(uiController.getColumns());
        }
        
        table.setItems(_list.getObservableFileList());
        
        return table;
    }

    private HBox builtButtons() {
        
        final HBox buttonPanel = new HBox();
        
        final Button generateGlobalFileButton = new Button("Generate global files");
        generateGlobalFileButton.setOnAction(event -> generateGlobalFiles());
        buttonPanel.getChildren().add(generateGlobalFileButton);
        
        final Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> exit());
        buttonPanel.getChildren().add(quitButton);
        
        return buttonPanel;
    }

    public void display(final Path homepagePath,
                        final Path tmpPath) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _list = new ObservableFileList();
        launch();
    }
    
    private void generateGlobalFiles() {        
        SiteFilesGenerator.generate(_homepagePath, _list.getFileList());
        JsonGenerator.generate(_homepagePath, _list.getFileList());
    }
    
    private void exit() {
        System.exit(0);
    }
}
