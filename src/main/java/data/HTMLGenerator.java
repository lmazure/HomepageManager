package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import utils.ExitHelper;
import utils.FileHelper;
import utils.Logger;

/**
 * Manage the creation of the HTML files
 *
 */
public class HTMLGenerator implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final DataController _controller;

    private final DocumentBuilder _builder;
    private final Transformer _transformer;

    private final static Lock _lock = new ReentrantLock();

    /**
     * This class generates the HTML files from the XML files.
     *
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     * @param controller
     */
    public HTMLGenerator(final Path homepagePath,
                         final Path tmpPath,
                         final DataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _lock.lock();
        try {
            _builder = newDocumentBuilder();
            _transformer = newTransformer();
        } finally {
            _lock.unlock();
        }
    }

    @Override
    public void handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;

        FileHelper.createParentDirectory(getOutputFile(file));

        final File outputFile = getOutputFile(file).toFile();

        try (final InputStream is = new FileInputStream(file.toFile())) {
            Document document;
            synchronized (_builder) {
                document = _builder.parse(is);
                final DOMSource source = new DOMSource(document);
                final StreamResult result = new StreamResult(outputFile);
                _transformer.transform(source, result);
                Logger.log(Logger.Level.INFO)
                      .append(outputFile)
                      .append(" is generated")
                      .submit();
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

        _controller.handleDeletion(file, status, getOutputFile(file), getReportFile(file));
    }

    @Override
    public void handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));

        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));
    }

    private static DocumentBuilder newDocumentBuilder() {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            return factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            ExitHelper.exit(e);
            return null;
        }
    }

    private Transformer newTransformer() {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final StreamSource stylesource = new StreamSource(getSylesheetFile().toFile());
        try {
            return transformerFactory.newTransformer(stylesource);
        } catch (final TransformerConfigurationException e) {
            ExitHelper.exit(e);
            return null;
        }
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileHelper.computeTargetFile(_homepagePath, _homepagePath, file, "", "html");
    }

    @Override
    public Path getReportFile(final Path file) {
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_html", "txt");
    }

    private Path getSylesheetFile() {
        return Paths.get(_homepagePath.toString(), "css", "strict.xsl");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {

        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified())
               || (getOutputFile(file).toFile().lastModified() <= getSylesheetFile().toFile().lastModified());
    }
}
