import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

/**
 * Watch a directory (and its sub-directories) for changes to files
 */

public class WatchDir {

    private final Path _path;
    private final WatchService _watcher;
    private final Map<WatchKey,Path> _keys;
    private final Set<String> _ignoredDirectories;
    
    /**
     * @param path directory to watch
     * @throws IOException
     */
    public WatchDir(final Path path) throws IOException {

        _path = path;
        _watcher = FileSystems.getDefault().newWatchService();
        _keys = new HashMap<WatchKey,Path>();
        _ignoredDirectories = new HashSet<String>();
    }

    /**
     * @param directoryName dub-directories with this name will be ignored
     * @return
     */
    public WatchDir ignoreDirectory(final String directoryName) {
        
        _ignoredDirectories.add(directoryName);
        
        return this;
    }
    
    /**
     * register the given directory, and all its sub-directories, with the WatchService
     * @param start directory
     * @throws IOException
     */
    private void registerAll(final Path start) throws IOException {

        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path path, final BasicFileAttributes attrs)
                throws IOException
            {
                if (attrs.isDirectory() && _ignoredDirectories.contains(path.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                final WatchKey key = path.register(_watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                _keys.put(key, path);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Process events
     */
    public void processEvents() {

        try {
            registerAll(_path);
        } catch (final IOException e) {
            ExitHelper.of().exception(e).exit();
        }

        for (;;) {

            // wait for key to be signaled
            WatchKey key = null;
            try {
                key = _watcher.take();
            } catch (final InterruptedException e) {
                ExitHelper.of().exception(e).exit();
            }
            assert(key != null);

            final Path path = _keys.get(key);
            if (path == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                final WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW) {
                    ExitHelper.of().message("Overflow in WatchDir events").exit();
                }

                // Context for directory entry event is the file name of entry
                final WatchEvent<Path> ev = cast(event);
                final Path name = ev.context();
                final Path child = path.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);

                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS) && !_ignoredDirectories.contains(path.getFileName().toString())) {
                            registerAll(child);
                        }
                    } catch (final IOException e) {
                        ExitHelper.of().exception(e).exit();
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                _keys.remove(key);

                // all directories are inaccessible
                if (_keys.isEmpty()) {
                    break;
                }
            }
        }
    }

}