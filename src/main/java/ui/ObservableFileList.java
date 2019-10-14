package ui;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import data.FileExistenceHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ObservableFileList implements FileExistenceHandler {

    private final ObservableList<ObservableFile> _data;
    private final Map<Path, ObservableFile> _files;

    public ObservableFileList() {
        _data = FXCollections.observableArrayList();
        _files = new HashMap<Path, ObservableFile>(); 
    }
    
    @Override
    public void handleCreation(final Path file) {
        final ObservableFile f = new ObservableFile(file);
        _data.add(f);
        _files.put(file, f);
    }

    @Override
    public void handleDeletion(final Path file) {
        getFile(file).setDeleted();
    }
    
    public ObservableList<ObservableFile> getObservableList() {
        return _data;
    }
    
    public ObservableFile getFile(final Path file) {
        return _files.get(file);
    }
}
