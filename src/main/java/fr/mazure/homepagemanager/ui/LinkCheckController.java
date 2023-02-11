package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.FileHandler.Status;
import javafx.scene.control.TableColumn;

/**
*
*/
public class LinkCheckController extends GenericBackgroundUiController {

    /**
     * @param list
     */
    public LinkCheckController(final ObservableFileList list) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setLinkCheckStatus(status, outputFile, reportFile));
    }

    @Override
    public TableColumn<ObservableFile, ?> getColumns() {

        final TableColumn<ObservableFile, String> allColumns = new TableColumn<>("Check links");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(61);
        displayColumn.setSortable(false);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<>("display", f -> ActionHelper.displayFile(f.getLinkCheckOuputFile()));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(172);
        statusColumn.setCellValueFactory(f -> f.getValue().getLinkCheckProperty());
        statusColumn.setCellFactory(p -> { return new ColoredUpdatableButtonCell<>(f -> ActionHelper.displayFile(f.getLinkCheckReportFile()),
                                                                            StatusRepresentation.getColorMap());});
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
