package ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import data.FileHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObservableFile { // TODO this class must be split, it currently knows all the types of generated files !

    private final SimpleStringProperty _name;
    private final SimpleStringProperty _modificationDateTime;
    private final SimpleLongProperty _size;
    private final SimpleStringProperty _htmlFileStatus;
    private final SimpleStringProperty _fileCheckStatus;
    private final SimpleStringProperty _nodeValueCheckStatus;
    private final SimpleStringProperty _linkCheckStatus;
    private Path _htmlFileOuputFile;
    private Path _htmlFileReportFile;
    private Path _fileCheckOuputFile;
    private Path _fileCheckReportFile;
    private Path _nodeValueCheckOuputFile;
    private Path _nodeValueCheckReportFile;
    private Path _linkCheckOuputFile;
    private Path _linkCheckReportFile;

    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("YYYYMMdd'T'HHmmss");

    public ObservableFile(final Path path,
                          final FileTime modificationDateTime,
                          final long size) {
        
        _name = new SimpleStringProperty(path.toString());
        _modificationDateTime = new SimpleStringProperty(formatFileTime(modificationDateTime));
        _size = new SimpleLongProperty(size);
        _htmlFileStatus = new SimpleStringProperty();
        _fileCheckStatus = new SimpleStringProperty();
        _nodeValueCheckStatus = new SimpleStringProperty();
        _linkCheckStatus = new SimpleStringProperty();
    }

    public SimpleStringProperty getNameProperty() {
        return _name;
    }

    public String getName() {
        return _name.get();
    }

    public Path getPath() {
        return Paths.get(_name.get());
    }

    void setDeleted() {
        _modificationDateTime.set("");
        _size.set(0L);
    }

    void setCreated(final FileTime modificationDateTime,
                    final long size) {
        _modificationDateTime.set(formatFileTime(modificationDateTime));
        _size.set(size);
    }

    public SimpleStringProperty getModificationDateTimeProperty() {
        return _modificationDateTime;
    }

    public SimpleLongProperty getSizeProperty() {
        return _size;
    }

    // --- HTML generation ---

    public SimpleStringProperty getHtmlGenerationProperty() {
        return _htmlFileStatus;
    }

    public String getHtmlGenerationStatus() {
        return _htmlFileStatus.get();
    }

    public void setHtmlGenerationStatus(final FileHandler.Status status,
                                        final Path outputFile,
                                        final Path reportFile) {
        _htmlFileStatus.set(status.toString());
        _htmlFileOuputFile = outputFile;
        _htmlFileReportFile = reportFile;
    }

    public Path getHtmlFileOuputFile() {
        return _htmlFileOuputFile;
    }

    public Path getHtmlFileReportFile() {
        return _htmlFileReportFile;
    }

    // --- file check ---
    
    public SimpleStringProperty getFileCheckProperty() {
        return _fileCheckStatus;
    }

    public String getFileCheckStatus() {
        return _fileCheckStatus.get();
    }

    public void setFileCheckStatus(final FileHandler.Status status,
                                   final Path outputFile,
                                   final Path reportFile) {
        _fileCheckStatus.set(status.toString());
        _fileCheckOuputFile = outputFile;
        _fileCheckReportFile = reportFile;
    }

    public Path getFileCheckOuputFile() {
        return _fileCheckOuputFile;
    }

    public Path getFileCheckReportFile() {
        return _fileCheckReportFile;
    }
    
    // --- node value check ---

    public SimpleStringProperty getNodeValueCheckProperty() {
        return _nodeValueCheckStatus;
    }

    public String getNodeValueCheckStatus() {
        return _nodeValueCheckStatus.get();
    }

    public void setNodeValueCheckStatus(final FileHandler.Status status,
                                        final Path outputFile,
                                        final Path reportFile) {
        _nodeValueCheckStatus.set(status.toString());
        _nodeValueCheckOuputFile = outputFile;
        _nodeValueCheckReportFile = reportFile;
    }

    public Path getNodeValueCheckOuputFile() {
        return _nodeValueCheckOuputFile;
    }

    public Path getNodeValueCheckReportFile() {
        return _nodeValueCheckReportFile;
    }

    // --- link check ---

    public SimpleStringProperty getLinkCheckProperty() {
        return _linkCheckStatus;
    }

    public String getLinkCheckStatus() {
        return _linkCheckStatus.get();
    }

    public void setLinkCheckStatus(final FileHandler.Status status,
                                   final Path outputFile,
                                   final Path reportFile) {
        _linkCheckStatus.set(status.toString());
        _linkCheckOuputFile = outputFile;
        _linkCheckReportFile = reportFile;
    }

    public Path getLinkCheckOuputFile() {
        return _linkCheckOuputFile;
    }

    public Path getLinkCheckReportFile() {
        return _linkCheckReportFile;
    }
    
    // --- helpers ---
    
    private static String formatFileTime(final FileTime fileTime) {
        return s_formatter.format(fileTime.toInstant()
                                          .atZone(ZoneId.systemDefault())
                                          .toLocalDateTime());
    }
}
