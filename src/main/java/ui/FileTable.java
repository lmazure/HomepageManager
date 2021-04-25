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

        final List<GenericUiController> uiControllers = new ArrayList<>();
        final List<FileHandler> fileHandlers = new ArrayList<>();

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
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setMaximized(true);

        final BorderPane border = new BorderPane();
        border.setCenter(buildTable(uiControllers));
        border.setBottom(builtButtons());
        final Scene scene = new Scene(border);

        final Service<Void> calculateService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {

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

    private static TableView<ObservableFile> buildTable(final List<GenericUiController> uiControllers) {

        final TableView<ObservableFile> table = new TableView<>();

        final TableColumn<ObservableFile, String> fileColumn = new TableColumn<>("File");
        fileColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        fileColumn.setPrefWidth(230);
        table.getColumns().add(fileColumn);

        final TableColumn<ObservableFile, String> modificationDateTimeColumn = new TableColumn<>("Modified on");
        modificationDateTimeColumn.setPrefWidth(103);
        modificationDateTimeColumn.setCellValueFactory(f -> f.getValue().getModificationDateTimeProperty());
        table.getColumns().add(modificationDateTimeColumn);

        final TableColumn<ObservableFile, Number> sizeColumn = new TableColumn<>("Size");
        sizeColumn.setPrefWidth(45);
        sizeColumn.setCellValueFactory(f -> f.getValue().getSizeProperty());
        table.getColumns().add(sizeColumn);

        for (final GenericUiController uiController: uiControllers) {
            table.getColumns().add(uiController.getColumns());
        }

        // solution 1
        table.setItems(_list.getObservableFileList());

        // solution 2 (same behavior, but I am still not able to make a default sorting working at initial display)
        /*
        final SortedList<ObservableFile> sortedData = new SortedList<ObservableFile>(_list.getObservableFileList());
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        table.getSortOrder().add(modificationDateTimeColumn);
        */

        return table;
    }

    private static HBox builtButtons() {

        final HBox buttonPanel = new HBox();

        final Button generateGlobalFileButton = new Button("Generate global files");
        generateGlobalFileButton.setOnAction(event -> generateGlobalFiles());
        buttonPanel.getChildren().add(generateGlobalFileButton);

        final Button quitButton = new Button("Quit");
        quitButton.setOnAction(event -> exit());
        buttonPanel.getChildren().add(quitButton);

        return buttonPanel;
    }

    public static void display(final Path homepagePath,
                               final Path tmpPath,
                               final boolean internetAccessiSEnabled) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _internetAccessiSEnabled = internetAccessiSEnabled;
        _list = new ObservableFileList();
        launch();
    }

    private static void generateGlobalFiles() {
        SiteFilesGenerator.generate(_homepagePath, _list.getFileList());
        JsonGenerator.generate(_homepagePath, _list.getFileList());
        MetricsExtractor.generate(_homepagePath);
    }

    private static void exit() {
        System.exit(0);
    }
}
