import java.io.PrintStream;
import java.nio.file.Path;

public class TrackedFile {

    private Path _path;
    private boolean _isDeleted;
    
    TrackedFile(final Path path) {
        
        _path = path;
        _isDeleted = false;
    }
    
    void setDeleted() {
        _isDeleted = true;
    }
    
    void setCreated() {
        _isDeleted = false;
    }
    
    boolean isDeleted() {
        return _isDeleted;
    }
    
    void dump(final PrintStream stream) {
        stream.println("path=" + _path);
        stream.println("isDeleted=" + _isDeleted);
    }
}
