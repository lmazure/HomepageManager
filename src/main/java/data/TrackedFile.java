package data;
import java.nio.file.Path;

public class TrackedFile {

    private boolean _isDeleted;
    
    public TrackedFile(final Path path) {
        _isDeleted = false;
    }
    
    public boolean isDeleted() {
        return _isDeleted;
    }
    
    public void setCreated() {
        _isDeleted = false;
    }
    
    public void setDeleted() {
        _isDeleted = true;
    }
}
