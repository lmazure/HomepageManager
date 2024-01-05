package fr.mazure.homepagemanager.ui;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fr.mazure.homepagemanager.data.FileExistenceHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 */
public class ObservableFileList implements FileExistenceHandler {

    private final ObservableList<ObservableFile> _data;
    private final Map<Path, ObservableFile> _files;

    /**
     *
     */
    public ObservableFileList() {
        _data = FXCollections.observableArrayList();
        _files = new HashMap<>();
    }

    @Override
    public void handleCreation(final Path file,
                               final FileTime creationDateTime,
                               final long size) {
        final ObservableFile f = getFile(file);
        if (f == null) {
            final ObservableFile nf = new ObservableFile(file, creationDateTime, size);
            _data.add(nf);
            _files.put(file, nf);
        } else {
            f.setCreated(creationDateTime, size);
        }
    }

    @Override
    public void handleDeletion(final Path file) {
        getFile(file).setDeleted();
    }

    /**
     * @return
     */
    public ObservableList<ObservableFile> getObservableFileList() {
        return _data;
    }

    /**
     * @return list of the paths of all files
     */
    public List<Path> getFileList() {
        return _data.stream()
                    .map(ObservableFile::getPath)
                    .collect(Collectors.toList());
    }

    /**
     * @param file
     * @return
     */
    public ObservableFile getFile(final Path file) {
        return _files.get(file);
    }
}
