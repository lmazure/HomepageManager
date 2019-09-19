import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FileTracker {

    final Map<Path, TrackedFile> _files;
    
    public FileTracker() {
        
        _files= new HashMap<>();
    }
    
    public void addFile(final Path file) {
    
        if (_files.containsKey(file)) {
            ExitHelper.of().message("Duplicated path");
        }
        
        _files.put(file, new TrackedFile(file));
    }
    
    public void handleFileCreation(final Path file) {

        TrackedFile f = _files.get(file);
        if (f == null) {
            f= new TrackedFile(file);
            _files.put(file, f);
        }

        if (!f.isDeleted()) {
            ExitHelper.of().message("Creating a file that currently exists");
        }
        
        f.setCreated();
        
        createHtmlFile(file);
    }
    
    public void handleFileDeletion(final Path file) {

        final TrackedFile f = _files.get(file);
        if (f == null) {
            ExitHelper.of().message("Unknown file");
        }
        assert (f != null);

        if (f.isDeleted()) {
            ExitHelper.of().message("Deleting a file that currently does not exist");
        }
        
        f.setDeleted();
        
        deleteHtmlFile(file);
    }
    
    private void createHtmlFile(final Path file) {
        System.out.println("===> create " + getHtmlFilename(file));
        final File outputFile = getHtmlFilename(file).toFile();
        
        //factory.setNamespaceAware(true);
        //factory.setValidating(true);
        try {
            File stylesheet = new File("H:/xampp/htdocs/css/strict.xsl");
            File datafile = file.toFile();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(datafile);

            // Use a Transformer for output
            TransformerFactory tFactory = TransformerFactory.newInstance();
            StreamSource stylesource = new StreamSource(stylesheet);
            Transformer transformer = tFactory.newTransformer(stylesource);

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(outputFile);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException tce) {
            // Error generated by the parser
            System.out.println("\n** Transformer Factory error");
            System.out.println("   " + tce.getMessage());

            // Use the contained exception, if any
            Throwable x = tce;

            if (tce.getException() != null) {
                x = tce.getException();
            }

            x.printStackTrace();
        } catch (TransformerException te) {
            // Error generated by the parser
            System.out.println("\n** Transformation error");
            System.out.println("   " + te.getMessage());

            // Use the contained exception, if any
            Throwable x = te;

            if (te.getException() != null) {
                x = te.getException();
            }

            x.printStackTrace();
        } catch (SAXException sxe) {
            // Error generated by this application
            // (or a parser-initialization error)
            Exception x = sxe;

            if (sxe.getException() != null) {
                x = sxe.getException();
            }

            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            // Parser with specified options can't be built
            pce.printStackTrace();
        } catch (IOException ioe) {
            // I/O error
            ioe.printStackTrace();
        }
    }

    private void deleteHtmlFile(final Path file) {
        System.out.println("===> delete " + getHtmlFilename(file));
    }

    public void dump(final PrintStream stream) {
        for (Path p: _files.keySet()) {
            _files.get(p).dump(stream);
            stream.print("-------------------------------------");
        }
    }

    private Path getHtmlFilename(final Path file) {
        
        final String s = file.toString();
        return Paths.get(s.substring(0, s.length() - 4).concat(".htmlT"));
    }

}
