package data;
import java.nio.file.Path;

public class TrackedFile {

    private Path _path;
    private boolean _isDeleted;
    
    public TrackedFile(final Path path) {
        
        _path = path;
        _isDeleted = false;
    }
    
    void setDeleted() {
        _isDeleted = true;
    }
    
    void setCreated() {
        _isDeleted = false;
    }
    
    public Path getPath() { //TODO delete this unused method
        return _path;
    }
    
    boolean isDeleted() {
        return _isDeleted;
    }
}
