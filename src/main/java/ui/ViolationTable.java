package ui;

import data.Violation;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Violation table
 */
public class ViolationTable {

    private final TableView<Violation> _table;

    /**
     * @param violationList list of violation
     */
    public ViolationTable(final ObservableList<Violation> violationList) {
        _table = new TableView<>(violationList);
    }

    /**
     * create and display the table
     */
    public void show() {
        final Stage stage = new Stage();
        stage.setTitle("Violations");

        final TableColumn<Violation, String> fileCol = new TableColumn<>("File");
        fileCol.setMinWidth(200);
        fileCol.setCellValueFactory(new PropertyValueFactory<>("file"));
        _table.getColumns().add(fileCol);

        final TableColumn<Violation, String> typeCol = new TableColumn<>("Type");
        typeCol.setMinWidth(50);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        _table.getColumns().add(typeCol);

        final TableColumn<Violation, String> ruleCol = new TableColumn<>("Rule");
        ruleCol.setMinWidth(200);
        ruleCol.setCellValueFactory(new PropertyValueFactory<>("rule"));
        _table.getColumns().add(ruleCol);

        final TableColumn<Violation, String> locationCol = new TableColumn<>("Location");
        locationCol.setMinWidth(100);
        locationCol.setCellValueFactory(new PropertyValueFactory<>("locationDescription"));
        _table.getColumns().add(locationCol);

        final TableColumn<Violation, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setMinWidth(500);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        _table.getColumns().add(descriptionCol);

        _table.setEditable(false);

        stage.setScene(new Scene(_table, 1000, 900));
        stage.show();
    }
}