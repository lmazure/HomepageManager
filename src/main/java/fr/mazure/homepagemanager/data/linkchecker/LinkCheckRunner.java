package fr.mazure.homepagemanager.data.linkchecker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.data.BackgroundDataController;
import fr.mazure.homepagemanager.data.FileHandler.Status;
import fr.mazure.homepagemanager.data.Violation;
import fr.mazure.homepagemanager.data.ViolationDataController;
import fr.mazure.homepagemanager.data.ViolationLocationUnknown;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.WellKnownRedirections;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.WellKnownRedirections.Match;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkUrlCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.Logger.Level;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.internet.InvalidHttpCodeException;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.FeedData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParser;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 * Execute the checks on all links of an XML file
 */
public class LinkCheckRunner {

    private static final int s_max_cache_age = 30*24*60*60;
    private final Path _file;
    private final Map<String, FullFetchedLinkData> _effectiveData;
    private final Map<String, LinkData> _expectedLinkData;
    private final Map<String, FeedData> _expectedFeedData;
    private final Map<String, ArticleData> _articles;
    private final Map<String, List<LinkContentCheck>> _checks;
    private int _nbSitesRemainingToBeChecked;
    private boolean _isCancelled;
    private final BackgroundDataController _controller;
    private final ViolationDataController _violationController;
    private final String _checkType;
    private final SiteDataRetriever _retriever;
    private final DocumentBuilder _builder;
    private final Path _outputFile;
    private final Path _reportFile;
    private static final WellKnownRedirections _redirectionData = new WellKnownRedirections();

    /**
     * @param file XML file to be checked
     * @param cachePath directory where the persistence files are written
     * @param controller controller
     * @param violationController violation controller
     * @param checkType Check type to use when recording the violations
     * @param ouputFile file into which the found violated checks are written
     * @param reportFile file into which technical error occurring during the check are written
     */
    public LinkCheckRunner(final Path file,
                           final Path cachePath,
                           final BackgroundDataController controller,
                           final ViolationDataController violationController,
                           final String checkType,
                           final Path ouputFile,
                           final Path reportFile) {
        _file = file;
        _effectiveData = new TreeMap<>();
        _expectedLinkData = new HashMap<>();
        _expectedFeedData = new HashMap<>();
        _articles = new HashMap<>();
        _checks = new HashMap<>();
        _isCancelled = false;
        _builder = XmlHelper.buildDocumentBuilder();
        _retriever = new SiteDataRetriever(cachePath);
        _controller = controller;
        _violationController = violationController;
        _checkType = checkType;
        _outputFile = ouputFile;
        _reportFile = reportFile;
    }

