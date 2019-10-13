package data;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import utils.ExitHelper;

public class FileTracker {

    private final List<FileHandler> _fileHandlers;
    private final Map<Path, TrackedFile> _files;
    private final ObservableList<TrackedFile> _data;


    public FileTracker(final ObservableList<TrackedFile> data) {
        _fileHandlers = new ArrayList<FileHandler>();
        _files= new HashMap<>();
        _data = data;
    }
    
    public void addFile(final Path file) {
    
        if (_files.containsKey(file)) {
            ExitHelper.exit("Duplicated path");
        }
        
        final TrackedFile f = new TrackedFile(file);
        _files.put(file, f);
        _data.add(f);
        
        for (FileHandler h: _fileHandlers) {
            if (h.outputFileMustBeRegenerated(file)) {
                final FileHandler.Status status = h.handleDeletion(file);
                if (h.getClass().equals(HTMLFileGenerator.class)) f.setHtmlFileStatus(status);
                if (h.getClass().equals(FileCheckGenerator.class)) f.setFileCheckStatus(status);
                final FileHandler.Status status2 = h.handleCreation(file);                
                if (h.getClass().equals(HTMLFileGenerator.class)) f.setHtmlFileStatus(status2);
                if (h.getClass().equals(FileCheckGenerator.class)) f.setFileCheckStatus(status2);
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
            _data.add(f);
        } else if (!f.isDeleted()) {
            ExitHelper.exit("Creating a file (" + file + ") that currently exists");
        }
        
        f.setCreated();
        
        for (FileHandler h: _fileHandlers) {
            final FileHandler.Status status = h.handleCreation(file);
            if (h.getClass().equals(HTMLFileGenerator.class)) f.setHtmlFileStatus(status);
            if (h.getClass().equals(FileCheckGenerator.class)) f.setFileCheckStatus(status);
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
            final FileHandler.Status status = h.handleDeletion(file);
            if (h.getClass().equals(HTMLFileGenerator.class)) f.setHtmlFileStatus(status);
            if (h.getClass().equals(FileCheckGenerator.class)) f.setFileCheckStatus(status);
        }
    }
}
