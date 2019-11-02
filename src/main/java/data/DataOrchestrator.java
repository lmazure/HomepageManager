package data;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import utils.ExitHelper;

public class DataOrchestrator {

    final static private String s_markerFile = "google1b78f05130a6dbb0.html";
    final static PathMatcher _matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.xml");
    final static List<String> _ignoredDirectories = new ArrayList<>(List.of( ".svn",
                                                                             ".git",
                                                                             ".vscode",
                                                                             "node_modules",
                                                                             "cap_fichiers",
                                                                             "cmm_fichiers" ));

    final private Path _homepagePath;
    final private FileTracker _fileTracker;

    public DataOrchestrator(final Path homepagePath,
                            final FileExistenceHandler handler,
                            final List<FileHandler> fileHandlers) {
        _homepagePath = homepagePath;
        _fileTracker = new FileTracker(handler);
        for (final FileHandler fh: fileHandlers) {
            _fileTracker.addFileHandler(fh);
        }
    }
    
    public void start() {
        
        if (!(new File(_homepagePath + File.separator + s_markerFile)).exists()) {
            ExitHelper.exit(_homepagePath + " does not contain the homepage");
        }

        try {
            recordFilesExistingAtStartup(_matcher);

            new WatchDir(_homepagePath).ignoreDirectories(_ignoredDirectories)
                                       .addFileWatcher(_matcher, (final Path p, final WatchDir.Event e) -> dispatchEvent(p, e))
                                       .processEvents();
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
    }

    private void recordFilesExistingAtStartup(final PathMatcher matcher) throws IOException {

        Files.walkFileTree(_homepagePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path path, final BasicFileAttributes attrs)
                throws IOException
            {
                if (_ignoredDirectories.contains(path.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(final Path path,
                                             final BasicFileAttributes attrs) throws IOException {
                if (matcher.matches(path)) {
                    _fileTracker.addFile(path);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    private void dispatchEvent(final Path path,
                               final WatchDir.Event event) {
        
        switch (event) {
            case CREATE:
                _fileTracker.handleFileCreation(path);
                break;
            case DELETE:
                _fileTracker.handleFileDeletion(path);
                break;
            default:
                ExitHelper.exit("Unknwown event");
                break;
        }
    }
}
