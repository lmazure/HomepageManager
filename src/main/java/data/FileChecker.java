package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import utils.ExitHelper;
import utils.FileHelper;

/**
 * Manage the creation of the HTML files
 *
 */
public class FileChecker implements FileHandler {

    static final private String UTF8_BOM = "\uFEFF";
    
    final private Path _homepagePath;
    final private Path _tmpPath;
    final private DataController _controller;
    final private Validator _validator;

    /**
     * This class checks the characters of the XML files.
     * 
     * @param homepagePath
     * @param tmpPath
     */
    public FileChecker(final Path homepagePath,
                       final Path tmpPath,
                       final DataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        
        final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final File schemaLocation = new File(homepagePath
                                             + File.separator
                                             + "css"
                                             + File.separator
                                             + "schema.xsd");
        Schema schema = null;
        
        try {
            schema = factory.newSchema(schemaLocation);
        } catch (final SAXException e) {
            ExitHelper.exit(e);
        }

        assert(schema != null);
        _validator = schema.newValidator();
    }
    
    @Override
    public Status handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;
        
        try (final FileReader fr = new FileReader(file.toFile());
             final BufferedReader br = new BufferedReader(fr);
             final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            final byte[] encoded = Files.readAllBytes(file);
            final String content = new String(encoded, StandardCharsets.UTF_8);
            final List<Error> errors = check(file, content);
            if (!errors.isEmpty() ) {
                status = Status.HANDLED_WITH_ERROR;
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
            System.out.println(getOutputFile(file).toFile() + " is generated");
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
        return status;
    }

    public List<Error> check(final Path file,
                             final String content) {  //TODO see how to test this method while keeping it private
        final List<Error> errors =  new ArrayList<Error>();
        errors.addAll(checkFileBom(content));
        errors.addAll(checkCharacters(content));
        errors.addAll(checkPath(file, content));
        errors.addAll(checkSchema(content));
        errors.addAll(checkEventNumberOfSpaces(content));
        return errors;
    }

    private List<Error> checkFileBom(final String content) {
        
        final List<Error> errors = new ArrayList<Error>();
        if (content.startsWith(UTF8_BOM)) {
            errors.add(new Error(1, "file should not have a UTF BOM"));
        }
        return errors;
    }

    private List<Error> checkCharacters(final String content) {

        final List<Error> errors = new ArrayList<Error>();

        boolean isPreviousCharacterCarriageReturn = false;
        boolean isPreviousCharacterWhiteSpace = false;
        boolean isLineEmpty = true;
        int lineNumber = 1;
        
        for (int i = 0; i < content.length(); i++) {
            final char ch = content.charAt(i);
            if (ch == '\r') {
                isPreviousCharacterCarriageReturn = true;                            
            } else if (ch == '\n') {
                if (!isPreviousCharacterCarriageReturn) {
                    errors.add(new Error(lineNumber, "line should finish by \\r\\n instead of \\n"));                
                }
                if (isPreviousCharacterWhiteSpace) {
                    errors.add(new Error(lineNumber, "line is finishing with a white space"));                
                }
                if (isLineEmpty) {
                    errors.add(new Error(lineNumber, "empty line"));                
                }
                lineNumber++;
                isLineEmpty = true;
                isPreviousCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = false;
            } else if (Character.isISOControl(ch)) {
                isPreviousCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = Character.isWhitespace(ch);
                errors.add(new Error(lineNumber, "line contains a control character"));                                        
            } else if (Character.isWhitespace(ch)) {
                isPreviousCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = true;
            } else {
                isPreviousCharacterCarriageReturn = false;                            
                isPreviousCharacterWhiteSpace = false;
                isLineEmpty = false;                    
            }
        }

        if (isPreviousCharacterWhiteSpace) {
            errors.add(new Error(lineNumber, "line is finishing with a white space"));                
        }
        if (isLineEmpty) {
            errors.add(new Error(lineNumber, "empty line"));                
        }

        return errors;
    }
    
    private List<Error> checkPath(final Path file,
                                  final String content) { // TODO really check that the 5th line is correct

        final List<Error> errors = new ArrayList<Error>();

        try {
            final String filename = file.toFile().getCanonicalPath();
            final int lastSeparatorPosition = filename.lastIndexOf(File.separator);
            final int previousSeparatorPosition = filename.lastIndexOf(File.separator, lastSeparatorPosition - 1);
            final String endOfFilename = filename.substring(previousSeparatorPosition + 1);
            final String pathString = "<PATH>" + endOfFilename.replace(File.separator, "/") + "</PATH>"; 
            if (!content.contains(pathString)) {
                errors.add(new Error(5, "the name of the file does not appear in the <PATH> node (expected to see \"" + pathString + "\")"));                            
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
                
        return errors;
    }

    private List<Error> checkEventNumberOfSpaces(final String content) {
        
        final List<Error> errors = new ArrayList<Error>();
        
        int n=0;
        for (final String line : content.lines().toArray(String[]::new)) {
            n++;
            if (numberOfWhiteCharactersAtBeginning(line) % 2 == 1) {
                errors.add(new Error(n, "odd number of spaces at the beginning of the line"));                                            
            }
        }

        return errors;
    }

    private List<Error> checkSchema(final String content) {

        final List<Error> errors = new ArrayList<Error>();
        
        final Source source = new StreamSource(new StringReader(content));
        
        try {
            _validator.validate(source);
        } catch (final SAXException e) {
            errors.add(new Error(0, "the file violates the schema (\"" + e.toString() + "\")"));                            
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
                
        return errors;
    }
    
    @Override
    public Status handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));
        
        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));

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
    
    private int numberOfWhiteCharactersAtBeginning(final String str) {
    
        int n = 0;
        while ((n < str.length()) && (str.charAt(n) == ' ')) n++;
        return n;
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
