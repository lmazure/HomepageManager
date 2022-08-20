package data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import data.linkchecker.LinkCheckRunner;
import utils.ExitHelper;
import utils.FileHelper;

public class LinkChecker implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final String _cacheFolderName;
    private final BackgroundDataController _controller;
    private final Map<Path, LinkCheckRunner> _handlers;

    /**
     * This class checks the links appearing in the XML files.
     *
     * @param homepagePath
     * @param tmpPath
     */
    public LinkChecker(final Path homepagePath,
                       final Path tmpPath,
                       final String cacheFolderName,
                       final BackgroundDataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _cacheFolderName = cacheFolderName;
        _controller = controller;
        _handlers = new HashMap<>();
    }

    @Override
    public void handleCreation(final Path file) {

        if (_handlers.get(file) != null) {
            ExitHelper.exit("there is already a handler for file " + file);
        }

        final LinkCheckRunner handler = new LinkCheckRunner(file, _tmpPath.resolve(_cacheFolderName), _controller, getOutputFile(file), getReportFile(file));
        _handlers.put(file, handler);
        handler.launch();
    }

    @Override
    public void handleDeletion(final Path file) {

        final LinkCheckRunner handler = _handlers.get(file);
        if (handler != null) {
            handler.cancel();
            _handlers.remove(file);
        }
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_linkcheck", "txt");
    }

    @Override
    public Path getReportFile(final Path file) {
         return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_linkcheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }
}
