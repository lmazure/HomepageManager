package data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.internet.SiteData;
import data.internet.SiteDataRetriever;
import data.nodechecker.checker.nodeChecker.NodeChecker;
import utils.ExitHelper;
import utils.FileHelper;
import utils.XMLHelper;

public class LinkChecker implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final BackgroundDataController _controller;
    private final DocumentBuilder _builder;
    private final SiteDataRetriever _retriever;
    private final Map<Path, LinkDataHandler> _handlers;
    
    /**
     * This class checks the characters of the XML files.
     * 
     * @param homepagePath
     * @param tmpPath
     */
    public LinkChecker(final Path homepagePath,
                       final Path tmpPath,
                       final BackgroundDataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _builder = XMLHelper.buildDocumentBuilder();
        _retriever = new SiteDataRetriever(tmpPath.resolve("internet_cache"));
        _handlers = new HashMap<Path, LinkDataHandler>();
    }
    
    @Override
    public void handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;
        
        FileHelper.createParentDirectory(getOutputFile(file));

        try (final FileReader fr = new FileReader(file.toFile());
             final BufferedReader br = new BufferedReader(fr)) {
            final byte[] encoded = Files.readAllBytes(file);
            final String content = new String(encoded, StandardCharsets.UTF_8);
            final List<String> links = extractLinks(file, content);
            launchCheck(file, links);
            status = Status.HANDLING_NO_ERROR;
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
    
    private List<String> extractLinks(final Path file,
                                      final String content) throws SAXException {
        
        try {
            final Document document = _builder.parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            return extractLinks(file.toFile(), document.getDocumentElement());
        } catch (final IOException e) {
            ExitHelper.exit(e);
            // NOT REACHED
            return null;
        }
    }
    
    @Override
    public void handleDeletion(final Path file) {
        
        final LinkDataHandler handler = _handlers.get(file);
        if (handler != null) {
            handler.cancel();
            _handlers.remove(file);
        }

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));
        
        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));
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

    private List<String> extractLinks(final File file,
                                      final Element e) {

        final List<String> list = new ArrayList<String>();
        
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if ( children.item(j).getNodeType() == Node.ELEMENT_NODE ) {
                list.addAll(extractLinks(file, (Element)children.item(j)));
            }
        }

        if (e.getTagName().equals(NodeChecker.X)) {
            final NodeList linkNodes = e.getElementsByTagName("A");
            list.add(linkNodes.item(0).getTextContent());
        }
        
        return list;
    }
    
    private void launchCheck(final Path file,
                             final List<String> links) {
        
        final LinkDataHandler handler = new LinkDataHandler(file);
        _handlers.put(file, handler);
        handler.launch(links);
    }

    // ------------------------------------------------------------------------------------------------------------------

    class LinkDataHandler {
        
        private final Path _file;
        private final Map<URL, SiteData> _siteData;
        private final Set<URL> _siteRemainingToBeChecked;
        private boolean _isCancelled;
        
        private LinkDataHandler(final Path file) {
            _file = file;
            _siteData = new TreeMap<URL, SiteData>(
                    new Comparator<URL>() {
                        @Override
                        public int compare(final URL a, final URL b) {
                            return a.toString().compareTo(b.toString());
                        }});
            _siteRemainingToBeChecked = new HashSet<>();
            _isCancelled = false;
        }
        
        private void cancel() {
            _isCancelled = true;
        }
        
        private void launch(final List<String> links) {
            createOutputfile();
            for (final String link: links) {
                launch(link);
            }
        }
        
        private void createOutputfile() {
            try (final FileOutputStream os = new FileOutputStream(getOutputFile(_file).toFile());
                 final PrintWriter pw = new PrintWriter(os)) {
                   pw.println("Analysis of links is started");
                   System.out.println(getOutputFile(_file).toFile() + " starts to be generated");
            } catch (final Exception e) {
                ExitHelper.exit(e);
            }
        }
        
        private void launch(final String link) {
            
            if (link.indexOf(":") < 0) {
                // TODO implement check of local links
                System.out.println("TBD: local link " + link + " is not checked");
                return;
            }

            if (link.startsWith("javascript:")) return;

            if (link.startsWith("ftp:")) {
                // TODO implement check of FTP links
                System.out.println("TBD: FTP link " + link + " is not checked");
                return;
            }

            URL url = null;
            try {
                url = new URL(link);
            } catch (final MalformedURLException e) {
                ExitHelper.exit(e);
            }
            _siteRemainingToBeChecked.add(url);
            _retriever.retrieve(url, this::handleLinkData, 30*24*60*60);
        }
        
        private synchronized void handleLinkData(final Boolean isDataFresh,
                                                 final SiteData siteData) {
        
            if (_isCancelled) {
                return;
            }
            
            _siteData.put(siteData.getUrl(), siteData);
            
            if (isDataFresh) {
                _siteRemainingToBeChecked.remove(siteData.getUrl());
            }
            
            Status status = Status.HANDLED_WITH_SUCCESS;
            try (final FileOutputStream os = new FileOutputStream(getOutputFile(_file).toFile());
                 final PrintWriter pw = new PrintWriter(os)) {
                for (final URL url : _siteData.keySet()) {
                    pw.println("URL = " + url);
                    pw.println("Status = " + _siteData.get(url).getStatus());
                    pw.println("xHTTP code = " + _siteData.get(url).getHttpCode().map(i -> i.toString()).orElse("---"));
                    pw.println();
                }
                status = _siteRemainingToBeChecked.isEmpty() ? Status.HANDLED_WITH_SUCCESS : Status.HANDLING_NO_ERROR;
            } catch (final Exception e) {
               final Path reportFile = getReportFile(_file);
               FileHelper.createParentDirectory(reportFile);
               try (final PrintStream reportWriter = new PrintStream(reportFile.toFile())) {
                   e.printStackTrace(reportWriter);
               } catch (final IOException e2) {
                   ExitHelper.exit(e2);
               }
               status = Status.FAILED_TO_HANDLED;                
            }

            System.out.println("URL " + siteData.getUrl() + " " + _siteRemainingToBeChecked.isEmpty());

            _controller.handleUpdate(_file, status, getOutputFile(_file), getReportFile(_file));
        }
    }
}
