package ui;

import java.nio.file.Path;

import data.FileHandler;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class ObservableFile {

    private final SimpleStringProperty _name;
    private final SimpleBooleanProperty _isDeleted;
    private final SimpleStringProperty _htmlFileStatus;
    private final SimpleStringProperty _fileCheckStatus;
    private Path _htmlFileOuputFile;
    private Path _htmlFileReportFile;
    private Path _fileCheckOuputFile;
    private Path _fileCheckReportFile;

    public ObservableFile(final Path path) {
        
        _name = new SimpleStringProperty(path.toString());
        _isDeleted = new SimpleBooleanProperty(false);
        _htmlFileStatus = new SimpleStringProperty();
        _fileCheckStatus = new SimpleStringProperty();
    }
    
    public SimpleStringProperty nameProperty() {
        return _name;
    }
    
    public String getName() {
        return _name.get();
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
    
    // --- HTML file ---
    
    public SimpleStringProperty htmlFileProperty() {
        return _htmlFileStatus;
    }
    
    public String getHtmlFileStatus() {
        return _htmlFileStatus.get();
    }

    public void setHtmlFileStatus(final FileHandler.Status status, final Path outputFile, final Path reportFile) {
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

    public void setFileCheckStatus(final FileHandler.Status status, final Path outputFile, final Path reportFile) {
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
    
}
