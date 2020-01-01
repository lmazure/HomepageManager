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
import java.util.List;
import java.util.Map;
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
import utils.xmlparsing.LinkData;
import utils.xmlparsing.XmlParser;

public class LinkChecker implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final BackgroundDataController _controller;
    private final DocumentBuilder _builder;
    private final SiteDataRetriever _retriever;
    private final Map<Path, LinkDataHandler> _handlers;
    
    /**
     * This class checks the appearing in XML files.
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
            final List<LinkData> links = extractLinks(file, content);
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
    
    private List<LinkData> extractLinks(final Path file,
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

    private List<LinkData> extractLinks(final File file,
                                        final Element e) {

        final List<LinkData> list = new ArrayList<LinkData>();
        
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if ( children.item(j).getNodeType() == Node.ELEMENT_NODE ) {
                list.addAll(extractLinks(file, (Element)children.item(j)));
            }
        }

        if (e.getTagName().equals(NodeChecker.X)) {
            list.add(XmlParser.parseXNode(e));
        }
        
        return list;
    }
    
    private void launchCheck(final Path file,
                             final List<LinkData> links) {
        
        final LinkDataHandler handler = new LinkDataHandler(file);
        _handlers.put(file, handler);
        handler.launch(links);
    }

    // ------------------------------------------------------------------------------------------------------------------

    class LinkDataHandler {
        
        private static final int MAX_CACHE_AGE = 30*24*60*60;
        private final Path _file;
        private final Map<URL, SiteData> _effectiveData;
        private final Map<URL, LinkData> _expectedData;
        private int _nbSitesRemainingToBeChecked;
        private boolean _isCancelled;
        
        private LinkDataHandler(final Path file) {
            _file = file;
            _effectiveData = new TreeMap<URL, SiteData>(
                    new Comparator<URL>() {
                        @Override
                        public int compare(final URL a, final URL b) {
                            return a.toString().compareTo(b.toString());
                        }});
            _expectedData = new HashMap<URL, LinkData>();
            _isCancelled = false;
        }
        
        private void launch(final List<LinkData> linkDatas) {
            createOutputfile();
            final List<URL> list = build(linkDatas);
            _nbSitesRemainingToBeChecked = list.size();
            for (final URL url: list) {
                _retriever.retrieve(url, this::handleLinkData, MAX_CACHE_AGE);
            }
        }
        
        private List<URL> build(final List<LinkData> linkDatas) {
        
            final List<URL> list = new ArrayList<URL>();

            for (final LinkData linkData: linkDatas) {
                
                if (linkData.getUrl().indexOf(":") < 0) {
                    // TODO implement check of local links
                    System.out.println("TBD: local link " + linkData.getUrl() + " is not checked");
                    continue;
                }

                if (linkData.getUrl().startsWith("javascript:")) {
                    continue;
                }

                if (linkData.getUrl().startsWith("ftp:")) {
                    // TODO implement check of FTP links
                    System.out.println("TBD: FTP link " + linkData.getUrl() + " is not checked");
                    continue;
                }

                URL url = null;
                try {
                    url = new URL(linkData.getUrl());
                } catch (final MalformedURLException e) {
                    ExitHelper.exit(e);
                }
                
                list.add(url);
                _expectedData.put(url, linkData);
            }
            
            return list;
        }
        
        private void cancel() {
            _isCancelled = true;
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
        
        /**
         * Callback when a the data of a link has been retrieved
         * @param isDataFresh
         * @param siteData
         */
        private synchronized void handleLinkData(final Boolean isDataFresh,
                                                 final SiteData siteData) {
        
            if (_isCancelled) {
                return;
            }
            
            _effectiveData.put(siteData.getUrl(), siteData);
            
            if (isDataFresh) {
                _nbSitesRemainingToBeChecked--;
            }
            
            Status status = Status.HANDLED_WITH_SUCCESS;
            try (final FileOutputStream os = new FileOutputStream(getOutputFile(_file).toFile());
                 final PrintWriter pw = new PrintWriter(os)) {
                for (final URL url : _effectiveData.keySet()) {
                    final LinkData expectedData = _expectedData.get(url);
                    final SiteData effectiveData = _effectiveData.get(url);
                    if (expectedData.getStatus().isPresent() ||
                        effectiveData.getHttpCode().isEmpty() ||
                        effectiveData.getHttpCode().get() != 200) {
                        pw.println("Title = " + expectedData.getTitle());
                        if (expectedData.getSubtitles().length > 0) {
                            pw.println("Subtitle = \"" + String.join("\" \"",  expectedData.getSubtitles()) + "\"");
                        }
                        pw.println("URL = " + url);
                        pw.println("Expected status = " + expectedData.getStatus().orElse(""));
                        pw.println("Effective status = " + effectiveData.getStatus());
                        pw.println("Effective HTTP code = " + effectiveData.getHttpCode().map(i -> i.toString()).orElse("---"));
                        pw.println();
                    }
                }
                status = (_nbSitesRemainingToBeChecked == 0) ? Status.HANDLED_WITH_SUCCESS : Status.HANDLING_NO_ERROR;
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

            System.out.println("URL " + siteData.getUrl() + " " + _nbSitesRemainingToBeChecked);

            _controller.handleUpdate(_file, status, getOutputFile(_file), getReportFile(_file));
        }
    }
}
