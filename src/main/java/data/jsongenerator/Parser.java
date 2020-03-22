package data.jsongenerator;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.Logger;
import utils.XMLHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.DateData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.XmlParser;

public class Parser {

	private final DocumentBuilder a_builder;
	private final ArticleFactory a_articleFactory;
	private final LinkFactory a_linkFactory;
	private final AuthorFactory a_authorFactory;
	
	/**
	 * @param articleFactory
	 * @param linkFactory
	 * @param authorFactory
	 */
	public Parser(final ArticleFactory articleFactory,
			      final LinkFactory linkFactory,
			      final AuthorFactory authorFactory) {
		a_articleFactory = articleFactory;
		a_linkFactory = linkFactory;
		a_authorFactory = authorFactory;
		a_builder = XMLHelper.buildDocumentBuilder();
	}
	
	/**
	 * @param file
	 */
	public void parse(final File file) {
		
		try {
			final Document document = a_builder.parse(file);
			extractArticles(document, file);
		} catch (final SAXException se) {
			Logger.log(Logger.Level.ERROR)
			      .append("Failed to parse the XML file")
			      .append(se)
			      .submit();
		} catch (final IOException ioe) {
			Logger.log(Logger.Level.ERROR)
			      .append("Failed to read the XML file")
			      .append(ioe)
			      .submit();
		}
	}

	/**
	 * @param document
	 * @param file
	 */
	private void extractArticles(final Document document,
			                     final File file) {
        
		final Element racine = document.getDocumentElement();
		final NodeList list = racine.getElementsByTagName("ARTICLE");

		for (int i=0; i<list.getLength(); i++) {

			final Element articleNode = (Element)list.item(i);
			
			final ArticleData articleData = XmlParser.parseArticleNode(articleNode);

			if (articleData.getDate().isPresent()) {
				validateDate(file, articleData.getDate().get());
			}
            
            final Article article = a_articleFactory.buildArticle(file, articleData.getDate());
            
            for (int j = 0; j < articleData.getLinks().size(); j++) {
	            final Link link = a_linkFactory.newLink(article, articleData.getLinks().get(j));
	            article.addLink(link);
			}

            for (int j = 0; j < articleData.getAuthors().size(); j++) {
            	final Author author = a_authorFactory.buildAuthor(articleData.getAuthors().get(j));
            	author.addArticle(article);
				article.addAuthor(author);
			}
		}
	}

	   /**
     * @param file
     */
    public void parsePersonFile(final File file) {
        
        try {
            final Document document = a_builder.parse(file);
            extractPersonLinks(document, file);
        } catch (final SAXException se) {
        	Logger.log(Logger.Level.ERROR)
        	      .append("Failed to parse the XML file")
			      .append(se)
        	      .submit();
        } catch (final IOException ioe) {
        	Logger.log(Logger.Level.ERROR)
        	      .append("Failed to read the XML file")
			      .append(ioe)
        	      .submit();
        }
    }
    

    /**
     * @param document
     * @param file
     */
    private void extractPersonLinks(final Document document,
                                    final File file) {
        
        final Element racine = document.getDocumentElement();
        final NodeList list = racine.getElementsByTagName("CLIST");

        for (int i=0; i<list.getLength(); i++) {

            final Element clistNode = (Element)list.item(i);

            final Node titleNode =  clistNode.getFirstChild();
            if (!titleNode.getNodeName().equals("TITLE")) {
                throw new UnsupportedOperationException("Unexpected XML structure (the first child of a CLIST node is not a TITLE node)");
            }
            
            final Node authorNode =  titleNode.getFirstChild();
            if (!authorNode.getNodeName().equals("AUTHOR")) {
                throw new UnsupportedOperationException("Unexpected XML structure (the first child of the first child of a CLIST node is not a AUTHOR node)");
            }

            final AuthorData authorData = XmlParser.parseAuthorNode((Element)authorNode);

            final Author author = a_authorFactory.peekAuthor(authorData);
            
            if (author == null) continue;
            
            for (int j = 0; j < clistNode.getElementsByTagName("ITEM").getLength(); j++) {
                
                final Element linkNode = (Element)clistNode.getElementsByTagName("ITEM").item(j);
                if (linkNode.getChildNodes().getLength() != 1) {
                    throw new UnsupportedOperationException("Illegal number of children nodes");                    
                }
                if (!((Element)linkNode.getChildNodes().item(0)).getTagName().equals("X")) {
                    throw new UnsupportedOperationException("Illegal child node");                    
                }
                final LinkData linkData = XmlParser.parseXNode((Element)linkNode.getChildNodes().item(0));
                final Link link = a_linkFactory.newLink(null, linkData);
                
                author.addLink(link);
            }
            
        }
    }

    /**
     * validate a DATE node
     * if the date is incorrect, throws a {@link UnsupportedOperationException}
     * if the date is correct, does nothing
     */
    private void validateDate(final File file, final DateData dateData) {
        
        try {
            final LocalDateTime date = LocalDateTime.of(dateData.getYear(),
                                                        dateData.getMonth().orElse(1),
                                                        dateData.getDay().orElse(1),
                                                        0,
                                                        0);
            if ((date.getYear() != dateData.getYear()) ||
                (date.getMonthValue() != dateData.getMonth().orElse(1)) ||
                (date.getDayOfMonth() != dateData.getDay().orElse(1))) {
                throw new UnsupportedOperationException("Invalid values for DATE node in file " + file.getPath());
            }
        } catch (final DateTimeException e) {
            throw new UnsupportedOperationException("Invalid values for DATE node in file " + file.getPath() + " " + e.getMessage());                        
        }
    }
}
