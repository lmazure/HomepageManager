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
import utils.Logger;

public class DataOrchestrator {

    final static private String s_markerFile = "google1b78f05130a6dbb0.html"; // TODO this should not be hardcoded
    final static PathMatcher _matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.xml");
    final static List<String> _ignoredDirectories = new ArrayList<>(List.of( ".svn",
                                                                             ".git",
                                                                             ".vscode",
                                                                             "sitemap",  // TODO this directory name also appears in SiteFilesGenerator
                                                                             "node_modules",
                                                                             "cap_fichiers",
                                                                             "cmm_fichiers" ));

    private final Path _homepagePath;
    private final List<FileHandler> _fileHandlers;
    private final FileExistenceHandler _handler;

    public DataOrchestrator(final Path homepagePath,
                            final FileExistenceHandler handler,
                            final List<FileHandler> fileHandlers) {
        _homepagePath = homepagePath;
        _fileHandlers = fileHandlers;
        _handler = handler;
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
        } catch (final Exception e) {
            // catch all exceptions, otherwise JavaFX will swallow it and it will be a nightmare to debug
            ExitHelper.exit(e);
        }
    }

    private void recordFilesExistingAtStartup(final PathMatcher matcher) throws IOException {

        Files.walkFileTree(_homepagePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path path,
                                                     final BasicFileAttributes attrs)
            {
                if (_ignoredDirectories.contains(path.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(final Path path,
                                             final BasicFileAttributes attrs)
            {
                if (matcher.matches(path)) {
                    addFile(path);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        Logger.log(Logger.Level.INFO)
              .append("visited all files")
              .submit();
    }

    private void addFile(final Path file) {
        
        _handler.handleCreation(file);
        
        for (final FileHandler h: _fileHandlers) {
            if (h.outputFileMustBeRegenerated(file)) {
                h.handleDeletion(file);
                h.handleCreation(file);                
            }
        }
    }
    
    private void dispatchEvent(final Path path,
                               final WatchDir.Event event) {
        
        switch (event) {
            case CREATE:
                handleFileCreation(path);
                break;
            case DELETE:
                handleFileDeletion(path);
                break;
            default:
                ExitHelper.exit("Unknwown event");
                break;
        }
    }

    private void handleFileCreation(final Path file) {
        _handler.handleCreation(file);
        for (final FileHandler h: _fileHandlers) {
            h.handleCreation(file);
        }
    }
    
    private void handleFileDeletion(final Path file) {
        _handler.handleDeletion(file);
        for (final FileHandler h: _fileHandlers) {
            h.handleDeletion(file);
        }
    }
}
