package data;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ExitHelper;

public class FileTracker {

    private final List<FileHandler> _fileHandlers;
    private final Map<Path, TrackedFile> _files;
    private final FileExistenceHandler _handler;

    public FileTracker(final FileExistenceHandler handler) {
        _fileHandlers = new ArrayList<FileHandler>();
        _files= new HashMap<Path, TrackedFile>();
        _handler = handler;
    }
    
    public void addFile(final Path file) {
    
        if (_files.containsKey(file)) {
            ExitHelper.exit("Duplicated path");
        }
        
        final TrackedFile f = new TrackedFile(file);
        _files.put(file, f);
        _handler.handleCreation(file);
        
        for (final FileHandler h: _fileHandlers) {
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
            _handler.handleCreation(file);
        } else if (!f.isDeleted()) {
            ExitHelper.exit("Creating a file (" + file + ") that currently exists");
        }
        
        f.setCreated();
        
        for (final FileHandler h: _fileHandlers) {
            h.handleCreation(file);
        }
    }
    
    public void handleFileDeletion(final Path file) {

        final TrackedFile f = _files.get(file);
        if (f == null) {
            ExitHelper.exit("Unknown file");
        }
        assert(f != null);

        if (f.isDeleted()) {
            ExitHelper.exit("Deleting a file (" + file + ") that currently does not exist");
        }

        _handler.handleDeletion(file);

        f.setDeleted();

        for (final FileHandler h: _fileHandlers) {
            h.handleDeletion(file);
        }
    }
}
