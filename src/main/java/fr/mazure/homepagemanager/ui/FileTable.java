package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.homepagemanager.data.FileContentChecker;
import fr.mazure.homepagemanager.data.FileEventDispatcher;
import fr.mazure.homepagemanager.data.FileHandler;
import fr.mazure.homepagemanager.data.HTMLGenerator;
import fr.mazure.homepagemanager.data.LinkChecker;
import fr.mazure.homepagemanager.data.NodeValueChecker;
import fr.mazure.homepagemanager.data.ViolationDataController;
import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Table listing all homepage pages
 */
public class FileTable extends Application {

    private static Path s_homepagePath;
    private static Path s_tmpPath;
    private static final String s_cacheFolderName = "internet_cache";
    private static boolean s_internetAccessIsEnabled;
    private static ObservableFileList s_list;
    private static ObservableViolationList s_violationList;

    @Override
    public void start(final Stage stage) {

        final List<GenericUiController> uiControllers = new ArrayList<>();
        final List<FileHandler> fileHandlers = new ArrayList<>();

        final HtmlGenerationController htmlFileController = new HtmlGenerationController(s_list, s_homepagePath);
        final HTMLGenerator htmlFileGenerator = new HTMLGenerator(s_homepagePath, s_tmpPath, htmlFileController);
        uiControllers.add(htmlFileController);
        fileHandlers.add(htmlFileGenerator);

        final ViolationDataController violationDataController = s_violationList;

        final FileCheckController fileCheckController = new FileCheckController(s_list);
        final FileContentChecker fileCheckGenerator = new FileContentChecker(s_homepagePath, s_tmpPath, fileCheckController, violationDataController);
        uiControllers.add(fileCheckController);
        fileHandlers.add(fileCheckGenerator);

        final NodeValueCheckController nodeCheckController = new NodeValueCheckController(s_list);
        final NodeValueChecker nodeValueCheckGenerator = new NodeValueChecker(s_homepagePath, s_tmpPath, nodeCheckController, violationDataController);
        uiControllers.add(nodeCheckController);
        fileHandlers.add(nodeValueCheckGenerator);

        if (s_internetAccessIsEnabled) {
            final LinkCheckController linkCheckController = new LinkCheckController(s_list);
            final LinkChecker linkCheckGenerator = new LinkChecker(s_homepagePath, s_tmpPath, s_cacheFolderName, linkCheckController, violationDataController);
            uiControllers.add(linkCheckController);
            fileHandlers.add(linkCheckGenerator);
        }

        stage.setTitle("Homepage Manager");
        stage.setWidth(1000);
        stage.setHeight(600);
        stage.setMaximized(true);

        final MenuBar menuBar = new MenuBar();
        final Menu menu = new Menu("Tools");
        final MenuItem displayViolations = new MenuItem("Display violations");
        displayViolations.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        displayViolations.setOnAction(_ -> displayViolations());
        menu.getItems().add(displayViolations);
        final MenuItem generateFiles = new MenuItem("Generate global files");
        generateFiles.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
        generateFiles.setOnAction(_ -> generateGlobalFiles());
        menu.getItems().add(generateFiles);
        final MenuItem generateXml = new MenuItem("Generate XML for link");
        generateXml.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN));
        generateXml.setOnAction(_ -> displayLinkXmlGenerator());
        menu.getItems().add(generateXml);
        final MenuItem displayStatistics = new MenuItem("Display statistics");
        displayStatistics.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        displayStatistics.setOnAction(_ -> displayStatisticsDialog());
        menu.getItems().add(displayStatistics);
        final MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exit.setOnAction(_ -> exit());
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
                        final FileEventDispatcher dataOrchestrator = new FileEventDispatcher(s_homepagePath, s_list, fileHandlers);
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
        fileColumn.setPrefWidth(350);
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
        table.setItems(s_list.getObservableFileList());

        // solution 2 (same behavior, but I am still not able to make a default sorting working at initial display)
        /*
        final SortedList<ObservableFile> sortedData = new SortedList<ObservableFile>(_list.getObservableFileList());
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);

        table.getSortOrder().add(modificationDateTimeColumn);
        */

        return table;
    }

    /**
     * display the table
     *
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     * @param internetAccessiSEnabled flag indicating if Internet is accessed (if False, links will not be checked)
     */
    public static void display(final Path homepagePath,
                               final Path tmpPath,
                               final boolean internetAccessiSEnabled) {
        s_homepagePath = homepagePath;
        s_tmpPath = tmpPath;
        s_internetAccessIsEnabled = internetAccessiSEnabled;
        s_list = new ObservableFileList();
        s_violationList = new ObservableViolationList();
        launch();
    }

    private static void displayViolations() {
        final ViolationTable violationTable = new ViolationTable(s_violationList.getObservableList());
        violationTable.show();
     }

    @SuppressWarnings("unused")
    private static void generateGlobalFiles() {
        new GlobalFileCreationDialog(s_homepagePath, s_list.getFileList()); //TODO this is wrong, the UI should not pilot the logic, it should be the other way around
    }

    private static void displayLinkXmlGenerator() {
       final XmlGenerationDialog dialog = new XmlGenerationDialog(s_tmpPath.resolve(s_cacheFolderName));
       dialog.showAndWait();
    }

    private static void displayStatisticsDialog() {
        final StatisticsDialog dialog = new StatisticsDialog(s_homepagePath, s_list.getFileList());
        dialog.showAndWait();
     }

    private static void exit() {
        System.exit(0);
    }
}
