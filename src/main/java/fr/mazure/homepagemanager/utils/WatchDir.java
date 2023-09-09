package fr.mazure.homepagemanager.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Watch a directory (and its sub-directories) for changes to files
 */

public class WatchDir {

    /**
    *
    */
    public enum Event {
        /**
         *
         */
        CREATE,
        /**
         *
         */
        DELETE,
        /**
         *
         */
        UPDATE
    }

    private final Path _path;
    private final WatchService _watcher;
    private final Map<WatchKey,Path> _keys;
    private final Set<String> _ignoredDirectories;
    private final List<FileWatcher> _watchers;

    /**
     * @param path directory to watch
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public WatchDir(final Path path) throws IOException {

        _path = path;
        _watcher = FileSystems.getDefault().newWatchService();
        _keys = new HashMap<>();
        _ignoredDirectories = new HashSet<>();
        _watchers = new ArrayList<>();
    }

    /**
     * @param directoryName sub-directories with this name will be ignored
     * @return the object itself
     */
    public WatchDir ignoreDirectory(final String directoryName) {

        _ignoredDirectories.add(directoryName);
        return this;
    }

    /**
     * @param directoryNames sub-directories with these names will be ignored
     * @return the object itself
     */
    public WatchDir ignoreDirectories(final Iterable<String> directoryNames) {

        for (final String dir: directoryNames) {
            ignoreDirectory(dir);
        }
        return this;
    }

    /**
     * @param matcher
     * @param consummer
     * @return the object itself
     */
    public WatchDir addFileWatcher(final PathMatcher matcher,
                                   final BiConsumer<Path, Event> consummer) {

        _watchers.add(new FileWatcher(matcher, consummer));
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
                if (_ignoredDirectories.contains(path.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                final WatchKey key = path.register(_watcher, StandardWatchEventKinds.ENTRY_CREATE,
                                                             StandardWatchEventKinds.ENTRY_DELETE,
                                                             StandardWatchEventKinds.ENTRY_MODIFY);
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
     * This method never returns
     */
    public void processEvents() {

        try {
            registerAll(_path);
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        for (;;) {

            // wait for key to be signaled
            WatchKey key = null;
            try {
                key = _watcher.take();
            } catch (final InterruptedException e) {
                ExitHelper.exit(e);
            }
            assert(key != null);

            final Path path = _keys.get(key);
            if (path == null) {
                ExitHelper.exit("Unknown key in WatchDir events");
            }
            assert(path != null);

            Logger.log(Logger.Level.TRACE)
                  .append("DBGWATCHDIR -- before WatchEvent loop")
                  .submit();
            for (final WatchEvent<?> event: key.pollEvents()) {
                final WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    ExitHelper.exit("Overflow in WatchDir events");
                }

                final WatchEvent<Path> ev = cast(event);
                final Path name = ev.context();
                final Path child = path.resolve(name);

                // dispatch event
                if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
                    Logger.log(Logger.Level.TRACE)
                          .append("DBGWATCHDIR -- path = ")
                          .append(child)
                          .append(" create")
                          .submit();
                    for (FileWatcher w: _watchers) {
                        w.consume(child, Event.CREATE);
                    }
                } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
                    Logger.log(Logger.Level.TRACE)
                          .append("DBGWATCHDIR -- path = ")
                          .append(child)
                          .append(" delete")
                          .submit();
                    for (FileWatcher w: _watchers) {
                        w.consume(child, Event.DELETE);
                    }
                } else if (event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY)) {
                    Logger.log(Logger.Level.TRACE)
                          .append("DBGWATCHDIR -- path = ")
                          .append(child)
                          .append(" modify")
                          .submit();
                    for (FileWatcher w: _watchers) {
                        w.consume(child, Event.UPDATE);
                    }
                } else {
                    ExitHelper.exit("Unexpected event type in WatchDir events");
                }

                // if directory is created, and watching recursively, then register it and its sub-directories
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, LinkOption.NOFOLLOW_LINKS) && !_ignoredDirectories.contains(path.getFileName().toString())) {
                            registerAll(child);
                        }
                    } catch (final IOException e) {
                        ExitHelper.exit(e);
                    }
                }
            }
            Logger.log(Logger.Level.TRACE)
                  .append("DBGWATCHDIR -- after WatchEvent loop")
                  .submit();

            // reset key and remove from set if directory no longer accessible
            final boolean valid = key.reset();
            if (!valid) {
                _keys.remove(key);
            }
        }
    }

    private class FileWatcher {

        private final PathMatcher _matcher;
        private final BiConsumer<Path, Event> _consummer;

        /**
         * @param matcher
         * @param consummer
         */
        public FileWatcher(final PathMatcher matcher,
                           final BiConsumer<Path, Event> consummer) {
            _matcher = matcher;
            _consummer = consummer;
        }

        public void consume(final Path path,
                            final Event event) {

            if (_matcher.matches(path)) {
                _consummer.accept(path, event);
            }
        }
    }
}