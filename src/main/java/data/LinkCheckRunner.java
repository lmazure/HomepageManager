package data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
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

import data.FileHandler.Status;
import data.internet.SiteData;
import data.internet.SiteDataRetriever;
import data.nodechecker.checker.nodeChecker.NodeChecker;
import utils.ExitHelper;
import utils.FileHelper;
import utils.HttpHelper;
import utils.InvalidHttpCodeException;
import utils.XMLHelper;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.XmlParser;

class LinkCheckRunner {
    
    private static final int MAX_CACHE_AGE = 30*24*60*60;
    private static final int REPORT_THROTTLING_PERIOD = 90;
    private final Path _file;
    private final Map<URL, SiteData> _effectiveData;
    private final Map<URL, LinkData> _expectedData;
    private int _nbSitesRemainingToBeChecked;
    private boolean _isCancelled;
    private final BackgroundDataController _controller;
    private final SiteDataRetriever _retriever;
    private final DocumentBuilder _builder;
    private final Path _outputFile;
    private final Path _reportFile;
    private Instant _lastFileWriteTimestamp; 
    
    LinkCheckRunner(final Path file,
                    final Path tmpPath,
                    final BackgroundDataController controller,
                    final Path ouputFile,
                    final Path reportFile) {
        _file = file;
        _effectiveData = new TreeMap<URL, SiteData>(
                new Comparator<URL>() {
                    @Override
                    public int compare(final URL a, final URL b) {
                        return a.toString().compareTo(b.toString());
                    }});
        _expectedData = new HashMap<URL, LinkData>();
        _isCancelled = false;
        _builder = XMLHelper.buildDocumentBuilder();
        _retriever = new SiteDataRetriever(tmpPath.resolve("internet_cache"));
        _controller = controller;
        _outputFile = ouputFile;
        _reportFile = reportFile;
        _lastFileWriteTimestamp = Instant.MIN;
    }
    
    synchronized void launch() {

        FileHelper.createParentDirectory(_outputFile);

        final List<LinkData> links;
        
        try (final FileReader fr = new FileReader(_file.toFile());
             final BufferedReader br = new BufferedReader(fr)) {
            final byte[] encoded = Files.readAllBytes(_file);
            final String content = new String(encoded, StandardCharsets.UTF_8);
            links = extractLinks(_file, content);
        } catch (final Exception e) {
            FileHelper.createParentDirectory(_reportFile);
            try (final PrintStream reportWriter = new PrintStream(_reportFile.toFile())) {
                e.printStackTrace(reportWriter);
            } catch (final IOException e2) {
                ExitHelper.exit(e2);
            }
            return;
        }           

        createOutputfile();
        
        final List<URL> list = buildListOfLinksToBeChecked(links);
        _nbSitesRemainingToBeChecked = list.size();
        for (final URL url: list) {
            _retriever.retrieve(url, this::handleLinkData, MAX_CACHE_AGE);
        }
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

    private List<LinkData> extractLinks(final File file,
                                        final Element e) {

        final List<LinkData> list = new ArrayList<LinkData>();
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                list.addAll(extractLinks(file, (Element) children.item(j)));
            }
        }

        if (e.getTagName().equals(NodeChecker.X)) {
            list.add(XmlParser.parseXNode(e));
        }

