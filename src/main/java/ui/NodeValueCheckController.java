package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class NodeValueCheckController extends GenericUiController {

    public NodeValueCheckController(final ObservableFileList list) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setNodeValueCheckStatus(status, outputFile, reportFile));
    }
    
    @Override
    public TableColumn<ObservableFile, ?> getColumns() {
        
        final TableColumn<ObservableFile, String> allColumns = new TableColumn<ObservableFile, String>("Check node values");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(70);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<ObservableFile>("display", f -> ActionHelper.displayFile(f.getNodeValueCheckOuputFile()));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(170);
        statusColumn.setCellValueFactory(f -> f.getValue().nodeValueCheckProperty());
        statusColumn.setCellFactory(p -> { return new UpdatableButtonCell<ObservableFile>(f -> ActionHelper.displayFile(f.getNodeValueCheckReportFile()));});
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
