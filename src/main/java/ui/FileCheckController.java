package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

/**
 *
 */
public class FileCheckController extends GenericUiController {

    /**
     * @param list
     */
    public FileCheckController(final ObservableFileList list) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setFileCheckStatus(status, outputFile, reportFile));
    }

    @Override
    public TableColumn<ObservableFile, ?> getColumns() {

        final TableColumn<ObservableFile, String> allColumns = new TableColumn<>("Check file");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(61);
        displayColumn.setSortable(false);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<>("display", f -> ActionHelper.displayFile(f.getFileCheckOuputFile()));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(172);
        statusColumn.setCellValueFactory(f -> f.getValue().getFileCheckProperty());
        statusColumn.setCellFactory(p -> { return new ColoredUpdatableButtonCell<>(f -> ActionHelper.displayFile(f.getFileCheckReportFile()),
                                                                            StatusRepresentation.getColorMap());});
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
