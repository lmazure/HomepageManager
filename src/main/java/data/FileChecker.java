package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import utils.ExitHelper;
import utils.FileHelper;
import utils.FileNameHelper;
import utils.Logger;
import utils.XmlHelper;

/**
 * This class checks the text appearing in XML files (buit without interpreting the XML, the XML content is verified by NodeValueChecker).
 */
public class FileChecker implements FileHandler { // TODO should be split several checkers (and these one put in their own namespace)

    private static final String s_utf8_bom = "\uFEFF";
    private static final Pattern s_badGreaterThan = Pattern.compile("<[^>]*>");
    private static final Pattern s_spaceInTags = Pattern.compile("(</? [^>]*/?>|</?[^>]* /?>)");
    private static final Pattern s_spaceInAttributes = Pattern.compile("(</?[^>]*( =|= )[^>]*/?>)");
    private static final Pattern s_doubleSpaceInAttributes = Pattern.compile("(</?[^>]*  [^>]*/?>)");
    private static final Pattern s_attributeWithSingleQuote = Pattern.compile("<[^>]*'[^>]*>");
    private static final Pattern s_localLinkPattern = Pattern.compile("<A>([^:]+?)</A>");
    private final static String s_checkType = "file";
    private final static Lock s_lock = new ReentrantLock();

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final DataController _controller;
    private final ViolationDataController _violationController;
    private final Validator _validator;

