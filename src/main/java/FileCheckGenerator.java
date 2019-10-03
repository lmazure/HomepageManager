import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage the creation of the HTML files
 *
 */
public class FileCheckGenerator implements FileHandler {

    final private Path _homepagePath;
    final private Path _tmpPath;
    
    public FileCheckGenerator(final Path homepagePath, final Path tmpPath) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
    }
    
    @Override
    public Status handleCreation(final Path file) {


        try (final FileReader fr = new FileReader(file.toFile());
             final BufferedReader br = new BufferedReader(fr);
             final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            final Iterable<String> errors = checkFile(br);
            pw.println("hello");
            for (final String error: errors) {
                pw.println(error);
            }
            pw.flush();
            os.flush();
            os.getFD().sync();
        } catch (final Exception e) {
            final Path reportFile = getReportFile(file);
            FileHelper.createParentDirectory(reportFile);
            try (final PrintStream reportWriter = new PrintStream(reportFile.toFile())) {
                e.printStackTrace(reportWriter);
            } catch (final IOException e2) {
                ExitHelper.exit(e2);
            }
            return Status.FAILED_TO_HANDLED;                
        }
            
        return Status.HANDLED_WITH_SUCCESS;
    }

    private Iterable<String> checkFile(final BufferedReader br) throws IOException {
        
        final List<String> errors = new ArrayList<String>();
        
        int n = 1;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.length() == 0) {
                errors.add("line " + n + ": empty line");
            }
            n++;
        }
        
        return errors;
    }

    @Override
    public Status handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));
        
        return Status.HANDLED_WITH_SUCCESS;
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_filecheck", "txt");
    }
    
    @Override
    public Path getReportFile(final Path file) {
         return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_filecheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        
        if (!getOutputFile(file).toFile().isFile()
                || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified())) {
            System.out.println("----- BEGIN DEBUG");
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            System.out.println("source file = " + file);
            System.out.println("target file = " + getOutputFile(file));
            System.out.println("source file timestamp = " + df2.format(file.toFile().lastModified()));
            System.out.println("target file timestamp = " + df2.format(getOutputFile(file).toFile().lastModified()));
            System.out.println("----- END DEBUG");
        }
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }
}
