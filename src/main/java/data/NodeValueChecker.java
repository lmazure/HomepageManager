package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import data.nodechecker.Logger;
import data.nodechecker.ParseManager;
import utils.ExitHelper;
import utils.FileHelper;

public class NodeValueChecker implements FileHandler, Logger {

    final private Path _homepagePath;
    final private Path _tmpPath;
    final private DataController _controller;
    PrintWriter _pw;
    boolean _hasError;
    
    /**
     * This class checks the characters of the XML files.
     * 
     * @param homepagePath
     * @param tmpPath
     */
    public NodeValueChecker(final Path homepagePath,
                              final Path tmpPath,
                              final DataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
    }
    
    @Override
    public Status handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;
        
        try (final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            _pw = pw; // TODO fix this crap
            _hasError = false;
            final Set<Logger> loggers = new HashSet<Logger>();
            loggers.add(this);
            final ParseManager pm = new ParseManager(loggers);
            pm.parse(file.toFile());
            if (_hasError) {
                status = Status.HANDLED_WITH_ERROR;
            }
        } catch (final Exception e) {
            final Path reportFile = getReportFile(file);
            FileHelper.createParentDirectory(reportFile);
            try (final PrintStream reportWriter = new PrintStream(reportFile.toFile())) {
                e.printStackTrace(reportWriter);
            } catch (final IOException e2) {
                ExitHelper.exit(e2);
            }
            status = Status.FAILED_TO_HANDLED;                
        }
           
         _controller.handleCreation(file, status, getOutputFile(file), getReportFile(file));
        
        _controller.handleCreation(file, status, getOutputFile(file), getReportFile(file));
        return status;
    }
    
    @Override
    public Status handleDeletion(final Path file) { //TODO see how to test this class while keeping this type private

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));
        
        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));

        return Status.HANDLED_WITH_SUCCESS;
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_nodevaluecheck", "txt");
    }
    
    @Override
    public Path getReportFile(final Path file) {
         return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_nodevaluecheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        
        /*if (!getOutputFile(file).toFile().isFile()
                || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified())) {
            System.out.println("----- BEGIN DEBUG");
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            System.out.println("source file = " + file);
            System.out.println("target file = " + getOutputFile(file));
            System.out.println("source file timestamp = " + df2.format(file.toFile().lastModified()));
            System.out.println("target file timestamp = " + df2.format(getOutputFile(file).toFile().lastModified()));
            System.out.println("----- END DEBUG");
        }*/
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }

    @Override
    public void record(final File file, 
                       final String tag,
                       final String value,
                       final String violation,
                       final String detail) {
        _hasError = true;
        _pw.println("file = " + file +
                " tag = " + tag +
                " value = " + value +
                " violation = " + violation +
                " detail = " + detail);
    }
}
