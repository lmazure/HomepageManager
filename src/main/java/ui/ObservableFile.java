package ui;

import java.nio.file.Path;
import java.nio.file.Paths;

import data.FileHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObservableFile {

    private final SimpleStringProperty _name;
    private final SimpleBooleanProperty _isDeleted;
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

    public ObservableFile(final Path path) {
        
        _name = new SimpleStringProperty(path.toString());
        _isDeleted = new SimpleBooleanProperty(false);
        _htmlFileStatus = new SimpleStringProperty();
        _fileCheckStatus = new SimpleStringProperty();
        _nodeValueCheckStatus = new SimpleStringProperty();
        _linkCheckStatus = new SimpleStringProperty();
    }
    
    public SimpleStringProperty nameProperty() {
        return _name;
    }
    
    public String getName() {
        return _name.get();
    }

    public Path getPath() {
        return Paths.get(_name.get());
    }

    public void setName(final String name) {
       _name.set(name);
    }

    void setDeleted() {
        _isDeleted.set(true);
    }
    
    void setCreated() {
        _isDeleted.set(false);
    }
       
    boolean isDeleted() {
        return _isDeleted.get();
    }
    
    public SimpleBooleanProperty deletedProperty() {
        return _isDeleted;
    }
    
    // --- HTML generation ---
    
    public SimpleStringProperty htmlGenerationProperty() {
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
    
    public SimpleStringProperty fileCheckProperty() {
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
    
    public SimpleStringProperty nodeValueCheckProperty() {
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
    
    public SimpleStringProperty linkCheckProperty() {
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
    }}
