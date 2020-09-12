package ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.FileEventDispachter;
import data.FileChecker;
import data.FileHandler;
import data.HTMLGenerator;
import data.LinkChecker;
import data.MetricsExtractor;
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

    private static Path _homepagePath;
    private static Path _tmpPath;
    private static boolean _internetAccessiSEnabled;
    private static ObservableFileList _list;

    @Override
    public void start(final Stage stage) {

        final List<GenericUiController> uiControllers = new ArrayList<GenericUiController>();
        final List<FileHandler> fileHandlers= new ArrayList<FileHandler>();
        
        final HtmlGenerationController htmlFileController = new HtmlGenerationController(_list, _homepagePath);
        final HTMLGenerator htmlFileGenerator = new HTMLGenerator(_homepagePath, _tmpPath, htmlFileController);
        uiControllers.add(htmlFileController);
        fileHandlers.add(htmlFileGenerator);

        final FileCheckController fileCheckController = new FileCheckController(_list);
        final FileChecker fileCheckGenerator = new FileChecker(_homepagePath, _tmpPath, fileCheckController);
        uiControllers.add(fileCheckController);
        fileHandlers.add(fileCheckGenerator);

        final NodeValueCheckController nodeCheckController = new NodeValueCheckController(_list);
        final NodeValueChecker nodeValueCheckGenerator = new NodeValueChecker(_homepagePath, _tmpPath, nodeCheckController);
        uiControllers.add(nodeCheckController);
        fileHandlers.add(nodeValueCheckGenerator);

        if (_internetAccessiSEnabled) {
            final LinkCheckController linkCheckController = new LinkCheckController(_list);
            final LinkChecker linkCheckGenerator = new LinkChecker(_homepagePath, _tmpPath, linkCheckController);
            uiControllers.add(linkCheckController);
            fileHandlers.add(linkCheckGenerator);
        }

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
                        final FileEventDispachter dataOrchestrator = new FileEventDispachter(_homepagePath, _list, fileHandlers);
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
        fileColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        fileColumn.setPrefWidth(250);
        table.getColumns().add(fileColumn);
        
        final TableColumn<ObservableFile, String> modificationDateTimeColumn = new TableColumn<>("Modified on");
        modificationDateTimeColumn.setPrefWidth(110);
        modificationDateTimeColumn.setCellValueFactory(f -> f.getValue().getModificationDateTimeProperty());
        table.getColumns().add(modificationDateTimeColumn);

        final TableColumn<ObservableFile, Number> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setPrefWidth(45);
        sizeColumn.setCellValueFactory(f -> f.getValue().getSizeProperty());
        table.getColumns().add(sizeColumn);

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
                        final Path tmpPath,
                        final boolean internetAccessiSEnabled) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _internetAccessiSEnabled = internetAccessiSEnabled;
        _list = new ObservableFileList();
        launch();
    }

    private void generateGlobalFiles() {        
        SiteFilesGenerator.generate(_homepagePath, _list.getFileList());
        JsonGenerator.generate(_homepagePath, _list.getFileList());
        MetricsExtractor.generate(_homepagePath);
    }

    private void exit() {
        System.exit(0);
    }
}
