package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class FileCheckController extends GenericUiController {

    public FileCheckController(final ObservableFileList list) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setFileCheckStatus(status, outputFile, reportFile));
    }

    @Override
    public TableColumn<ObservableFile, ?> getColumns() {

        final TableColumn<ObservableFile, String> allColumns = new TableColumn<ObservableFile, String>("Check file");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(59);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<ObservableFile>("display", f -> ActionHelper.displayFile(f.getFileCheckOuputFile()));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(170);
        statusColumn.setCellValueFactory(f -> f.getValue().getFileCheckProperty());
        statusColumn.setCellFactory(p -> { return new UpdatableButtonCell<ObservableFile>(f -> ActionHelper.displayFile(f.getFileCheckReportFile()));});
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
