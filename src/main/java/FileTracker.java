import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileTracker {

    final Map<Path, TrackedFile> _files;
    
    public FileTracker() {
        
        _files= new HashMap<>();
    }
    
    public void addFile(final Path file) {
    
        if (_files.containsKey(file)) {
            ExitHelper.of().message("Duplicated path");
        }
        
        _files.put(file, new TrackedFile(file));
    }
    
    public void handleFileCreation(final Path file) {

        TrackedFile f = _files.get(file);
        if (f == null) {
            f= new TrackedFile(file);
            _files.put(file, f);
        }

        if (!f.isDeleted()) {
            ExitHelper.of().message("Creating a file that currently exists");
        }
        
        f.setCreated();
        
        createHtmlFile(file);
    }
    
    public void handleFileDeletion(final Path file) {

        final TrackedFile f = _files.get(file);
        if (f == null) {
            ExitHelper.of().message("Unknown file");
        }
        assert (f != null);

        if (f.isDeleted()) {
            ExitHelper.of().message("Deleting a file that currently does not exist");
        }
        
        f.setDeleted();
        
        deleteHtmlFile(file);
    }
    
    private void createHtmlFile(final Path file) {
        System.out.println("===> create " + getHtmlFilename(file));
    }

    private void deleteHtmlFile(final Path file) {
        System.out.println("===> delete " + getHtmlFilename(file));
    }

    public void dump(final PrintStream stream) {
        for (Path p: _files.keySet()) {
            _files.get(p).dump(stream);
            stream.print("-------------------------------------");
        }
    }

    private Path getHtmlFilename(final Path file) {
        
        final String s = file.toString();
        return Paths.get(s.substring(0, s.length() - 4).concat(".htmlT"));
    }

}
