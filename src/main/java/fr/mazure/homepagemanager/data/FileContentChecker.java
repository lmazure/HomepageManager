package fr.mazure.homepagemanager.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import fr.mazure.homepagemanager.data.filechecker.FileChecker;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.FileNameHelper;
import fr.mazure.homepagemanager.utils.Logger;

/**
 * This class checks the text appearing in XML files (buit without interpreting the XML, the XML content is verified by NodeValueChecker).
 */
public class FileContentChecker implements FileHandler { // TODO should be split several checkers (and these one put in their own namespace)


    private static final String s_checkType = "file";

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final DataController _controller;
    private final ViolationDataController _violationController;
    private final FileChecker _checker;

    /**
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     * @param controller controller to notify of additional / removed violations
     * @param violationController controller to notify of additional / removed violations
     */
    public FileContentChecker(final Path homepagePath,
                              final Path tmpPath,
                              final DataController controller,
                              final ViolationDataController violationController) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _violationController = violationController;
        _checker = new FileChecker(homepagePath);
    }

    @Override
    public void handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;

        FileHelper.createParentDirectory(getOutputFile(file));

        final List<Error> errors = _checker.check(file);
        if (!errors.isEmpty()) {
            status = Status.HANDLED_WITH_ERROR;
        }

        try (final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            if (status == Status.HANDLED_WITH_SUCCESS) {
                // must write something in the file otherwise its last modification datetime will be incorrect
                pw.println("OK");
            } else {
                for (final Error error: errors) {
                    final String message = "line " + error.lineNumber() + ": " + error.errorMessage();
                    pw.println(message);
                    _violationController.add(new Violation(file.toString(),
                                                           s_checkType,
                                                           error.checkName(),
                                                           (error.lineNumber() > 0) ? new ViolationLocationLine(error.lineNumber())
                                                                                    : new ViolationLocationUnknown(),
                                                           error.errorMessage(),
                                                           Optional.empty()));
                }
            }
            Logger.log(Logger.Level.INFO)
                  .append(getOutputFile(file))
                  .append(" is generated")
                  .submit();
        } catch (final Exception e) {
            final Path reportFile = getReportFile(file);
            FileHelper.createParentDirectory(reportFile);
            try (final PrintStream reportWriter = new PrintStream(reportFile.toFile())) {
                e.printStackTrace(reportWriter);
            } catch (final IOException e2) {
                ExitHelper.exit(e2);
            }
            status = Status.FAILED_TO_HANDLE;
        }

        _controller.handleCreation(file, status, getOutputFile(file), getReportFile(file));
    }


    @Override
    public void handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));

        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));

        _violationController.remove(v -> (v.getFile().equals(file.toString()) && v.getType().equals(s_checkType)));
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileNameHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_filecheck", "txt");
    }

    @Override
    public Path getReportFile(final Path file) {
         return FileNameHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_filecheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {

        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }

    /**
     * @param checkName Name of the check
     * @param lineNumber Line number of the violation
     * @param errorMessage Message describing the violation
     */
    public static record Error(String checkName, int lineNumber, String errorMessage) {}
}
