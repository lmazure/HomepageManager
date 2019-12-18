package data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.nodechecker.checker.nodeChecker.NodeChecker;
import utils.ExitHelper;
import utils.FileHelper;
import utils.XMLHelper;

public class LinkChecker implements FileHandler {

    final private Path _homepagePath;
    final private Path _tmpPath;
    final private DataController _controller;
    PrintWriter _pw;
    private final DocumentBuilder _builder;
    
    /**
     * This class checks the characters of the XML files.
     * 
     * @param homepagePath
     * @param tmpPath
     */
    public LinkChecker(final Path homepagePath,
                       final Path tmpPath,
                       final DataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _builder = XMLHelper.buildDocumentBuilder();
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
            _pw = pw; // TODO fix this crap
            check(file, content);
            status = Status.HANDLED_WITH_ERROR;
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
    
    public void check(final Path file,
                      final String content) throws SAXException {
        
        try {
            final Document document = _builder.parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            checkNode(file.toFile(), document.getDocumentElement());
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }
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
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_linkcheck", "txt");
    }
    
    @Override
    public Path getReportFile(final Path file) {
         return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_linkcheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }

    private void checkNode(final File file,
                           final Element e) {

        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if ( children.item(j).getNodeType() == Node.ELEMENT_NODE ) {
                checkNode(file, (Element)children.item(j));
            }
        }

            if (e.getTagName().equals(NodeChecker.X)) {
                System.out.println(e.getTextContent());
            }
    }
}