    /**
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     * @param controller controller to notify of additional / removed violations
     * @param violationController controller to notify of additional / removed violations
     */
    public FileChecker(final Path homepagePath,
                       final Path tmpPath,
                       final DataController controller,
                       final ViolationDataController violationController) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _violationController = violationController;
        s_lock.lock();
        try {
            _validator = XmlHelper.buildValidator(homepagePath.resolve("css").resolve("schema.xsd"));
        } finally {
            s_lock.unlock();
        }
    }

    @Override
    public void handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;

        FileHelper.createParentDirectory(getOutputFile(file));

        final List<Error> errors = check(file);
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

    /**
     * @param file name of the file to be checked
     * @return violations
     */
    public List<Error> check(final Path file) {  //TODO see how to test this method while keeping it private
        final String content = FileHelper.slurpFile(file.toFile());

        final List<Error> errors =  new ArrayList<>();
        errors.addAll(checkFileBom(content));
        errors.addAll(checkCharacters(content));
        errors.addAll(checkPath(file, content));
        errors.addAll(checkSchema(content));
        errors.addAll(checkLines(content));
        errors.addAll(checkLocalLinks(content, file));
        return errors;
    }

    private static List<Error> checkFileBom(final String content) {

        final List<Error> errors = new ArrayList<>();
        if (content.startsWith(s_utf8_bom)) {
            errors.add(new Error("MissingBom", 1, "file should not have a UTF BOM"));
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
                    errors.add(new Error("BadEndOfLine", lineNumber, "line should finish by \\r\\n instead of \\n"));
                }
                if (isPreviousCharacterWhiteSpace) {
                    errors.add(new Error("WhiteSpaceAtLineEnd", lineNumber, "line is finishing with a white space"));
                }
                if (isLineEmpty) {
                    errors.add(new Error("EmptyLine", lineNumber, "empty line"));
                }
                lineNumber++;
                columnNumber = 1;
                isLineEmpty = true;
                isPreviousCharacterCarriageReturn = false;
                isPreviousCharacterWhiteSpace = false;
            } else if (Character.isISOControl(ch)) {
                isPreviousCharacterCarriageReturn = false;
                isPreviousCharacterWhiteSpace = Character.isWhitespace(ch);
                errors.add(new Error("ControlCharacter",
                                     lineNumber,
                                     "line contains a control character (x" +
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
            errors.add(new Error("WhiteSpaceAtLineEnd", lineNumber, "line is finishing with a white space"));
        }
        if (isLineEmpty) {
            errors.add(new Error("EmptyLine", lineNumber, "empty line"));
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
                errors.add(new Error("WrongPath",
                                     5,
                                     "the name of the file does not appear in the <PATH> node (expected to see \"" + pathString + "\")"));
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
            if (numberOfSpacesAtBeginningOfLine(line) % 2 == 1) {
                errors.add(new Error("OddIndentation", n, "odd number of spaces at the beginning of the line"));
            }
            final Matcher badGreaterThan = s_badGreaterThan.matcher(line);
            if (badGreaterThan.replaceAll("").indexOf('>') >= 0) {
                errors.add(new Error("GreaterThanCharacter", n, "the line contains a \">\""));
            }
            final Matcher attributeWithSingleQuote = s_attributeWithSingleQuote.matcher(line);
            if (attributeWithSingleQuote.find()) {
                errors.add(new Error("AttributeBetweenSingleQuotes", n, "the line contains an XML attribute between single quotes \"" + attributeWithSingleQuote.group() + "\""));
            }
            final Matcher spaceInTags = s_spaceInTags.matcher(line);
            if (spaceInTags.find()) {
                errors.add(new Error("SpaceInXmlNode", n, "the line contains space in an XML tag \"" + spaceInTags.group() + "\""));
            }
            final Matcher spaceInAttributes = s_spaceInAttributes.matcher(line);
            if (spaceInAttributes.find()) {
                errors.add(new Error("SpaceInAttributeSetting", n, "the line contains space near \"=\" in an XML attribute \"" + spaceInAttributes.group() + "\""));
            }
            final Matcher doubleSpaceInAttributes = s_doubleSpaceInAttributes.matcher(line);
            if (doubleSpaceInAttributes.find()) {
                errors.add(new Error("DoubleSpaceInXmlNode", n, "the line contains double space in an XML attribute \"" + doubleSpaceInAttributes.group() + "\""));
            }
        }

        return errors;
    }

    private static List<Error> checkLocalLinks(final String content,
                                               final Path file) {

        final List<Error> errors = new ArrayList<>();

        final List<String> localLinks = extractLocalLinks(content);
        for (final String link: localLinks) {
            final Error error = checkLocalLink(link, file);
            if (error != null) {
                errors.add(error);
            }
        }
        return errors;
    }

    private List<Error> checkSchema(final String content) {

        final List<Error> errors = new ArrayList<>();

        final Source source = new StreamSource(new StringReader(content));

        try {
            synchronized (_validator) {
                _validator.validate(source);
            }
        } catch (final SAXException e) {
            errors.add(new Error("SchemaViolation", 0, "the file violates the schema (\"" + e.toString() + "\")"));
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        return errors;
    }

    private static List<String> extractLocalLinks(final String content) {
        final List<String> links = new ArrayList<>();
        final Matcher matcher = s_localLinkPattern.matcher(content);
        while (matcher.find()) {
            String table = matcher.group(1);
            links.add(table);
        }
        return links;
    }

    private static Error checkLocalLink(final String link,
                                        final Path file) {
        final Path directory = file.getParent();

        // check file presence
        Path targetFile;
        try {
            targetFile = directory.resolve(link.replaceFirst("#.*$", "")
                                               .replaceFirst("\\.html$", ".xml"));
            if (!Files.exists(targetFile)) {
                return new Error("IncorrectLocalLink", 0, "the file \"" + targetFile + "\" does not exist");
            }
        } catch (@SuppressWarnings("unused") final InvalidPathException e) {
            return new Error("IncorrectLocalLink", 0, "the local link \"" + link + "\" has an invalid value");
        }

        // check anchor presence
        if (link.indexOf('#') < 0 ) {
            return null;
        }
        final String anchor = link.replaceFirst(".*#", "");
        final String targetFileContent = FileHelper.slurpFile(targetFile.toFile());
        if (targetFileContent.indexOf("<ANCHOR>" + anchor + "</ANCHOR>") < 0) {
            return new Error("IncorrectLocalLink", 0, "the file \"" + file + "\" does not contain the anchor \"" + anchor + "\"");
        }

        return null;
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

    private static int numberOfSpacesAtBeginningOfLine(final String str) {

        int n = 0;
        while ((n < str.length()) && (str.charAt(n) == ' ')) {
            n++;
        }
        return n;
    }


    /**
     * @param checkName Name of the check
     * @param lineNumber Line number of the violation
     * @param errorMessage Message describing the violation
     */
    public static record Error(String checkName, int lineNumber, String errorMessage) {}
}
