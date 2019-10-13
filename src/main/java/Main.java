import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

import data.DataOrchestrator;
import data.TrackedFile;
import utils.ExitHelper;

public class Main extends Application {

    static private Path _homepagePath;
    static private Path _tmpPath;
 
    private final TableView<TrackedFile> _table;
    private final ObservableList<TrackedFile> _data;
    
    public Main() {
        _data = FXCollections.observableArrayList();
        _table = new TableView<TrackedFile>();
    }
    
    public static void main(final String[] args) {
        
        if (args.length != 2) {
            ExitHelper.exit("Syntax: HomepageManager <homepage directory> <tmp directory>");
        }
 
        _homepagePath = Paths.get(args[0]);
        _tmpPath = Paths.get(args[1]);

        launch(args);
    }
 
    @Override
    public void start(Stage stage) {
        final Scene scene = new Scene(new Group());
        stage.setTitle("Homepage Manager");
        stage.setWidth(900);
        stage.setHeight(500);
 
        final Label label = new Label("Files");
        label.setFont(new Font("Arial", 20));
 
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
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, _table);
 
        ((Group)scene.getRoot()).getChildren().addAll(vbox);
 
        
        final Service<Void> calculateService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        final DataOrchestrator main = new DataOrchestrator(_homepagePath, _tmpPath, _data);
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