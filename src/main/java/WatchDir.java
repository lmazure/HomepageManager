import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;

/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir {

    private final Path _path;
    private final WatchService _watcher;
    private final Map<WatchKey,Path> _keys;
    private final Set<String> _ignoredDirectories;
    
    public WatchDir(final Path path) throws IOException {

        _path = path;
        _watcher = FileSystems.getDefault().newWatchService();
        _keys = new HashMap<WatchKey,Path>();
        _ignoredDirectories = new HashSet<String>();
    }

    public WatchDir ignoreDirectory(final String directory) {
        
        _ignoredDirectories.add(directory);
        
        return this;
    }
    
    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
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
                register(path);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(final Path path) throws IOException {
        final WatchKey key = path.register(_watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        
        // TODO delete line below --------------
        Path prev = _keys.get(key);
        if (prev == null) {
            System.out.format("register: %s\n", path);
        } else {
            if (!path.equals(prev)) {
                System.out.format("update: %s -> %s\n", prev, path);
            }
        }
        // TODO delete line above --------------

        _keys.put(key, path);
    }


    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }


    /**
     * Process all events for _keys queued to the _watcher
     */
    public void processEvents() {

        try {
            registerAll(_path);
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        for (;;) {

            // wait for key to be signaled
            WatchKey key = null;
            try {
                key = _watcher.take();
            } catch (final InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
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
                    System.err.println(Thread.currentThread().getStackTrace().toString());
                    System.exit(1);
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
                        e.printStackTrace();
                        System.exit(1);
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