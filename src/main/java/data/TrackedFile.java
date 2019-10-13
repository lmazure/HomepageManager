package data;
import java.nio.file.Path;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class TrackedFile {

    private final SimpleStringProperty _name;
    private final SimpleBooleanProperty _isDeleted;
    private final SimpleStringProperty _htmlFileStatus;
    private final SimpleStringProperty _fileCheckStatus;

    public TrackedFile(final Path path) {
        
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

    public void setName(final String name) { // TODO do we really need this method?
       _name.set(name);
    }

    public SimpleStringProperty htmlFileProperty() {
        return _htmlFileStatus;
    }
    
    public String getHtmlFileStatus() {
        return _htmlFileStatus.get();
    }

    public void setHtmlFileStatus(final FileHandler.Status status) { // TODO do we really need this method?
        _htmlFileStatus.set(status.toString());
    }

    public SimpleStringProperty fileCheckProperty() {
        return _fileCheckStatus;
    }
    
    public String getFileCheckStatus() {
        return _fileCheckStatus.get();
    }

    public void setFileCheckStatus(final FileHandler.Status status) { // TODO do we really need this method?
        _fileCheckStatus.set(status.toString());
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
}
