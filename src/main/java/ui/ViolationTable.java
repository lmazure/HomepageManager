package ui;

import data.Violation;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @author Laurent
 *
 */
public class ViolationTable {

    private final TableView<Violation> _table;

    public ViolationTable(final ObservableList<Violation> violationList) {
        _table = new TableView<>(violationList);
        System.out.println("---- before");
        for (Violation o: violationList) {
            System.out.println(o);
        }
        System.out.println("---- after");
    }

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        
        final TableColumn<Violation, String> flleCol = new TableColumn<>("File");
        flleCol.setMinWidth(100);
        flleCol.setCellValueFactory(new PropertyValueFactory<>("file"));

        final TableColumn<Violation, String> typeCol = new TableColumn<>("Type");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        final TableColumn<Violation, String> ruleCol = new TableColumn<>("Rule");
        ruleCol.setMinWidth(100);
        ruleCol.setCellValueFactory(new PropertyValueFactory<>("rule"));

        final TableColumn<Violation, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setMinWidth(100);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        _table.getColumns().addAll(flleCol, typeCol, ruleCol, descriptionCol);
        _table.setEditable(false);

        stage.setScene(new Scene(_table, 900, 900));
        stage.show();
    }
}
