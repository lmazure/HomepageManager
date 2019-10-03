import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileTracker {

    final List<FileHandler> _fileHandlers;
    final Map<Path, TrackedFile> _files;

    public FileTracker() {
        _fileHandlers = new ArrayList<FileHandler>();
        _files= new HashMap<>();
    }
    
    public void addFile(final Path file) {
    
        if (_files.containsKey(file)) {
            ExitHelper.exit("Duplicated path");
        }
        
        _files.put(file, new TrackedFile(file));
        
        for (FileHandler h: _fileHandlers) {
            if (h.outputFileMustBeRegenerated(file)) {
                h.handleDeletion(file);
                h.handleCreation(file);                
            }
        }
    }
    
    public FileTracker addFileHandler(final FileHandler handler) {
        _fileHandlers.add(handler);
        return this;
    }
    
    public void handleFileCreation(final Path file) {

        TrackedFile f = _files.get(file);
        if (f == null) {
            f= new TrackedFile(file);
            _files.put(file, f);
        } else if (!f.isDeleted()) {
            ExitHelper.exit("Creating a file (" + file + ") that currently exists");
        }
        
        f.setCreated();
        
        for (FileHandler h: _fileHandlers) {
            h.handleCreation(file);
        }
    }
    
    public void handleFileDeletion(final Path file) {

        final TrackedFile f = _files.get(file);
        if (f == null) {
            ExitHelper.exit("Unknown file");
        }
        assert (f != null);

        if (f.isDeleted()) {
            ExitHelper.exit("Deleting a file (" + file + ") that currently does not exist");
        }
        
        f.setDeleted();

        for (FileHandler h: _fileHandlers) {
            h.handleDeletion(file);
        }
    }
}
