import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {

    final static private String s_markerFile = "google1b78f05130a6dbb0.html";
    final static PathMatcher _matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.xml");
    
    final private FileTracker _fileTracker;

    public static void main(final String[] args) {
        
        if (args.length != 1) {
            ExitHelper.exit("Syntax: HomepageManager <homepage directory>");
        }
 
        final Main main = new Main();
        main.start(Paths.get(args[0]));
    }

    public Main() {
        _fileTracker = new FileTracker();
    }
    
    private void start(final Path homepagePath) {

        if (!(new File(homepagePath + File.separator + s_markerFile)).exists()) {
            ExitHelper.exit(homepagePath + " does not contain the homepage");
        }

        try {
            recordFilesExistingAtStartup(homepagePath, _matcher);

            new WatchDir(homepagePath).ignoreDirectory(".svn")
                                      .ignoreDirectory(".git")
                                      .ignoreDirectory(".vscode")
                                      .ignoreDirectory("node_modules")
                                      .addFileWatcher(_matcher, (final Path p, final WatchDir.Event e) -> dispatchEvent(p, e))
                                      .processEvents();
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        System.out.println("Done!");
    }

    private void recordFilesExistingAtStartup(final Path homepagePath, final PathMatcher matcher) throws IOException {
        
        Files.walkFileTree(homepagePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(final Path path, final BasicFileAttributes attrs)
                throws IOException
            {
                if (attrs.isRegularFile() && matcher.matches(path)) {
                    _fileTracker.addFile(path);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }
    
    private void dispatchEvent(final Path path, final WatchDir.Event event) {
        
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
