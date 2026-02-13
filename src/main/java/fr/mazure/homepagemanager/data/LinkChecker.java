package fr.mazure.homepagemanager.data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import fr.mazure.homepagemanager.data.linkchecker.LinkCheckRunner;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileNameHelper;

/**
 * This class checks the links appearing in XML files.
 *
 */
public class LinkChecker implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final String _cacheFolderName;
    private final BackgroundDataController _controller;
    private final ViolationDataController _violationController;
    private final Map<Path, LinkCheckRunner> _handlers;

    private static final String s_checkType = "link";

    /**
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     * @param cacheFolderName name of the cache folder (which will be in the tmpPath directory)
     * @param controller controller to notify of additional / removed violations
     * @param violationController controller to notify of additional / removed violations
     */
    public LinkChecker(final Path homepagePath,
                       final Path tmpPath,
                       final String cacheFolderName,
                       final BackgroundDataController controller,
                       final ViolationDataController violationController) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _cacheFolderName = cacheFolderName;
        _controller = controller;
        _violationController = violationController;
        _handlers = new HashMap<>();
    }

    @Override
    public void handleCreation(final Path file) {

        if (_handlers.get(file) != null) {
            ExitHelper.exit("there is already a handler for file " + file);
        }

        final LinkCheckRunner handler = new LinkCheckRunner(file, _tmpPath.resolve(_cacheFolderName), _controller, _violationController, s_checkType, getOutputFile(file), getReportFile(file));
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
        _violationController.remove(v -> (v.getFile().equals(file.toString()) && v.getType().equals(s_checkType)));
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileNameHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_linkcheck", "txt");
    }

    @Override
    public Path getReportFile(final Path file) {
         return FileNameHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_linkcheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }
}
