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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.checker.nodeChecker.DateChecker;
import data.nodechecker.checker.nodeChecker.DoubleSpaceChecker;
import data.nodechecker.checker.nodeChecker.DurationChecker;
import data.nodechecker.checker.nodeChecker.DurationPresenceChecker;
import data.nodechecker.checker.nodeChecker.EllipsisChecker;
import data.nodechecker.checker.nodeChecker.ExtremitySpaceChecker;
import data.nodechecker.checker.nodeChecker.FormatChecker;
import data.nodechecker.checker.nodeChecker.FormatFromURLChecker;
import data.nodechecker.checker.nodeChecker.KeyChecker;
import data.nodechecker.checker.nodeChecker.LanguageChecker;
import data.nodechecker.checker.nodeChecker.MiddleNewlineChecker;
import data.nodechecker.checker.nodeChecker.MissingSpaceChecker;
import data.nodechecker.checker.nodeChecker.ModifierKeyChecker;
import data.nodechecker.checker.nodeChecker.NodeChecker;
import data.nodechecker.checker.nodeChecker.NonEmptyChecker;
import data.nodechecker.checker.nodeChecker.NonNormalizedAuthorChecker;
import data.nodechecker.checker.nodeChecker.NonNormalizedURLChecker;
import data.nodechecker.checker.nodeChecker.ProtectionFromURLChecker;
import data.nodechecker.checker.nodeChecker.TableSortChecker;
import data.nodechecker.checker.nodeChecker.TitleFormatChecker;
import data.nodechecker.checker.nodeChecker.URLProtocolChecker;
import utils.ExitHelper;
import utils.FileHelper;

public class NodeValueChecker implements FileHandler {

    final private Path _homepagePath;
    final private Path _tmpPath;
    final private DataController _controller;
    PrintWriter _pw;
    private final DocumentBuilder a_builder;
    final private Set<NodeChecker> _nodeCheckers;
    
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
        
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try{
            builder = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException pce){
            System.out.println("Failed to configure the XML parser");
            pce.printStackTrace();
        }

        a_builder = builder;

         _nodeCheckers = new HashSet<NodeChecker>(); 
        _nodeCheckers.add(new ExtremitySpaceChecker());
        _nodeCheckers.add(new MiddleNewlineChecker());
        _nodeCheckers.add(new EllipsisChecker());
        _nodeCheckers.add(new DoubleSpaceChecker());
        _nodeCheckers.add(new MissingSpaceChecker());
        _nodeCheckers.add(new TitleFormatChecker());
        _nodeCheckers.add(new NonEmptyChecker());
        _nodeCheckers.add(new FormatChecker());
        _nodeCheckers.add(new LanguageChecker());
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
            final List<Error> errors = check(file, content);
            if (errors.size() > 0) {
                for (Error error : errors ) {
                    _pw.println(" tag = \""       + error.getTag()       + "\"" +
                                " value = \""     + error.getValue()     + "\"" +
                                " violation = \"" + error.getViolation() + "\"" +
                                " detail = \""    + error.getDetail()    + "\"");

                }
                status = Status.HANDLED_WITH_ERROR;
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
        return status;
    }
    
    public List<Error> check(final Path file,
                             final String content) {
        
        try {
            final Document document = a_builder.parse(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            return checkNode(file.toFile(), document.getDocumentElement());
        } catch (final SAXException | IOException e) {
            ExitHelper.exit(e);
        }
        
        // NOT REACHED
        return(null);
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
        return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_nodevaluecheck", "txt");
    }
    
    @Override
    public Path getReportFile(final Path file) {
         return FileHelper.computeTargetFile(_homepagePath, _tmpPath, file, "_report_nodevaluecheck", "txt");
    }

    @Override
    public boolean outputFileMustBeRegenerated(final Path file) {
        
        /*if (!getOutputFile(file).toFile().isFile()
                || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified())) {
            System.out.println("----- BEGIN DEBUG");
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            System.out.println("source file = " + file);
            System.out.println("target file = " + getOutputFile(file));
            System.out.println("source file timestamp = " + df2.format(file.toFile().lastModified()));
            System.out.println("target file timestamp = " + df2.format(getOutputFile(file).toFile().lastModified()));
            System.out.println("----- END DEBUG");
        }*/
        return !getOutputFile(file).toFile().isFile()
               || (getOutputFile(file).toFile().lastModified() <= file.toFile().lastModified());
    }

    private List<Error> checkNode(final File file,
                                  final Element e) {

        final List<Error> errors = new ArrayList<Error>();
        final NodeList children = e.getChildNodes();

        for (int j = 0; j < children.getLength(); j++) {
            if ( children.item(j).getNodeType() == Node.ELEMENT_NODE ) {
                errors.addAll(checkNode(file, (Element)children.item(j)));
            }
        }

        for (final NodeChecker checker: _nodeCheckers) {
            if (checker.getTagSelector().isTagCheckable(e.getTagName())) {
                for (final NodeChecker.NodeRule rule : checker.getRules()) {
                    final CheckStatus status = rule.checkElement(e);
                    if ( status != null ) {
                        errors.add(new Error(e.getTagName(), e.getTextContent(), rule.getDescription(), status.getDetail()));                         
                    }

                }
            }
        }
        
        return errors;
    }
    
    static public class Error {

        final private String _tag;
        final private String _value;
        final private String _violation;
        final private String _detail;

        public Error(final String tag,
                     final String value,
                     final String violation,
                     final String detail) {
            _tag = tag;
            _value = value;
            _violation = violation;
            _detail = detail;
        }

        public String getTag() {
            return _tag;
        }

        public String getValue() {
            return _value;
        }

        public String getViolation() {
            return _violation;
        }

        public String getDetail() {
            return _detail;
        }
    }
}
