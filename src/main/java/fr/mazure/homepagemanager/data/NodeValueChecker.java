package fr.mazure.homepagemanager.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.mazure.homepagemanager.data.nodechecker.ArticleCommentChecker;
import fr.mazure.homepagemanager.data.nodechecker.ArticleDateChecker;
import fr.mazure.homepagemanager.data.nodechecker.AuthorsChecker;
import fr.mazure.homepagemanager.data.nodechecker.BritishChecker;
import fr.mazure.homepagemanager.data.nodechecker.CommentChecker;
import fr.mazure.homepagemanager.data.nodechecker.DateChecker;
import fr.mazure.homepagemanager.data.nodechecker.DoubleSpaceChecker;
import fr.mazure.homepagemanager.data.nodechecker.DurationChecker;
import fr.mazure.homepagemanager.data.nodechecker.DurationPresenceChecker;
import fr.mazure.homepagemanager.data.nodechecker.EllipsisChecker;
import fr.mazure.homepagemanager.data.nodechecker.ExtremitySpaceChecker;
import fr.mazure.homepagemanager.data.nodechecker.FormatFromURLChecker;
import fr.mazure.homepagemanager.data.nodechecker.IncorrectSpaceChecker;
import fr.mazure.homepagemanager.data.nodechecker.KeyChecker;
import fr.mazure.homepagemanager.data.nodechecker.MiddleNewlineChecker;
import fr.mazure.homepagemanager.data.nodechecker.ModifierKeyChecker;
import fr.mazure.homepagemanager.data.nodechecker.NodeCheckError;
import fr.mazure.homepagemanager.data.nodechecker.NodeChecker;
import fr.mazure.homepagemanager.data.nodechecker.NonEmptyChecker;
import fr.mazure.homepagemanager.data.nodechecker.NonNormalizedAuthorChecker;
import fr.mazure.homepagemanager.data.nodechecker.NonNormalizedURLChecker;
import fr.mazure.homepagemanager.data.nodechecker.PredecessorArticleChecker;
import fr.mazure.homepagemanager.data.nodechecker.ProtectionFromURLChecker;
import fr.mazure.homepagemanager.data.nodechecker.SpaceBetweenTagsChecker;
import fr.mazure.homepagemanager.data.nodechecker.TableSortChecker;
import fr.mazure.homepagemanager.data.nodechecker.TitleFormatChecker;
import fr.mazure.homepagemanager.data.nodechecker.UrlChecker;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.FileNameHelper;
import fr.mazure.homepagemanager.utils.Logger;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 * This class checks the XM node values of XML files.
 */
public class NodeValueChecker implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final DataController _controller;
    private final ViolationDataController _violationController;
    private final DocumentBuilder _builder;
    private final Set<NodeChecker> _nodeCheckers;

    private static final Lock _lock = new ReentrantLock();
    private static final String s_checkType = "node";

    /**
     * Constructor
     * @param homepagePath path to the directory containing the pages
     * @param tmpPath path to the directory containing the temporary files and log files
     * @param controller controller to notify of additional / removed violations
     * @param violationController controller to notify of additional / removed violations
     */
    public NodeValueChecker(final Path homepagePath,
                            final Path tmpPath,
                            final DataController controller,
                            final ViolationDataController violationController) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _violationController = violationController;
        _lock.lock();
        try {
            _builder = XmlHelper.buildDocumentBuilder();
        } finally {
            _lock.unlock();
        }
        _nodeCheckers = new HashSet<>();
        _nodeCheckers.add(new ArticleCommentChecker());
        _nodeCheckers.add(new ArticleDateChecker());
        _nodeCheckers.add(new AuthorsChecker());
        _nodeCheckers.add(new BritishChecker());
        _nodeCheckers.add(new CommentChecker());
        _nodeCheckers.add(new DateChecker());
        _nodeCheckers.add(new DoubleSpaceChecker());
        _nodeCheckers.add(new DurationChecker());
        _nodeCheckers.add(new DurationPresenceChecker());
        _nodeCheckers.add(new EllipsisChecker());
        _nodeCheckers.add(new ExtremitySpaceChecker());
        _nodeCheckers.add(new FormatFromURLChecker());
        _nodeCheckers.add(new IncorrectSpaceChecker());
        _nodeCheckers.add(new KeyChecker());
        _nodeCheckers.add(new MiddleNewlineChecker());
        _nodeCheckers.add(new ModifierKeyChecker());
        _nodeCheckers.add(new NonEmptyChecker());
        _nodeCheckers.add(new NonNormalizedAuthorChecker());
        _nodeCheckers.add(new NonNormalizedURLChecker());
        _nodeCheckers.add(new PredecessorArticleChecker());
        _nodeCheckers.add(new ProtectionFromURLChecker());
        _nodeCheckers.add(new SpaceBetweenTagsChecker());
        _nodeCheckers.add(new TableSortChecker());
        _nodeCheckers.add(new TitleFormatChecker());
        _nodeCheckers.add(new UrlChecker());

    }

    @Override
    public void handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;

        FileHelper.createParentDirectory(getOutputFile(file));

        try (final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            final List<NodeCheckError> errors = check(file);
            if (errors.size() > 0) {
                for (final NodeCheckError error: errors) {
                    final String message = " tag = \""       + error.tag()       + "\"\n" +
                                           " value = \""     + error.value()     + "\"\n" +
                                           " violation = \"" + error.violation() + "\"\n" +
                                           " detail = \""    + error.detail()    + "\"\n";
                    pw.println(message);
                    _violationController.add(new Violation(file.toString(),
                                                           s_checkType,
                                                           error.checkName(),
                                                           new ViolationLocationUnknown(),
                                                           message,
                                                           error.correction()));
                }
                status = Status.HANDLED_WITH_ERROR;
                Logger.log(Logger.Level.INFO)
                      .append(getOutputFile(file))
                      .append(" is generated")
                      .submit();
            } else {
                // must write something in the file otherwise its last modification datetime will be incorrect
                pw.println("OK");
            }
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
     * @param file file to be checked
     * @return list of violation
     * @throws SAXException exception if the XML is invalid
     */
    public List<NodeCheckError> check(final Path file) throws SAXException {

        try {
            Document document;
            synchronized (_builder) {
                document = _builder.parse(file.toFile());
            }
            return checkNode(file.toFile(), document.getDocumentElement());
        } catch (final IOException e) {
            ExitHelper.exit(e);
        }

        // NOT REACHED
        return(null);
    }

    @Override
    public void handleDeletion(final Path file) {

        FileHelper.deleteFile(getOutputFile(file));
        FileHelper.deleteFile(getReportFile(file));

        _controller.handleDeletion(file, Status.HANDLED_WITH_SUCCESS, getOutputFile(file), getReportFile(file));

        _violationController.remove(v -> (v.getFile().equals(file.toString()) && v.getType().equals(s_checkType)));    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileNameHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_nodevaluecheck", "txt");
    }

    @Override
    public Path getReportFile(final Path file) {
         return FileNameHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_nodevaluecheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {

        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }

    private List<NodeCheckError> checkNode(final File file,
                                           final Element e) {

        final List<NodeCheckError> errors = new ArrayList<>();
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if (children.item(j).getNodeType() == Node.ELEMENT_NODE) {
                errors.addAll(checkNode(file, (Element)children.item(j)));
            }
        }

        for (final NodeChecker checker: _nodeCheckers) {
            if (checker.isElementCheckable(e)) {
                errors.addAll(checker.check(e));
            }
        }

        return errors;
    }
}
