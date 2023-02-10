package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;

import fr.mazure.homepagemanager.data.FileHandler.Status;
import javafx.scene.control.TableColumn;

/**
 *
 */
public class HtmlGenerationController extends GenericUiController {

    private final Path _homepagePath;

    /**
     * @param list
     * @param homepagePath
     */
    public HtmlGenerationController(final ObservableFileList list,
                                    final Path homepagePath) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setHtmlGenerationStatus(status, outputFile, reportFile));
        _homepagePath = homepagePath;
    }

    @Override
    public TableColumn<ObservableFile, ?> getColumns() {

        final TableColumn<ObservableFile, String> allColumns = new TableColumn<>("HTML");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(61);
        displayColumn.setSortable(false);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<>("display", f -> ActionHelper.displayHtmlFile(f.getHtmlFileOuputFile(), _homepagePath));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(172);
        statusColumn.setCellValueFactory(f -> f.getValue().getHtmlGenerationProperty());
        statusColumn.setCellFactory(p -> { return new ColoredUpdatableButtonCell<>(f -> ActionHelper.displayFile(f.getHtmlFileReportFile()),
                                                                            StatusRepresentation.getColorMap());});
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
