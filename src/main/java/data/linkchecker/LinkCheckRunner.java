package data.linkchecker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.BackgroundDataController;
import data.FileHandler.Status;
import data.internet.SiteData;
import data.internet.SiteDataRetriever;
import utils.ExitHelper;
import utils.FileHelper;
import utils.HttpHelper;
import utils.InvalidHttpCodeException;
import utils.Logger;
import utils.Logger.Level;
import utils.StringHelper;
import utils.XmlHelper;
import utils.XmlParsingException;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlParser;

public class LinkCheckRunner {

    private static final int MAX_CACHE_AGE = 30*24*60*60;
    private static final int REPORT_THROTTLING_PERIOD = 90;
    private final Path _file;
    private final Map<URL, SiteData> _effectiveData;
    private final Map<URL, LinkData> _expectedData;
    private final Map<URL, ArticleData> _articles;
    private final Map<URL, List<LinkContentCheck>> _checks;
    private int _nbSitesRemainingToBeChecked;
    private boolean _isCancelled;
    private final BackgroundDataController _controller;
    private final SiteDataRetriever _retriever;
    private final DocumentBuilder _builder;
    private final Path _outputFile;
    private final Path _reportFile;
    private Instant _lastFileWriteTimestamp;

    public LinkCheckRunner(final Path file,
                           final Path tmpPath,
                           final BackgroundDataController controller,
                           final Path ouputFile,
                           final Path reportFile) {
        _file = file;
        _effectiveData = new TreeMap<>(
                new Comparator<URL>() {
                    @Override
                    public int compare(final URL a, final URL b) {
                        return a.toString().compareTo(b.toString());
                    }});
        _expectedData = new HashMap<>();
        _articles = new HashMap<>();
        _checks = new HashMap<>();
        _isCancelled = false;
        _builder = XmlHelper.buildDocumentBuilder();
        _retriever = new SiteDataRetriever(tmpPath.resolve("internet_cache"));
        _controller = controller;
        _outputFile = ouputFile;
        _reportFile = reportFile;
        _lastFileWriteTimestamp = Instant.MIN;
    }

    public synchronized void launch() {

        createOutputfile();

        final List<LinkData> links;
        final List<ArticleData> articles;

        try {
            final Document document = _builder.parse(_file.toFile());
            links =  extractLinks(document.getDocumentElement());
            articles =  extractArticles(document.getDocumentElement());
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

        for (final ArticleData article : articles) {
            for (final LinkData link : article.getLinks()) {
                final URL url = StringHelper.convertStringToUrl(link.getUrl());
                if (url != null) {
                    _articles.put(url, article);
                }
            }
        }

        final List<URL> list = buildListOfLinksToBeChecked(links);
        _nbSitesRemainingToBeChecked = list.size();
        for (final URL url: list) {
            _retriever.retrieve(url, this::handleLinkData, MAX_CACHE_AGE);
        }
    }

    private List<LinkData> extractLinks(final Element e) {

        final List<LinkData> list = new ArrayList<>();
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                list.addAll(extractLinks((Element)children.item(j)));
            }
        }

        if (XmlHelper.isOfType(e, ElementType.X)) {
            try {
                list.add(XmlParser.parseXElement(e));
            } catch (final XmlParsingException ex) {
                Logger.log(Level.ERROR)
                      .append("Failed to parse Element ")
                      .append(ex)
                      .submit();
            }
        }

