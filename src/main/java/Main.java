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
    
    final private Path _homepagePath;
    final private Path _tmpPath;
    final private FileTracker _fileTracker;

    public static void main(final String[] args) {
        
        if (args.length != 2) {
            ExitHelper.exit("Syntax: HomepageManager <homepage directory> <tmp directory>");
        }
 
        final Path homepagePath = Paths.get(args[0]);
        final Path tmpPath = Paths.get(args[1]);
        final Main main = new Main(homepagePath, tmpPath);
        main.start();
    }

    public Main(final Path homepagePath, final Path tmpPath) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _fileTracker = new FileTracker();
        _fileTracker.AddFileHandler(new HTMLFileGenerator(_homepagePath, _tmpPath));
    }
    
    private void start() {

        if (!(new File(_homepagePath + File.separator + s_markerFile)).exists()) {
            ExitHelper.exit(_homepagePath + " does not contain the homepage");
        }

        try {
            recordFilesExistingAtStartup(_matcher);

            new WatchDir(_homepagePath).ignoreDirectory(".svn")
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

    private void recordFilesExistingAtStartup(final PathMatcher matcher) throws IOException {
        
        Files.walkFileTree(_homepagePath, new SimpleFileVisitor<Path>() {
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
