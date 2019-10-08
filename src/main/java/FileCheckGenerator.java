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
            final byte[] encoded = Files.readAllBytes(file);
            final String content = new String(encoded, StandardCharsets.UTF_8);
            final List<Error> errors = check(file, content);
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

    public List<Error> check(final Path file, final String content) {  //TODO see how to test this method while keeping it private
        final List<Error> errors =  new ArrayList<Error>();
        errors.addAll(checkFileBom(content));
        errors.addAll(checkCharacters(content));
        errors.addAll(checkPath(file, content));
        return errors;
    }

    private List<Error> checkFileBom(final String str) {
        
        final List<Error> errors = new ArrayList<Error>();
        if (str.startsWith(UTF8_BOM)) {
            errors.add(new Error(1, "file should not have a UTF BOM"));
        }
        return errors;
    }

    private List<Error> checkCharacters(final String str) {

        final List<Error> errors = new ArrayList<Error>();

        boolean isPreviousCharacterCarriageReturn = false;
        boolean isPreviousCharacterWhiteSpace = false;
        boolean isCurrentCharacterCarriageReturn = false;
        boolean isLineEmpty = true;
        int lineNumber = 1;
        
        for (int i = 0; i<str.length(); i++) {
            isPreviousCharacterCarriageReturn = isCurrentCharacterCarriageReturn;
            final char ch = str.charAt(i);
            if (ch == '\r') {
                isCurrentCharacterCarriageReturn = true;                            
            } else if (Character.isWhitespace(ch)) {
                isCurrentCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = true;
            } else if (Character.isISOControl(ch)) {
                isCurrentCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = false;
                errors.add(new Error(lineNumber, "line contains a control character"));                                        
            } else if (ch == '\n') {
                isCurrentCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = false;
                if (!isPreviousCharacterCarriageReturn) {
                    errors.add(new Error(lineNumber, "line should finished by \\r\\n instead of \\n"));                
                }
                if (isPreviousCharacterWhiteSpace) {
                    errors.add(new Error(lineNumber, "line is finishing with a white space"));                
                }
                if (isLineEmpty) {
                    errors.add(new Error(lineNumber, "empty line"));                
                }
                lineNumber++;
                isLineEmpty = true;
            } else {
                isCurrentCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = false;
                isLineEmpty = false;                    
            }
        }
                
        return errors;
    }
    
    private List<Error> checkPath(final Path file, final String content) { // TODO realy check that the 5th line is correct

        final List<Error> errors = new ArrayList<Error>();

        try {
            final String filename = file.toFile().getCanonicalPath();
            final int lastSeparatorPosition = filename.lastIndexOf(File.separator);
            final int previousSeparatorPosition = filename.lastIndexOf(File.separator, lastSeparatorPosition - 1);
            final String endOfFilename = filename.substring(previousSeparatorPosition + 1);            
            if (!content.contains("<PATH>" + endOfFilename.replace(File.separator, "/") + "</PATH>")) {
                errors.add(new Error(5, "the name of the file does not appear in the <PATH> node"));                            
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
                
        return errors;
    }
    
    @Override
    public Status handleDeletion(final Path file) { //TODO see how to test this class while keeping this type private

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
    
    static public class Error {
        
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