        return list;
    }

    private List<ArticleData> extractArticles(final Element e) {

        final List<ArticleData> list = new ArrayList<>();
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                list.addAll(extractArticles((Element)children.item(j)));
            }
        }

        if (XmlHelper.isOfType(e, ElementType.ARTICLE)) {
            try {
                list.add(XmlParser.parseArticleElement(e));
            } catch (final XmlParsingException ex) {
                Logger.log(Level.ERROR)
                      .append("Failed to parse Element ")
                      .append(ex)
                      .submit();
            }
        }

        return list;
    }

    private List<URL> buildListOfLinksToBeChecked(final List<LinkData> linkDatas) {

        final List<URL> list = new ArrayList<>();

        for (final LinkData linkData: linkDatas) {

            final String urlStr = linkData.getUrl();
            if (urlStr.startsWith("javascript:")) {
                continue;
            }
            if (urlStr.indexOf(":") < 0) {
                // TODO implement check of local links
                Logger.log(Logger.Level.INFO)
                      .append("TBD: local link ")
                      .append(urlStr)
                      .append(" is not checked")
                      .submit();
                continue;
            }
            if (urlStr.startsWith("ftp:")) {
                // TODO implement check of FTP links
                Logger.log(Logger.Level.INFO)
                      .append("TBD: FTP URL ")
                      .append(urlStr)
                      .append(" is not checked")
                      .submit();
                continue;
            }
            if (urlStr.startsWith("mailto:")) {
                // TODO implement check of mail links
                Logger.log(Logger.Level.INFO)
                      .append("TBD: mailto URL ")
                      .append(urlStr)
                      .append(" is not checked")
                      .submit();
                continue;
            }

            final URL url = StringHelper.convertStringToUrl(urlStr);
            if (url != null) {
                list.add(url);
                _expectedData.put(url, linkData);
            }
        }

        return list;
    }

    public synchronized void cancel() {
        _isCancelled = true;
        FileHelper.deleteFile(_outputFile);
        FileHelper.deleteFile(_reportFile);
        _controller.handleDeletion(_file, Status.HANDLED_WITH_SUCCESS, _outputFile, _reportFile); // TODO the two last arguments cannot be correct
    }

    private void createOutputfile() {
        FileHelper.createParentDirectory(_outputFile);

        try (final FileOutputStream os = new FileOutputStream(_outputFile.toFile());
             final PrintWriter pw = new PrintWriter(os)) {
               pw.println("Analysis of links is started");
               Logger.log(Logger.Level.INFO)
                     .append(_outputFile)
                     .append(" starts to be generated")
                     .submit();
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
        if ((siteData.getStatus() == SiteData.Status.SUCCESS) &&
            _expectedData.get(siteData.getUrl()).getStatus().isEmpty()) {
            final LinkContentChecker contentChecker = LinkContentCheckerFactory.build(siteData.getUrl(),
                                                                                      _expectedData.get(siteData.getUrl()),
                                                                                      Optional.ofNullable(_articles.get(siteData.getUrl())),
                                                                                      siteData.getDataFile().get());
            try {
                _checks.put(siteData.getUrl(), contentChecker.check());
            } catch (final ContentParserException e) {
                FileHelper.createParentDirectory(_reportFile);
                try (final PrintStream reportWriter = new PrintStream(_reportFile.toFile())) {
                    e.printStackTrace(reportWriter);
                } catch (final IOException e2) {
                    ExitHelper.exit(e2);
                }
                _controller.handleUpdate(_file, Status.FAILED_TO_HANDLED, _outputFile, _reportFile);
                return;
            }
        }

        if (isDataFresh.booleanValue()) {
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

        Logger.log(Logger.Level.INFO)
              .append("URL ")
              .append(siteData.getUrl())
              .append(" ")
              .append(_nbSitesRemainingToBeChecked)
              .append(" status=")
              .append(status.toString())
              .append(" updateIsPublished=")
              .append(shouldUpdateBePublished)
              .submit();
    }

    private void writeOutputFile() throws FileNotFoundException, IOException {

        final StringBuilder ok = new StringBuilder();
        final StringBuilder ko = new StringBuilder();
        final StringBuilder checks = new StringBuilder();

        int numberOfBrokenLinks = 0;
        int numberOfBadLinkData = 0;

        for (final URL url : _effectiveData.keySet()) {
            final LinkData expectedData = _expectedData.get(url);
            final SiteData effectiveData = _effectiveData.get(url);
            final StringBuilder builder = isOneDataExpected(expectedData, effectiveData) ? ok : ko;
            if (!isOneDataExpected(expectedData, effectiveData)) {
                numberOfBrokenLinks++;
            }
            appendLivenessCheckResult(url, expectedData, effectiveData, builder);
            if (_checks.containsKey(url) && !_checks.get(url).isEmpty()) {
                checks.append('\n');
                checks.append(url);
                checks.append('\n');
                for (final LinkContentCheck c: _checks.get(url)) {
                    checks.append(c.getDescription());
                    checks.append('\n');
                    numberOfBadLinkData++;
                }
            }
        }

        try (final FileOutputStream os = new FileOutputStream(_outputFile.toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println("number of broken links = " + numberOfBrokenLinks);
            pw.println("number of bad link data = " + numberOfBadLinkData);
            pw.println();
            pw.println(ko.toString());
            pw.println("=".repeat(80));
            pw.println(checks.toString());
            pw.println("=".repeat(80));
            pw.println(ok.toString());
        }
    }

    /**
     * append (at the end of builder) the result of the liveness check of url
     *
     * @param url
     * @param expectedData
     * @param effectiveData
     * @param builder
     */
    private static void appendLivenessCheckResult(final URL url,
                                                  final LinkData expectedData,
                                                  final SiteData effectiveData,
                                                  final StringBuilder builder) {

        builder.append("Title = \"" + expectedData.getTitle() + "\"\n");
        if (expectedData.getSubtitles().length > 0) {
            builder.append("Subtitle = \"" + String.join("\" \"",  expectedData.getSubtitles()) + "\"\n");
        }
        builder.append("URL = " + url + "\n");
        builder.append("Expected status = " + expectedData.getStatus().map(utils.xmlparsing.LinkStatus::toString).orElse("") + "\n");
        builder.append("Effective status = " + effectiveData.getStatus() + "\n");
        final String httpCode = effectiveData.getHttpCode().map(i -> {
            try {
                return i.toString() + " " + HttpHelper.getStringOfCode(i.intValue());
            } catch (@SuppressWarnings("unused") final InvalidHttpCodeException e) {
                return " invalid code! (" + i.toString() + ")";
            }
        }).orElse("---");
        builder.append("Effective HTTP code = " + httpCode + "\n");
        if (effectiveData.getHeaders().isPresent() && effectiveData.getHeaders().get().containsKey("Location")) {
            final String redirection = effectiveData.getHeaders().get().get("Location").get(0);
            builder.append("Redirection = " + redirection + "\n");
        }
        /*
        final Optional<List<String>> redirection = getRedirection(effectiveData);
        if (redirection.isPresent()) {
            builder.append("Redirection = " + redirection.get().get(0) + "\n");
        }
         */
        if (effectiveData.getError().isPresent()) {
            builder.append("Effective error = \"" + effectiveData.getError().get() + "\"\n");
        }
        final StringBuilder googleUrl = new StringBuilder("https://www.google.com/search?q=%22" +
                                                          URLEncoder.encode(expectedData.getTitle(), StandardCharsets.UTF_8) +
                                                         "%22");
        for (final String st: expectedData.getSubtitles()) {
            googleUrl.append("+%22" + URLEncoder.encode(st, StandardCharsets.UTF_8) + "%22");
        }
        builder.append("Look for article = " + googleUrl.toString() + "\n");
        builder.append("\n");
    }

    private boolean isDataExpected() { //TBD this method is very stupid, we should used a flag instead of computing the status every time

        for (final URL url : _effectiveData.keySet()) {
            if (!isOneDataExpected(_expectedData.get(url), _effectiveData.get(url))) {
                return false;
            }
            if (_checks.containsKey(url) && !_checks.get(url).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private static boolean isOneDataExpected(final LinkData expectedData,
                                             final SiteData effectiveData) {

        if (expectedData.getStatus().isPresent() && expectedData.getStatus().get().equals(utils.xmlparsing.LinkStatus.DEAD)) {
            if (effectiveData.getStatus() == SiteData.Status.FAILURE) {
                return true;
            }
            if (effectiveData.getHttpCode().isEmpty()) {
                return true;
            }
            if (effectiveData.getHttpCode().isPresent() && effectiveData.getHttpCode().get().intValue() != 200) {
                return true;
            }
            return false;
        }

        return (effectiveData.getHttpCode().isPresent() && effectiveData.getHttpCode().get().intValue() == 200);
    }

    /*
        private Optional<List<String>> getRedirection(final SiteData effectiveData) {

        if (effectiveData.getHeaders().isEmpty()) {
            return Optional.empty();
        }

        final Map<String, List<String>> headers = effectiveData.getHeaders().get();
        for (final String key: headers.keySet()) {
            if ((key != null) && key.equalsIgnoreCase("location")) {
                return Optional.of(headers.get(key));
            }
        }

        return Optional.empty();
    }
    */
}