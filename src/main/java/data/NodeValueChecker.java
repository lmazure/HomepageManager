package data;

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

import data.nodechecker.checker.nodeChecker.ArticleDateChecker;
import data.nodechecker.checker.nodeChecker.AuthorsChecker;
import data.nodechecker.checker.nodeChecker.CommentChecker;
import data.nodechecker.checker.nodeChecker.DateChecker;
import data.nodechecker.checker.nodeChecker.DoubleSpaceChecker;
import data.nodechecker.checker.nodeChecker.DurationChecker;
import data.nodechecker.checker.nodeChecker.DurationPresenceChecker;
import data.nodechecker.checker.nodeChecker.EllipsisChecker;
import data.nodechecker.checker.nodeChecker.ExtremitySpaceChecker;
import data.nodechecker.checker.nodeChecker.FormatFromURLChecker;
import data.nodechecker.checker.nodeChecker.KeyChecker;
import data.nodechecker.checker.nodeChecker.MiddleNewlineChecker;
import data.nodechecker.checker.nodeChecker.IncorrectSpaceChecker;
import data.nodechecker.checker.nodeChecker.ModifierKeyChecker;
import data.nodechecker.checker.nodeChecker.NodeCheckError;
import data.nodechecker.checker.nodeChecker.NodeChecker;
import data.nodechecker.checker.nodeChecker.NonEmptyChecker;
import data.nodechecker.checker.nodeChecker.NonNormalizedAuthorChecker;
import data.nodechecker.checker.nodeChecker.NonNormalizedURLChecker;
import data.nodechecker.checker.nodeChecker.PredecessorArticleChecker;
import data.nodechecker.checker.nodeChecker.ProtectionFromURLChecker;
import data.nodechecker.checker.nodeChecker.SpaceBetweenTagsChecker;
import data.nodechecker.checker.nodeChecker.TableSortChecker;
import data.nodechecker.checker.nodeChecker.TitleFormatChecker;
import data.nodechecker.checker.nodeChecker.URLProtocolChecker;
import utils.ExitHelper;
import utils.FileHelper;
import utils.Logger;
import utils.XmlHelper;

public class NodeValueChecker implements FileHandler {

    private final Path _homepagePath;
    private final Path _tmpPath;
    private final DataController _controller;
    private final DocumentBuilder _builder;
    private final Set<NodeChecker> _nodeCheckers;

    private final static Lock _lock = new ReentrantLock();

    /**
     * This class checks the characters of the XML files.
     *
     * @param homepagePath
     * @param tmpPath
     */
    public NodeValueChecker(final Path homepagePath,
                            final Path tmpPath,
                            final DataController controller) {
        _homepagePath = homepagePath;
        _tmpPath = tmpPath;
        _controller = controller;
        _lock.lock();
        try {
            _builder = XmlHelper.buildDocumentBuilder();
        } finally {
            _lock.unlock();
        }
        _nodeCheckers = new HashSet<>();
        _nodeCheckers.add(new ExtremitySpaceChecker());
        _nodeCheckers.add(new MiddleNewlineChecker());
        _nodeCheckers.add(new EllipsisChecker());
        _nodeCheckers.add(new DoubleSpaceChecker());
        _nodeCheckers.add(new AuthorsChecker());
        _nodeCheckers.add(new ArticleDateChecker());
        _nodeCheckers.add(new PredecessorArticleChecker());
        _nodeCheckers.add(new IncorrectSpaceChecker());
        _nodeCheckers.add(new TitleFormatChecker());
        _nodeCheckers.add(new NonEmptyChecker());
        _nodeCheckers.add(new FormatFromURLChecker());
        _nodeCheckers.add(new NonNormalizedURLChecker());
        _nodeCheckers.add(new NonNormalizedAuthorChecker());
        _nodeCheckers.add(new TableSortChecker());
        _nodeCheckers.add(new DurationPresenceChecker());
        _nodeCheckers.add(new URLProtocolChecker());
        _nodeCheckers.add(new DateChecker());
        _nodeCheckers.add(new ModifierKeyChecker());
        _nodeCheckers.add(new KeyChecker());
        _nodeCheckers.add(new DurationChecker());
        _nodeCheckers.add(new ProtectionFromURLChecker());
        _nodeCheckers.add(new SpaceBetweenTagsChecker());
        _nodeCheckers.add(new CommentChecker());
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
                    pw.println(" tag = \""       + error.getTag()       + "\"" +
                               " value = \""     + error.getValue()     + "\"" +
                               " violation = \"" + error.getViolation() + "\"" +
                               " detail = \""    + error.getDetail()    + "\"");

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
            status = Status.FAILED_TO_HANDLED;
        }

        _controller.handleCreation(file, status, getOutputFile(file), getReportFile(file));
    }

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
    }

    @Override
    public Path getOutputFile(final Path file) {
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_nodevaluecheck", "txt");
    }

    @Override
    public Path getReportFile(final Path file) {
         return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_nodevaluecheck", "txt");
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
