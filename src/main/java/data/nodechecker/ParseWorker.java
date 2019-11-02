package data.nodechecker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import data.nodechecker.checker.nodeChecker.NodeChecker;

/**
 * @author Laurent
 *
 */
public class ParseWorker implements Runnable {

	private final File a_fileToBeParsed;
	private final Set<Logger> a_loggers;
	private final Set<NodeChecker> a_nodeCheckers; 
	private final DocumentBuilder a_builder;


	/**
	 * @param fileToBeParsed
	 * @param checkers
	 * @param loggers
	 */
	public ParseWorker(
			final File fileToBeParsed,
			final Set<NodeChecker> nodeCheckers,
			final Set<Logger> loggers) {
		a_fileToBeParsed = fileToBeParsed;
		a_nodeCheckers = nodeCheckers;
		a_loggers = loggers;

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

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		System.out.println(Thread.currentThread().getName() + " is parsing \"" + a_fileToBeParsed + "\"");

		try {
		    checkFileContent(a_fileToBeParsed);
			final Document document = a_builder.parse(a_fileToBeParsed);
			checkNodesInFile(document, a_fileToBeParsed);
		} catch (final SAXException se){
			System.out.println("Failed to parse the XML file: " + a_fileToBeParsed.getAbsolutePath());
			se.printStackTrace();
		} catch (final IOException ioe){
			System.out.println("Failed to read the XML file: " + a_fileToBeParsed.getAbsolutePath());
			ioe.printStackTrace();
		}

		System.out.println(Thread.currentThread().getName() + " has parsed \"" + a_fileToBeParsed + "\"");
	}

	private void checkFileContent(final File file) throws IOException {
	    
        final byte[] encoded = Files.readAllBytes(file.toPath());
        final String content = new String(encoded, StandardCharsets.UTF_8);
        
       if (!content.contains("<PAGE")) return;
    }
	
	/**
	 * @param document
	 * @param file
	 */
	private void checkNodesInFile(
			final Document document,
			final File file) {

		final Element element = document.getDocumentElement();

		if (!element.getNodeName().equals("PAGE")) {
			System.out.println("Ignored the file: " + file.getAbsolutePath());
			return;			
		}
		checkNode(file, element);
	}

	/**
	 * @param file
	 * @param e
	 */
	private void checkNode(
			final File file,
			final Element e) {

		final NodeList children = e.getChildNodes();

		for (int j=0; j<children.getLength(); j++) {
			if ( children.item(j).getNodeType() == Node.ELEMENT_NODE ) {
				checkNode(file, (Element)children.item(j));
			}
		}

		for (NodeChecker checker: a_nodeCheckers) {

			if ( checker.getTagSelector().isTagCheckable(e.getTagName())) {
				final NodeChecker.NodeRule rules[] = checker.getRules();

				for (int k=0; k<rules.length; k++) {

					final CheckStatus status = rules[k].checkElement(e);

					if ( status != null ) {
						for (Logger logger: a_loggers) {
							logger.record(file, e.getTagName(), e.getTextContent(), rules[k].getDescription(), status.getDetail());							
						}
					}

				}
			}
		}
	}

}
