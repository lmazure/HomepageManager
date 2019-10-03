import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.xml.sax.SAXParseException;

/**
 * Manage the creation of the HTML files
 *
 */
public class HTMLFileGenerator implements FileHandler {

    final private Path _homepagePath;
    final private Path _tmpPath;
    final private DocumentBuilder _builder;
    final private Transformer _transformer;
    
    public HTMLFileGenerator(final Path homepagePath, final Path tmpPath) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _builder = newDocumentBuilder();
        _transformer = newTransformer(_homepagePath);
    }
    
    @Override
    public Status handleCreation(final Path file) {

        final File outputFile = getOutputFile(file).toFile();

        //factory.setNamespaceAware(true);
        //factory.setValidating(true);
        try (final InputStream is = new FileInputStream(file.toFile())) {
            final Document document = _builder.parse(is);
            final DOMSource source = new DOMSource(document);
            final StreamResult result = new StreamResult(outputFile);
            _transformer.transform(source, result);
        } catch (final Exception e) {
            final Path reportFile = getReportFile(file);
            FileHelper.createParentDirectory(reportFile);
            try (final PrintStream reportWriter = new PrintStream(reportFile.toFile())) {
                e.printStackTrace(reportWriter);
            } catch (final IOException e2) {
                ExitHelper.exit(e2);
            }
            return (e instanceof SAXParseException) ? Status.HANDLED_WITH_ERROR : Status.FAILED_TO_HANDLED;                
        }
            
        return Status.HANDLED_WITH_SUCCESS;
    }

    @Override
    public Status handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));
        
        return Status.HANDLED_WITH_SUCCESS;
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
    
    private Transformer newTransformer(final Path homepagePath) {
        final TransformerFactory tFactory = TransformerFactory.newInstance();
        final StreamSource stylesource = new StreamSource(getSylesheetFile().toFile());
        try {
            return tFactory.newTransformer(stylesource);
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

    public Path getSylesheetFile() {
        return Paths.get(_homepagePath.toString(), "css", "strict.xsl");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified())
               || (getOutputFile(file).toFile().lastModified() <= getSylesheetFile().toFile().lastModified());
    }
}
