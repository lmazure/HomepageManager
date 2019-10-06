import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manage the creation of the HTML files
 *
 */
public class FileCheckGenerator implements FileHandler {

    static final private String UTF8_BOM = "\uFEFF";
    
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
            final List<Error> errors =  new ArrayList<Error>();
            final byte[] encoded = Files.readAllBytes(file);
            final String content = new String(encoded, StandardCharsets.UTF_8);
            errors.addAll(checkFileBom(content));
            errors.addAll(checkNewLine(content));
            errors.addAll(checkPath(file, content));
            if (!errors.isEmpty() ) {
                System.out.println(file);
            }
            for (final Error error: errors) {
                final String message = "line " + error.getLineNumber() + ": " + error.getErrorMessage(); 
                System.err.println(message);
                pw.println(message);
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

    private List<Error> checkFileBom(final String str) {
        
        final List<Error> errors = new ArrayList<Error>();
        if (str.startsWith(UTF8_BOM)) {
            errors.add(new Error(0, "file has a UTF BOM"));
        }
        return errors;
    }

    private List<Error> checkNewLine(final String str) {

        final List<Error> errors = new ArrayList<Error>();

        boolean isPreviousCharacterCarriageReturn = false;
        boolean isCurrentCharacterCarriageReturn = false;
        boolean isLineEmpty = true;
        int lineNumber = 1;
        
        for (int i=0; i<str.length(); i++) {
            isPreviousCharacterCarriageReturn = isCurrentCharacterCarriageReturn;
            if (str.charAt(i) == '\r') {
                isCurrentCharacterCarriageReturn = true;                            
            } else {
                isCurrentCharacterCarriageReturn = false;                            
                if (str.charAt(i) == '\n') {
                    if (!isPreviousCharacterCarriageReturn) {
                        errors.add(new Error(lineNumber, "line should finished by \\r\\n instead of \\n"));                
                    }
                    if (isLineEmpty) {
                        errors.add(new Error(lineNumber, "empty line"));                
                    }
                    lineNumber++;
                    isLineEmpty = true;
                } else {
                    //TODO check for control characters
                    isLineEmpty = false;
                }
            }
        }
                
        return errors;
    }
    
    private List<Error> checkPath(final Path file, final String content) {

        final List<Error> errors = new ArrayList<Error>();

        try {
            final String filename = file.toFile().getCanonicalPath();
            final int lastSeparatorPosition = filename.lastIndexOf(File.separator);
            final int previousSeparatorPosition = filename.lastIndexOf(File.separator, lastSeparatorPosition - 1);
            final String endOfFilename = filename.substring(previousSeparatorPosition + 1);            
            if (!content.contains("<PATH>" + endOfFilename.replace(File.separator, "/") + "</PATH>")) {
                errors.add(new Error(0, "the name of the file does not appear in the <PATH> node"));                            
            }
        } catch (final IOException e) {
            e.printStackTrace();
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
    
    private class Error {
        
        final private int _lineNumber;
        final private String _errorMessage;

        public Error(final int lineNumber, final String errorMessage) {
            _lineNumber = lineNumber;
            _errorMessage = errorMessage;
        }

        public int getLineNumber() {
            return _lineNumber;
        }

        public String getErrorMessage() {
            return _errorMessage;
        }
        
    }
}