    /**
     * Launch the checks
     */
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
            _controller.handleUpdate(_file, Status.FAILED_TO_HANDLE, _outputFile, _reportFile);
            return;
        }

        for (final ArticleData article : articles) {
            for (final LinkData link : article.links()) {
                _articles.put(link.getUrl(), article);
            }
        }

        final List<String> linksToBeChecked = buildListOfLinksToBeChecked(links);
        _nbSitesRemainingToBeChecked = linksToBeChecked.size();
        if (_nbSitesRemainingToBeChecked == 0) {
            try {
                writeOutputFile();
            } catch (final Exception e) {
               FileHelper.createParentDirectory(_reportFile);
               try (final PrintStream reportWriter = new PrintStream(_reportFile.toFile())) {
                   e.printStackTrace(reportWriter);
               } catch (final IOException e2) {
                   ExitHelper.exit(e2);
               }
               _controller.handleUpdate(_file, Status.FAILED_TO_HANDLE, _outputFile, _reportFile);
               return;
            }
            _controller.handleUpdate(_file, Status.HANDLED_WITH_SUCCESS, _outputFile, _reportFile);
            return;
        }
        for (final String url: linksToBeChecked) {
            _retriever.retrieve(url, this::handleLinkData, s_max_cache_age, doNotUseCookies(url));
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
                      .append("Failed to parse Element")
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
                      .append("Failed to parse Element")
                      .append(ex)
                      .submit();
            }
        }

        return list;
    }

    private List<String> buildListOfLinksToBeChecked(final List<LinkData> linkDatas) {

        final List<String> list = new ArrayList<>();

        for (final LinkData linkData: linkDatas) {
            final String url = linkData.getUrl();
            if (isLinkCheckable(url)) {
                list.add(url);
                _expectedLinkData.put(url, linkData);
            }
            if (linkData.getFeed().isPresent()) {
                final String urlFeed = linkData.getFeed().get().getUrl();
                list.add(urlFeed);
                _expectedFeedData.put(urlFeed, linkData.getFeed().get());
            }
        }

        return list;
    }

    private static boolean isLinkCheckable(final String url) {
        if (url.indexOf(":") < 0) {
            // local links are checked in a file checker
            // TODO which one?
            return false;
        }
        if (url.startsWith("javascript:")) {
            return false;
        }
        if (url.startsWith("ftp:")) {
            // TODO implement check of FTP links
            Logger.log(Logger.Level.INFO)
                  .append("TBD: FTP URL ")
                  .append(url)
                  .append(" is not checked")
                  .submit();
            return false;
        }
        if (url.startsWith("mailto:")) {
            // TODO implement check of mail links
            Logger.log(Logger.Level.INFO)
                  .append("TBD: mailto URL ")
                  .append(url)
                  .append(" is not checked")
                  .submit();
            return false;
        }
        return true;
    }

    /**
     * Cancel the checks
     */
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
        _controller.handleUpdate(_file, Status.HANDLING_NO_ERROR, _outputFile, _reportFile);
    }

    /**
     * Callback when the data of a link has been retrieved
     *
     * @param isDataFresh
     * @param effectiveSiteData
     */
    private synchronized void handleLinkData(final Boolean isDataFresh,
                                             final FullFetchedLinkData effectiveSiteData) {

        if (_isCancelled) {
            return;
        }

        _effectiveData.put(effectiveSiteData.url().toString(), effectiveSiteData);
        if (!effectiveSiteData.error().isPresent()) {
            Checker checker = null;
            if (_expectedLinkData.containsKey(effectiveSiteData.url())) {
                // this is a link
                if (_expectedLinkData.get(effectiveSiteData.url()).getStatus() == LinkStatus.OK) {
                    checker = LinkContentCheckerFactory.build(effectiveSiteData.url(),
                                                              _expectedLinkData.get(effectiveSiteData.url().toString()),
                                                              Optional.ofNullable(_articles.get(effectiveSiteData.url().toString())),
                                                              effectiveSiteData.dataFileSection().get());
                }
            } else if (_expectedFeedData.containsKey(effectiveSiteData.url())) {
                // this is a feed
                checker = new FeedContentChecker(_expectedFeedData.get(effectiveSiteData.url()),
                                                 effectiveSiteData.dataFileSection().get());
            } else {
                // this is a bug
                ExitHelper.exit("URL " + effectiveSiteData.url() + " is unexpected");
            }
            if (checker != null) {
                try {
                    _checks.put(effectiveSiteData.url(), checker.check());
                } catch (final ContentParserException e) {
                    FileHelper.createParentDirectory(_reportFile);
                    try (final PrintStream reportWriter = new PrintStream(_reportFile.toFile())) {
                        e.printStackTrace(reportWriter);
                    } catch (final IOException e2) {
                        ExitHelper.exit(e2);
                    }
                    _controller.handleUpdate(_file, Status.FAILED_TO_HANDLE, _outputFile, _reportFile);
                    return;
                }
            }
        }

        if (isDataFresh.booleanValue()) {
            _nbSitesRemainingToBeChecked--;
        }

        final Status status = isDataExpected() ? ((_nbSitesRemainingToBeChecked == 0) ? Status.HANDLED_WITH_SUCCESS : Status.HANDLING_NO_ERROR)
                                               : ((_nbSitesRemainingToBeChecked == 0) ? Status.HANDLED_WITH_ERROR : Status.HANDLING_WITH_ERROR);

        /*Logger.log(Logger.Level.INFO)
              .append("URL ")
              .append(siteData.url())
              .append(" ")
              .append(_nbSitesRemainingToBeChecked)
              .append(" status=")
              .append(status.toString())
              .submit();*/

        if (_nbSitesRemainingToBeChecked == 0) {
           try {
               writeOutputFile();
           } catch (final Exception e) {
              FileHelper.createParentDirectory(_reportFile);
              try (final PrintStream reportWriter = new PrintStream(_reportFile.toFile())) {
                  e.printStackTrace(reportWriter);
              } catch (final IOException e2) {
                  ExitHelper.exit(e2);
              }
              _controller.handleUpdate(_file, Status.FAILED_TO_HANDLE, _outputFile, _reportFile);
              return;
           }
           _controller.handleUpdate(_file, status, _outputFile, _reportFile);
        }

    }

    private void writeOutputFile() throws FileNotFoundException, IOException {

        final StringBuilder ok = new StringBuilder();
        final StringBuilder ko = new StringBuilder();
        final StringBuilder checks = new StringBuilder();

        for (final String url : _effectiveData.keySet()) {
            final FullFetchedLinkData effectiveData = _effectiveData.get(url);
            final LinkData expectedData = _expectedLinkData.get(url);
            if (expectedData == null) {
                final FeedData expectedFeedData = _expectedFeedData.get(url);
                final boolean isDataExpected = LinkStatusAnalyzer.doesEffectiveDataMatchesExpectedData(LinkStatus.OK, effectiveData);
                if (isDataExpected) {
                    appendFeedLivenessCheckResult(url, expectedFeedData, effectiveData, ok);
                    ok.append('\n');
                } else {
                    final StringBuilder temp = new StringBuilder();
                    appendFeedLivenessCheckResult(url, expectedFeedData, effectiveData, temp);
                    ko.append(temp.toString());
                    ko.append('\n');
                    Optional<ViolationCorrection> correction = Optional.empty();
                    if (extractHttpCode(effectiveData.headers()).isPresent() &&
                        ((extractHttpCode(effectiveData.headers()).get().intValue() == HttpURLConnection.HTTP_MOVED_PERM) ||
                         (extractHttpCode(effectiveData.headers()).get().intValue() == 308))) {
                        if (effectiveData.previousRedirection() != null) {
                            HeaderFetchedLinkData d = effectiveData.previousRedirection();
                            while (d.previousRedirection() != null) {
                                d = d.previousRedirection();
                            }
                            correction = Optional.of(new UpdateLinkUrlCorrection(url, d.url()));
                        }
                    }
                    _violationController.add(new Violation(_file.toString(),
                                                           _checkType,
                                                           "WrongLiveness",
                                                           new ViolationLocationUnknown(),
                                                           temp.toString(),
                                                           correction));
                }
            } else {
                final boolean isDataExpected = LinkStatusAnalyzer.doesEffectiveDataMatchesExpectedData(expectedData.getStatus(), effectiveData);
                if (isDataExpected) {
                    appendLinkLivenessCheckResult(url, expectedData, effectiveData, ok);
                    ok.append('\n');
                } else {
                    final StringBuilder temp = new StringBuilder();
                    appendLinkLivenessCheckResult(url, expectedData, effectiveData, temp);
                    ko.append(temp.toString());
                    ko.append('\n');
                    final Optional<ViolationCorrection> correction = LinkStatusAnalyzer.getProposedCorrection(expectedData, effectiveData);
                    _violationController.add(new Violation(_file.toString(),
                                                           _checkType,
                                                           "WrongLiveness",
                                                           new ViolationLocationUnknown(),
                                                           temp.toString(),
                                                           correction));
                }
            }
            if (_checks.containsKey(url) && !_checks.get(url).isEmpty()) {
                checks.append('\n');
                checks.append(url);
                checks.append('\n');
                for (final LinkContentCheck c: _checks.get(url)) {
                    checks.append(c.getDescription());
                    checks.append('\n');
                    _violationController.add(new Violation(_file.toString(),
                                                           _checkType,
                                                           c.getCheckName(),
                                                           new ViolationLocationUnknown(),
                                                           url + "\n" + c.getDescription(),
                                                           c.getCorrection()));
                }
            }
        }

        try (final FileOutputStream os = new FileOutputStream(_outputFile.toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            pw.println(ko.toString());
            pw.println("=".repeat(80));
            pw.println(checks.toString());
            pw.println("=".repeat(80));
            pw.println(ok.toString());
        }

        Logger.log(Logger.Level.INFO)
              .append(_outputFile)
              .append(" has been generated")
              .submit();
    }

    /**
     * append (at the end of builder) the result of the liveness check of a link
     *
     * @param url
     * @param expectedData data as expected in the XML file
     * @param effectiveData data as retrieved from Internet
     * @param builder
     */
    private static void appendLinkLivenessCheckResult(final String url,
                                                      final LinkData expectedData,
                                                      final FullFetchedLinkData effectiveData,
                                                      final StringBuilder builder) {

        builder.append("Title = \"" + expectedData.getTitle() + "\"\n");
        if (expectedData.getSubtitles().length > 0) {
            builder.append("Subtitle = \"" + String.join("\" \"",  expectedData.getSubtitles()) + "\"\n");
        }
        builder.append("URL = " + url + "\n");
        builder.append("Expected status = " + expectedData.getStatus() + "\n");
        if (effectiveData.error().isPresent()) {
            builder.append("Effective error = \"" + effectiveData.error().get() + "\"\n");
        }
        builder.append("Effective HTTP code = " + extractPrintableHttpCode(effectiveData.headers()) + "\n");
        final HeaderFetchedLinkData lastRedirection = lastRedirection(effectiveData);
        if (lastRedirection != null) {
            builder.append("Effective HTTP code of last redirection = " + extractPrintableHttpCode(lastRedirection.headers()) + "\n");
        }
        if (effectiveData.previousRedirection() != null) {
            String descriptionOfRedirectionChain = effectiveData.url();
            for (HeaderFetchedLinkData d = effectiveData.previousRedirection(); d != null; d = d.previousRedirection()) {
                descriptionOfRedirectionChain += " → " + d.url();
            }
            builder.append("Redirection chain = " + descriptionOfRedirectionChain + "\n");
        }
        if (LinkStatusAnalyzer.hasMaximumNumberOfRedirectionsBeenReached(effectiveData)) {
            builder.append("The maximum number of redirections has been reached.\n");
        } else {
            final Match match = _redirectionData.getMatch(effectiveData);
            builder.append("Redirection matcher = " + match.name() + "\n");
            builder.append("Redirection matcher expected statuses = " + match.statuses().stream().map(LinkStatus::toString).collect(Collectors.joining( "," )) + "\n");
        }
        final StringBuilder googleUrl = new StringBuilder("https://www.google.com/search?q=%22" +
                                                          URLEncoder.encode(expectedData.getTitle(), StandardCharsets.UTF_8) +
                                                         "%22");
        for (final String st: expectedData.getSubtitles()) {
            googleUrl.append("+%22" + URLEncoder.encode(st, StandardCharsets.UTF_8) + "%22");
        }
        builder.append("Look for article = " + googleUrl.toString() + "\n");
    }

    /**
     * append (at the end of builder) the result of the liveness check of a feed
     *
     * @param url
     * @param expectedData data as expected in the XML file
     * @param effectiveData data as retrieved from Internet
     * @param builder
     */
    private static void appendFeedLivenessCheckResult(final String url,
                                                      final FeedData expectedData,
                                                      final FullFetchedLinkData effectiveData,
                                                      final StringBuilder builder) {

        builder.append("URL = " + url + "\n");
        builder.append("Effective HTTP code = " + extractPrintableHttpCode(effectiveData.headers()) + "\n");
        final HeaderFetchedLinkData lastRedirection = lastRedirection(effectiveData);
        if (lastRedirection != null) {
            builder.append("Effective HTTP code of last redirection = " + extractPrintableHttpCode(lastRedirection.headers()) + "\n");
        }
        if (effectiveData.previousRedirection() != null) {
            String descriptionOfRedirectionChain = effectiveData.url();
            for (HeaderFetchedLinkData d = effectiveData.previousRedirection(); d != null; d = d.previousRedirection()) {
                descriptionOfRedirectionChain += " → " + d.url();
            }
            builder.append("Redirection chain = " + descriptionOfRedirectionChain + "\n");
        }
        if (LinkStatusAnalyzer.hasMaximumNumberOfRedirectionsBeenReached(effectiveData)) {
            builder.append("The maximum number of redirections has been reached.\n");
        } else {
            final Match match = _redirectionData.getMatch(effectiveData);
            builder.append("Redirection matcher = " + match.name() + "\n");
            builder.append("Redirection matcher expected statuses = " + match.statuses().stream().map(LinkStatus::toString).collect(Collectors.joining( "," )) + "\n");
        }
    }

    private static String extractPrintableHttpCode(final Optional<Map<String, List<String>>> headers) {
        return extractHttpCode(headers).map(code -> {
            try {
                return code + " " + HttpHelper.getStringOfCode(code.intValue());
            } catch (@SuppressWarnings("unused") final InvalidHttpCodeException e) {
                return " invalid code! (" + code + ")";
            }
        }).orElse("---");
    }

    private static Optional<Integer> extractHttpCode(final Optional<Map<String, List<String>>> headers) {
        if (headers.isEmpty()) {
            return Optional.empty();
        }
        final int code = HttpHelper.getResponseCodeFromHeaders(headers.get());
        return Optional.of(Integer.valueOf(code));
    }

    private boolean isDataExpected() { //TODO this method is very stupid, we should use a flag instead of computing the status every time

        for (final String url : _effectiveData.keySet()) {
            if (_expectedLinkData.containsKey(url) &&
                !LinkStatusAnalyzer.doesEffectiveDataMatchesExpectedData(_expectedLinkData.get(url).getStatus(), _effectiveData.get(url))) {
                return false;
            }
            if (_checks.containsKey(url) && !_checks.get(url).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private static HeaderFetchedLinkData lastRedirection(final FullFetchedLinkData data) {
        HeaderFetchedLinkData d = data.previousRedirection();
        if (d == null) {
            return null;
        }
        while (d.previousRedirection() != null) {
            d = d.previousRedirection();
        }
        return d;
    }

    private static boolean doNotUseCookies(final String url) { // TODO the decision to allow/disallow cookies should be in the parser
        return url.startsWith("https://www.youtube.com/channel/") ||
               url.startsWith("https://www.youtube.com/user/");
    }
}