        return list;
    }
    
    private List<URL> buildListOfLinksToBeChecked(final List<LinkData> linkDatas) {
    
        final List<URL> list = new ArrayList<URL>();

        for (final LinkData linkData: linkDatas) {
            
            final String urlStr = linkData.getUrl(); 
            if (urlStr.startsWith("javascript:")) {
                continue;
            }
            if (urlStr.indexOf(":") < 0) {
                // TODO implement check of local links
                System.out.println("TBD: local link " + urlStr + " is not checked");
                continue;
            }
            if (urlStr.startsWith("ftp:")) {
                // TODO implement check of FTP links
                System.out.println("TBD: FTP URL " + urlStr + " is not checked");
                continue;
            }
            if (urlStr.startsWith("mailto:")) {
                // TODO implement check of mail links
                System.out.println("TBD: mailto URL " + urlStr + " is not checked");
                continue;
            }

            URL url = null;
            try {
                url = new URL(urlStr);
            } catch (@SuppressWarnings("unused") final MalformedURLException e) {
                System.out.println("URL " + urlStr + " is not checked because the URL is malformed");
                continue;
            }
            list.add(url);
            _expectedData.put(url, linkData);
        }
        
        return list;
    }
    
    synchronized void cancel() {
        _isCancelled = true;
        FileHelper.deleteFile(_outputFile);
        FileHelper.deleteFile(_reportFile);
        _controller.handleDeletion(_file, Status.HANDLED_WITH_SUCCESS, _outputFile, _reportFile); // TODO the two last arguments cannot be correct
    }
    
    private void createOutputfile() {
        try (final FileOutputStream os = new FileOutputStream(_outputFile.toFile());
             final PrintWriter pw = new PrintWriter(os)) {
               pw.println("Analysis of links is started");
               System.out.println(_outputFile.toFile() + " starts to be generated");
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

        final Status status = isDataExpected() ? ((_nbSitesRemainingToBeChecked == 0) ? Status.HANDLED_WITH_SUCCESS : Status.HANDLING_NO_ERROR)
                                               : ((_nbSitesRemainingToBeChecked == 0) ? Status.HANDLED_WITH_ERROR : Status.HANDLING_WITH_ERROR);

        final Instant now = Instant.now();
        final boolean shouldUpdateBePublished = (_nbSitesRemainingToBeChecked == 0) ||
                                                (Duration.between(_lastFileWriteTimestamp, now).getSeconds() > REPORT_THROTTLING_PERIOD);
        if (shouldUpdateBePublished) {
           try {
                writeOutputFile();
           } catch (final Exception e) {
              FileHelper.createParentDirectory(_reportFile);
              try (final PrintStream reportWriter = new PrintStream(_reportFile.toFile())) {
                  e.printStackTrace(reportWriter);
              } catch (final IOException e2) {
                  ExitHelper.exit(e2);
              }
              _controller.handleUpdate(_file, Status.FAILED_TO_HANDLED, _outputFile, _reportFile);
              return;
           }            
           _controller.handleUpdate(_file, status, _outputFile, _reportFile);
           _lastFileWriteTimestamp = now;
        }

        System.out.println("URL " + siteData.getUrl() + " " +
                           _nbSitesRemainingToBeChecked +
                           " status=" + status +
                           " updateIsPublished=" + shouldUpdateBePublished);
    }
    
    private void writeOutputFile() throws FileNotFoundException, IOException {

        final StringBuilder ok = new StringBuilder();
        final StringBuilder ko = new StringBuilder();
        int nbOk = 0;
        int nbKo = 0;
        for (final URL url : _effectiveData.keySet()) {
            final LinkData expectedData = _expectedData.get(url);
            final SiteData effectiveData = _effectiveData.get(url);
            final StringBuilder builder = isOneDataExpected(expectedData, effectiveData) ? ok : ko;
            if (isOneDataExpected(expectedData, effectiveData)) {
                nbOk++;
            } else {
                nbKo++;
            }
            builder.append("Title = \"" + expectedData.getTitle() + "\"\n");
            if (expectedData.getSubtitles().length > 0) {
                builder.append("Subtitle = \"" + String.join("\" \"",  expectedData.getSubtitles()) + "\"\n");
            }
            builder.append("URL = " + url + "\n");
            builder.append("Expected status = " + expectedData.getStatus().orElse("") + "\n");
            builder.append("Effective status = " + effectiveData.getStatus() + "\n");
            final String httpCode = effectiveData.getHttpCode().map(i -> {
                try {
                    return i.toString() + " " + HttpHelper.getStringOfCode(i);
                } catch (@SuppressWarnings("unused") final InvalidHttpCodeException e) {
                    return " invalid code! (" + i.toString() + ")";
                }
            }).orElse("---");
            builder.append("Effective HTTP code = " + httpCode + "\n");
            if (effectiveData.getHeaders().isPresent() && effectiveData.getHeaders().get().containsKey("Location")) {
                final String redirection = effectiveData.getHeaders().get().get("Location").get(0);
                builder.append("Redirection = " + redirection + "\n");
            }
            if (effectiveData.getError().isPresent()) {
                builder.append("Effective error = \"" + effectiveData.getError().get() + "\"\n");
            }
            builder.append("Look for article = https://www.google.com/search?q=%22" + URLEncoder.encode(expectedData.getTitle(), StandardCharsets.UTF_8) + "%22\n");
            builder.append("\n");
        }

        try (final FileOutputStream os = new FileOutputStream(_outputFile.toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("good = " + nbOk + " / bad = " + nbKo);
            pw.println();
            pw.println(ko.toString());
            pw.println("=".repeat(80));
            pw.println(ok.toString());
        }
    }
    
    private boolean isDataExpected() {

        for (final URL url : _effectiveData.keySet()) {
            if (!isOneDataExpected(_expectedData.get(url), _effectiveData.get(url))) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isOneDataExpected(final LinkData expectedData,
                                      final SiteData effectiveData) {
        
        if (expectedData.getStatus().isPresent() && expectedData.getStatus().get().equals("dead")) {
            if (effectiveData.getStatus() == SiteData.Status.FAILURE) return true;
            if (effectiveData.getHttpCode().isEmpty()) return true;
            if (effectiveData.getHttpCode().isPresent() && effectiveData.getHttpCode().get() != 200) return true;
            return false;
        }
        
        return (effectiveData.getHttpCode().isPresent() && effectiveData.getHttpCode().get() == 200);
    }
}