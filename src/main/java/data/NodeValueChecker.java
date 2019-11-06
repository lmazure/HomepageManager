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
import data.nodechecker.checker.nodeChecker.XMLSchemaValidationChecker;
import utils.ExitHelper;
import utils.FileHelper;

public class NodeValueChecker implements FileHandler {

    final private Path _homepagePath;
    final private Path _tmpPath;
    final private DataController _controller;
    PrintWriter _pw;
    private List<Error> _errors;
    private final DocumentBuilder a_builder;
    
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

    }
    
    @Override
    public Status handleCreation(final Path file) {

        Status status = Status.HANDLED_WITH_SUCCESS;
        
        try (final FileOutputStream os = new FileOutputStream(getOutputFile(file).toFile());
             final PrintWriter pw = new PrintWriter(os)) {
            _pw = pw; // TODO fix this crap
            final List<Error> errors = check(file);
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
    
    public List<Error> check(final Path file) {
        _errors = new ArrayList<Error>();
        
        final Set<NodeChecker> nodeCheckers = new HashSet<NodeChecker>(); 
        nodeCheckers.add(new ExtremitySpaceChecker());
        nodeCheckers.add(new MiddleNewlineChecker());
        nodeCheckers.add(new EllipsisChecker());
        nodeCheckers.add(new DoubleSpaceChecker());
        nodeCheckers.add(new MissingSpaceChecker());
        nodeCheckers.add(new TitleFormatChecker());
        nodeCheckers.add(new NonEmptyChecker());
        nodeCheckers.add(new FormatChecker());
        nodeCheckers.add(new LanguageChecker());
        nodeCheckers.add(new FormatFromURLChecker());
        nodeCheckers.add(new NonNormalizedURLChecker());
        nodeCheckers.add(new NonNormalizedAuthorChecker());
        nodeCheckers.add(new TableSortChecker());
        nodeCheckers.add(new DurationPresenceChecker());
        nodeCheckers.add(new URLProtocolChecker());
        nodeCheckers.add(new DateChecker());
        nodeCheckers.add(new ModifierKeyChecker());
        nodeCheckers.add(new KeyChecker());
        nodeCheckers.add(new DurationChecker());
        nodeCheckers.add(new ProtectionFromURLChecker());
        nodeCheckers.add(new XMLSchemaValidationChecker(file.toFile()));

        try {
            final Document document = a_builder.parse(file.toFile());
            checkNodesInFile(document, file.toFile(), nodeCheckers);
        } catch (final SAXException se){
            System.out.println("Failed to parse the XML file: " + file);
            se.printStackTrace();
        } catch (final IOException ioe){
            System.out.println("Failed to read the XML file: " + file);
            ioe.printStackTrace();
        }
        
        return _errors; 
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

    private void checkNodesInFile(final Document document,
                                  final File file,
                                  final Set<NodeChecker> nodeCheckers) {

        checkNode(file, document.getDocumentElement(), nodeCheckers);
    }

    private void checkNode(final File file,
                           final Element e,
                           final Set<NodeChecker> nodeCheckers) {

        final NodeList children = e.getChildNodes();

        for (int j=0; j<children.getLength(); j++) {
            if ( children.item(j).getNodeType() == Node.ELEMENT_NODE ) {
                checkNode(file, (Element)children.item(j), nodeCheckers);
            }
        }

        for (NodeChecker checker: nodeCheckers) {

            if ( checker.getTagSelector().isTagCheckable(e.getTagName())) {
                final NodeChecker.NodeRule rules[] = checker.getRules();

                for (int k = 0; k<rules.length; k++) {

                    final CheckStatus status = rules[k].checkElement(e);

                    if ( status != null ) {
                        _errors.add(new Error(e.getTagName(), e.getTextContent(), rules[k].getDescription(), status.getDetail()));                         
                    }

                }
            }
        }
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
