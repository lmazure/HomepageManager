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
import data.MyFile;
import utils.ExitHelper;

public class Main extends Application {

    static private Path _homepagePath;
    static private Path _tmpPath;
 
    private final TableView<MyFile> _table;
    private final ObservableList<MyFile> _data;
    
    public Main() {
        _data = FXCollections.observableArrayList();
        _table = new TableView<MyFile>();
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
        stage.setWidth(300);
        stage.setHeight(500);
 
        final Label label = new Label("Files");
        label.setFont(new Font("Arial", 20));
 
        //_table.setEditable(true);
 
        final TableColumn<MyFile, String> fileCol = new TableColumn<MyFile, String>("File");
        fileCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        
        _table.getColumns().addAll(fileCol);
        _table.setItems(_data);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, _table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        
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