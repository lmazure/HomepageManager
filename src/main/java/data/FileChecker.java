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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import utils.ExitHelper;
import utils.FileHelper;
import utils.Logger;
import utils.XmlHelper;

/**
 * Manage the creation of the HTML files
 *
 */
public class FileChecker implements FileHandler {

    private static final String UTF8_BOM = "\uFEFF";
    private static final Pattern s_badGreaterThan = Pattern.compile("<[^>]*>");
    private static final Pattern s_spaceInTags = Pattern.compile("(< [^>]*>|<[^>]* >)");
    private static final Pattern s_spaceBetweenTags = Pattern.compile(">\\s+<");
    private static final Pattern s_attributeWithSingleQuote = Pattern.compile("<[^>]*'[^>]*>");

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final DataController _controller;
    private final Validator _validator;

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
        _validator = XmlHelper.buildValidator(homepagePath.resolve("css").resolve("schema.xsd"));
    }

    @Override
    public void handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;

        FileHelper.createParentDirectory(getOutputFile(file));

        try (final FileReader fr = new FileReader(file.toFile());
             final BufferedReader br = new BufferedReader(fr);
             final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            final byte[] encoded = Files.readAllBytes(file);
            final String content = new String(encoded, StandardCharsets.UTF_8);
            final List<Error> errors = check(file, content);
            if (!errors.isEmpty()) {
                status = Status.HANDLED_WITH_ERROR;
            } else {
                // must write something in the file otherwise its last modification datetime will be incorrect
                pw.println("OK");
            }
            for (final Error error: errors) {
                final String message = "line " + error.getLineNumber() + ": " + error.getErrorMessage();
                pw.println(message);
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
            status = Status.FAILED_TO_HANDLED;
        }

        _controller.handleCreation(file, status, getOutputFile(file), getReportFile(file));
    }

    public List<Error> check(final Path file,
                             final String content) {  //TODO see how to test this method while keeping it private
        final List<Error> errors =  new ArrayList<>();
        errors.addAll(checkFileBom(content));
        errors.addAll(checkCharacters(content));
        errors.addAll(checkPath(file, content));
        errors.addAll(checkSchema(content));
        errors.addAll(checkLines(content));
        return errors;
    }

    private static List<Error> checkFileBom(final String content) {

        final List<Error> errors = new ArrayList<>();
        if (content.startsWith(UTF8_BOM)) {
            errors.add(new Error(1, "file should not have a UTF BOM"));
        }
        return errors;
    }

    private static List<Error> checkCharacters(final String content) {

        final List<Error> errors = new ArrayList<>();

        boolean isPreviousCharacterCarriageReturn = false;
        boolean isPreviousCharacterWhiteSpace = false;
        boolean isLineEmpty = true;
        int lineNumber = 1;
        int columnNumber = 1;

        for (int i = 0; i < content.length(); i++) {
            final int ch = content.codePointAt(i);
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
                columnNumber = 1;
                isLineEmpty = true;
                isPreviousCharacterCarriageReturn = false;
                isPreviousCharacterWhiteSpace = false;
            } else if (Character.isISOControl(ch)) {
                isPreviousCharacterCarriageReturn = false;
                isPreviousCharacterWhiteSpace = Character.isWhitespace(ch);
                errors.add(new Error(lineNumber, "line contains a control character (x" +
                                                 Integer.toHexString(ch) +
                                                 ") at column " +
                                                 columnNumber));
                columnNumber++;
            } else if (Character.isWhitespace(ch)) {
                isPreviousCharacterCarriageReturn = false;
                isPreviousCharacterWhiteSpace = true;
                columnNumber++;
            } else {
                isPreviousCharacterCarriageReturn = false;
                isPreviousCharacterWhiteSpace = false;
                isLineEmpty = false;
                columnNumber++;
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

    private static List<Error> checkPath(final Path file,
                                         final String content) { // TODO really check that the 5th line is correct

        final List<Error> errors = new ArrayList<>();

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
            ExitHelper.exit(e);
        }

        return errors;
    }

    private static List<Error> checkLines(final String content) {

        final List<Error> errors = new ArrayList<>();

        int n = 0;
        for (final String line : content.lines().toArray(String[]::new)) {
            n++;
            if (numberOfWhiteCharactersAtBeginning(line) % 2 == 1) {
                errors.add(new Error(n, "odd number of spaces at the beginning of the line"));
            }
            final Matcher badGreaterThan = s_badGreaterThan.matcher(line);
            if (badGreaterThan.replaceAll("").indexOf('>') >= 0) {
                errors.add(new Error(n, "the line contains a \">\""));
            }
            final Matcher attributeWithSingleQuote = s_attributeWithSingleQuote.matcher(line);
            if (attributeWithSingleQuote.find()) {
                errors.add(new Error(n, "the line contains an XML attribute between single quotes \"" + attributeWithSingleQuote.group() + "\""));
            }
            final Matcher spaceInTags = s_spaceInTags.matcher(line);
            if (spaceInTags.find()) {
                errors.add(new Error(n, "the line contains space in an XML tag \"" + spaceInTags.group() + "\""));
            }
            if (n > 3) {
                final Matcher spaceBetweenTags = s_spaceBetweenTags.matcher(line);
                if (spaceBetweenTags.find()) {
                    errors.add(new Error(n, "the line contains the string \"" + spaceBetweenTags.group() + "\""));
                }
            }
        }

        return errors;
    }

    private List<Error> checkSchema(final String content) {

        final List<Error> errors = new ArrayList<>();

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
    public void handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));

        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));
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

        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }

    private static int numberOfWhiteCharactersAtBeginning(final String str) {

        int n = 0;
        while ((n < str.length()) && (str.charAt(n) == ' ')) n++;
        return n;
    }

    public static class Error {

        private final int _lineNumber;
        private final String _errorMessage;

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
