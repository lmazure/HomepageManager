package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import fr.mazure.homepagemanager.data.FileHandler;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * File displayed in the file table
 */
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

    private static DateTimeFormatter s_formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    /**
     * Constructor
     *
     * @param path path of the file
     * @param modificationDateTime modification date and time of the file
     * @param size size of the file
     */
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

    /**
     * @return property containing the name of the file
     */
    public SimpleStringProperty getNameProperty() {
        return _name;
    }

    /**
     * @return name of the file
     */
    public String getName() {
        return _name.get();
    }

    /**
     * @return path of the file
     */
    public Path getPath() {
        return Paths.get(_name.get());
    }

    /**
     * Update the file data when the file is deleted
     */
    public void setDeleted() {
        _modificationDateTime.set("");
        _size.set(0L);
    }

    /**
     * Update the file data when the file is created
     *
     * @param modificationDateTime modification date and time of the file
     * @param size size of the file
     */
    public void setCreated(final FileTime modificationDateTime,
                           final long size) {
        _modificationDateTime.set(formatFileTime(modificationDateTime));
        _size.set(size);
    }

    /**
     * @return property containing the modification date and time of the file
     */
    public SimpleStringProperty getModificationDateTimeProperty() {
        return _modificationDateTime;
    }

    /**
     * @return property containing the size of the file
     */
    public SimpleLongProperty getSizeProperty() {
        return _size;
    }

    // --- HTML generation ---

    /**
     * @return property containing the status of the HTML generation
     */
    public SimpleStringProperty getHtmlGenerationProperty() {
        return _htmlFileStatus;
    }

    /**
     * @return status of the HTML generation
     */
    public String getHtmlGenerationStatus() {
        return _htmlFileStatus.get();
    }

    /**
     * @param status status of the HTML generation
     * @param outputFile output file of the HTML generation
     * @param reportFile report file of the HTML generation
     */
    public void setHtmlGenerationStatus(final FileHandler.Status status,
                                        final Path outputFile,
                                        final Path reportFile) {
        _htmlFileStatus.set(status.toString());
        _htmlFileOuputFile = outputFile;
        _htmlFileReportFile = reportFile;
    }

    /**
     * @return output file of the HTML generation
     */
    public Path getHtmlFileOuputFile() {
        return _htmlFileOuputFile;
    }

    /**
     * @return report file of the HTML generation
     */
    public Path getHtmlFileReportFile() {
        return _htmlFileReportFile;
    }

    // --- file check ---

    /**
     * @return property containing the status of the file check
     */
    public SimpleStringProperty getFileCheckProperty() {
        return _fileCheckStatus;
    }

    /**
     * @return status of the file check
     */
    public String getFileCheckStatus() {
        return _fileCheckStatus.get();
    }

    /**
     * @param status status of the file check
     * @param outputFile output file of the file check
     * @param reportFile report file of the file check
     */
    public void setFileCheckStatus(final FileHandler.Status status,
                                   final Path outputFile,
                                   final Path reportFile) {
        _fileCheckStatus.set(status.toString());
        _fileCheckOuputFile = outputFile;
        _fileCheckReportFile = reportFile;
    }

    /**
     * @return output file of the file check
     */
    public Path getFileCheckOuputFile() {
        return _fileCheckOuputFile;
    }

    /**
     * @return report file of the file check
     */
    public Path getFileCheckReportFile() {
        return _fileCheckReportFile;
    }

    // --- node value check ---

    /**
     * @return property containing the status of the node value check
     */
    public SimpleStringProperty getNodeValueCheckProperty() {
        return _nodeValueCheckStatus;
    }

    /**
     * @return status of the node value check
     */
    public String getNodeValueCheckStatus() {
        return _nodeValueCheckStatus.get();
    }

    /**
     * @param status status of the node value check
     * @param outputFile output file of the node value check
     * @param reportFile report file of the node value check
     */
    public void setNodeValueCheckStatus(final FileHandler.Status status,
                                        final Path outputFile,
                                        final Path reportFile) {
        _nodeValueCheckStatus.set(status.toString());
        _nodeValueCheckOuputFile = outputFile;
        _nodeValueCheckReportFile = reportFile;
    }

    /**
     * @return output file of the node value check
     */
    public Path getNodeValueCheckOuputFile() {
        return _nodeValueCheckOuputFile;
    }

    /**
     * @return report file of the node value check
     */
    public Path getNodeValueCheckReportFile() {
        return _nodeValueCheckReportFile;
    }

    // --- link check ---

    /**
     * @return property containing the status of the link check
     */
    public SimpleStringProperty getLinkCheckProperty() {
        return _linkCheckStatus;
    }

    /**
     * @return status of the link check
     */
    public String getLinkCheckStatus() {
        return _linkCheckStatus.get();
    }

    /**
     * @param status status of the link check
     * @param outputFile output file of the link check
     * @param reportFile report file of the link check
     */
    public void setLinkCheckStatus(final FileHandler.Status status,
                                   final Path outputFile,
                                   final Path reportFile) {
        _linkCheckStatus.set(status.toString());
        _linkCheckOuputFile = outputFile;
        _linkCheckReportFile = reportFile;
    }

    /**
     * @return output file of the link check
     */
    public Path getLinkCheckOuputFile() {
        return _linkCheckOuputFile;
    }

    /**
     * @return report file of the link check
     */
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
