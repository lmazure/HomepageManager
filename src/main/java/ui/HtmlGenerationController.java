package ui;

import java.nio.file.Path;

import data.FileHandler.Status;
import javafx.scene.control.TableColumn;

public class HtmlGenerationController extends GenericUiController {

    final private Path _homepagePath;

    public HtmlGenerationController(final ObservableFileList list,
                                    final Path homepagePath) {
        super((final Path file, final Status status, final Path outputFile, final Path reportFile) -> list.getFile(file).setHtmlGenerationStatus(status, outputFile, reportFile));
        _homepagePath = homepagePath;
    }
    
    @Override
    public TableColumn<ObservableFile, ?> getColumns() {
        
        final TableColumn<ObservableFile, String> allColumns = new TableColumn<ObservableFile, String>("HTML");

        final TableColumn<ObservableFile, String> displayColumn = new TableColumn<>("Display");
        displayColumn.setPrefWidth(70);
        displayColumn.setCellFactory(p -> { return new FixedButtonCell<ObservableFile>("display", f -> ActionHelper.displayHtmlFile(f.getHtmlFileOuputFile(), _homepagePath));});
        allColumns.getColumns().add(displayColumn);

        final TableColumn<ObservableFile, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(170);
        statusColumn.setCellValueFactory(f -> f.getValue().getHtmlGenerationProperty());
        statusColumn.setCellFactory(p -> { return new UpdatableButtonCell<ObservableFile>(f -> ActionHelper.displayFile(f.getHtmlFileReportFile()));});
        allColumns.getColumns().add(statusColumn);

        return allColumns;
    }
}
