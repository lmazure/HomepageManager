package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.FileHandler.Status;
import javafx.scene.control.TableColumn;

/**
 *
 */
public class NodeValueCheckController extends GenericUiController {

    /**
     * @param list
     */
    public NodeValueCheckController(final ObservableFileList list) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setNodeValueCheckStatus(status, outputFile, reportFile));
    }

    @Override
    public TableColumn<ObservableFile, ?> getColumns() {

        final TableColumn<ObservableFile, String> allColumns = new TableColumn<>("Check node values");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(61);
        displayColumn.setSortable(false);
        displayColumn.setCellFactory(_ -> new FixedButtonCell<>("display", f -> ActionHelper.displayFile(f.getNodeValueCheckOuputFile())));
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(172);
        statusColumn.setCellValueFactory(f -> f.getValue().getNodeValueCheckProperty());
        statusColumn.setCellFactory(_ -> new ColoredUpdatableButtonCell<>(f -> ActionHelper.displayFile(f.getNodeValueCheckReportFile()),
                                                                            StatusRepresentation.getColorMap()));
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
