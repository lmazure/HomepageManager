package fr.mazure.homepagemanager.data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.Logger;

/**
 * Queue dispatching the file events to the file handlers
 */
public class FileEventQueue {

    private final Map<Path, EventType> _queue;
    private final List<FileHandler> _fileHandlers;
    private final Set<Path> _filesBeingProcessed;
    private final int NB_THREADS = 4;

    /**
     * Type of a file event
     */
    public enum EventType {
        /**
         * the file has been created
         */
        CREATE,
        /**
         * the file has been deleted
         */
        DELETE,
        /**
         * the file has been updated
         */
        UPDATE
    }

    /**
     * Constructor
     *
     * @param fileHandlers handlers processing the files
     */
    public FileEventQueue(final List<FileHandler> fileHandlers) {
        _queue = new HashMap<>();
        _fileHandlers = fileHandlers;
        _filesBeingProcessed = new HashSet<>();
        for (int i = 0; i < NB_THREADS; i++) {
            final Consumer consumer = new Consumer(this);
            final String threadName = "file-event-dispatch-" + String.format("%03d", Integer.valueOf(i));
            final Thread thread = new Thread(consumer, threadName);
            thread.start();
        }
    }

    /**
     * Insert an event in the queue
     *
     * @param file file
     * @param type type of the event
     */
    public void insertEvent(final Path file,
                            final EventType type) {
        synchronized (_queue) {

            final EventType presentType = _queue.get(file);

            if (presentType == null) {
                _queue.put(file, type);
                return;
            }

            switch (type) {
            case CREATE:
                if (presentType != EventType.DELETE) {
                    ExitHelper.exit("file " + file + " is created while it was not deleted before");
                }
                _queue.put(file, EventType.CREATE);
                return;
            case DELETE:
                if (presentType == EventType.DELETE) {
                    ExitHelper.exit("file " + file + " is deleted while it was already deleted before");
                }
                _queue.put(file, EventType.DELETE);
                return;
            case UPDATE:
                if (presentType == EventType.DELETE) {
                    ExitHelper.exit("file " + file + " is updated while it was already deleted before");
                }
                // do nothing
                return;
            default:
                // NOT REACHABLE
                break;
            }
        }
    }

    /**
     * @return event to be handled, null if there is none
     */
    public Event popEvent() {
        synchronized (_queue) {
           final Optional<Path> path = _queue.keySet().stream().filter(p -> !_filesBeingProcessed.contains(p)).findFirst();
           if (path.isEmpty()) {
               return null;
           }

           final Event event = new Event(path.get(), _queue.get(path.get()));
           _queue.remove(path.get());
           _filesBeingProcessed.add(event.getFile());
           return event;
        }
    }

    /**
     * Indicate that the event of a file has been handled
     *
     * @param path path of the file whose event has been handled
     */
    public void eventHasBeenHandled(final Path path) {
       synchronized (_queue) {
        _filesBeingProcessed.remove(path);
       }
    }

    private void handleFileCreation(final Path file) {

        Logger.log(Logger.Level.INFO)
              .append("created file: " + file)
              .submit();

        for (final FileHandler h: _fileHandlers) {
            h.handleCreation(file);
        }
    }

    private void handleFileDeletion(final Path file) {
        Logger.log(Logger.Level.INFO)
        .append("deleted file: " + file)
        .submit();

        for (final FileHandler h: _fileHandlers) {
            h.handleDeletion(file);
        }
    }

    private class Event {

        private final Path _file;
        private final EventType _type;

        /**
         * Constructor
         *
         * @param file file
         * @param type type of the event
         */
        public Event(final Path file,
                     final EventType type) {
            _file = file;
            _type = type;
        }

        /**
         * @return the file
         */
        public Path getFile() {
            return _file;
        }

        /**
         * @return type of the event
         */
        public EventType getType() {
            return _type;
        }
    }

    private class Consumer implements Runnable {

        private final FileEventQueue _fileQueue;

        /**
         * Constructor
         *
         * @param queue queue from which the events are popped
         */
        public Consumer(final FileEventQueue queue) {
            _fileQueue = queue;
        }

        @Override
        public void run() {
            while (true) {
                final Event e = _fileQueue.popEvent();
                if (e == null) {
                    try {
                        Thread.sleep(100);
                    } catch (final InterruptedException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } // TODO stupid!!!
                    continue;
                }
                switch (e.getType()) {
                case CREATE:
                    _fileQueue.handleFileCreation(e.getFile());
                    break;
                case DELETE:
                    _fileQueue.handleFileDeletion(e.getFile());
                    break;
                case UPDATE:
                    _fileQueue.handleFileDeletion(e.getFile());
                    _fileQueue.handleFileCreation(e.getFile());
                    break;
                default:
                    // NOT REACHABLE
                    break;
                }
                _fileQueue.eventHasBeenHandled(e.getFile());
            }
        }
    }
}
