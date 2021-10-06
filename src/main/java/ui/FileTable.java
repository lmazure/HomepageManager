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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

        final MenuBar menuBar = new MenuBar();
        final Menu menu = new Menu("Tools");
        final MenuItem generateFiles = new MenuItem("Generate global files");
        generateFiles.setOnAction(e -> generateGlobalFiles());
        menu.getItems().add(generateFiles);
        final MenuItem generateXml = new MenuItem("Generate XML for link");
        generateXml.setOnAction(e -> displayLinkXmlGenerator());
        menu.getItems().add(generateXml);
        final MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> exit());
        menu.getItems().add(exit);
        menuBar.getMenus().add(menu);

        final TableView<ObservableFile> table = buildTable(uiControllers);
        final VBox vBox = new VBox(menuBar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        final Scene scene = new Scene(vBox);

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

    private static void displayLinkXmlGenerator() {
       final XmlGeneratorDialog dialog = new XmlGeneratorDialog();
       dialog.showAndWait();
    }

    private static void exit() {
        System.exit(0);
    }
}
