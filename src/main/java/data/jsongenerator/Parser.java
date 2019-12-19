package data.jsongenerator;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import utils.XMLHelper;


/**
 * @author Laurent
 *
 */
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
		
		System.out.println("parsing file " + file.getName());
		
		try {
			final Document document = a_builder.parse(file);
			extractArticles(document, file);
		} catch (final SAXException se) {
			System.out.println("Failed to parse the XML file");
			se.printStackTrace();
		} catch (final IOException ioe) {
			System.out.println("Failed to read the XML file");
			ioe.printStackTrace();
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

			Integer dateYear=null, dateMonth=null, dateDay=null;
			
			final NodeList dateNodes =  articleNode.getElementsByTagName("DATE");
			
            if (dateNodes.getLength() == 1) {
            	final Element dateNode = (Element)dateNodes.item(0);
            	
				final NodeList dateYearNodes = dateNode.getElementsByTagName("YEAR");
				if (dateYearNodes.getLength() == 1) {
					dateYear = Integer.parseInt(dateYearNodes.item(0).getTextContent());
				} else {
                    throw new UnsupportedOperationException("Wrong number of YEAR nodes in file " + file.getPath());
				}
				final NodeList dateMonthNodes = dateNode.getElementsByTagName("MONTH");
				if (dateMonthNodes.getLength() == 1) {
					dateMonth = Integer.parseInt(dateMonthNodes.item(0).getTextContent());
				} else if (dateMonthNodes.getLength() > 1) {
                    throw new UnsupportedOperationException("Wrong number of MONTH nodes in file " + file.getPath());
                }
				final NodeList dateDayNodes = dateNode.getElementsByTagName("DAY");
				if (dateDayNodes.getLength() == 1) {
					dateDay = Integer.parseInt(dateDayNodes.item(0).getTextContent());
				} else if (dateDayNodes.getLength() > 1) {
                    throw new UnsupportedOperationException("Wrong number of DAY nodes in file " + file.getPath());
                }
				
				validateDate(file, dateYear, dateMonth, dateDay);
			}
            
            final Article article = a_articleFactory.buildArticle(file, dateYear, dateMonth, dateDay);
            
			final NodeList linkNodes =  articleNode.getChildNodes();

			for (int j=0; j<linkNodes.getLength(); j++) {
				
				if ( linkNodes.item(j).getNodeType() != Node.ELEMENT_NODE ) continue;
            	final Element linkNode = (Element)linkNodes.item(j);
                if ( linkNode.getTagName().compareTo("X") != 0 ) continue;
                
                final ParserLinkDto linkDto = parseLinkNode(linkNode);
	            final Link link = a_linkFactory.newLink(article, linkDto);
				
				article.addLink(link);
			}

			final NodeList authorNodes =  articleNode.getElementsByTagName("AUTHOR");

			for (int j=0; j<authorNodes.getLength(); j++) {
				
            	final Element authorNode = (Element)authorNodes.item(j);

    			final ParserAuthorDto authorDto = parseLinkAuthor(authorNode);

            	final Author author = a_authorFactory.buildAuthor(authorDto);
            	author.addArticle(article);
				article.addAuthor(author);
			}
		}
	}

	   /**
     * @param file
     */
    public void parsePersonFile(final File file) {
        
        System.out.println("parsing person file " + file.getName());
        
        try {
            final Document document = a_builder.parse(file);
            extractPersonLinks(document, file);
        } catch (final SAXException se) {
            System.out.println("Failed to parse the XML file");
            se.printStackTrace();
        } catch (final IOException ioe) {
            System.out.println("Failed to read the XML file");
            ioe.printStackTrace();
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

            final ParserAuthorDto authorDto = parseLinkAuthor((Element)authorNode);

            final Author author = a_authorFactory.peekAuthor(authorDto);
            
            if (author == null) continue;
            
            for (int j = 0; j < clistNode.getElementsByTagName("ITEM").getLength(); j++) {
                
                final Element linkNode = (Element)clistNode.getElementsByTagName("ITEM").item(j);
                final ParserLinkDto linkDto = parseLinkNode(linkNode);
                final Link link = a_linkFactory.newLink(null, linkDto);
                
                author.addLink(link);
            }
            
        }
    }

    private ParserAuthorDto parseLinkAuthor(final Element authorNode) {
        
        String namePrefix = null;
        if ( authorNode.getElementsByTagName("NAMEPREFIX").getLength() == 1 ) {
        	namePrefix = authorNode.getElementsByTagName("NAMEPREFIX").item(0).getTextContent();
        } else if ( authorNode.getElementsByTagName("NAMEPREFIX").getLength() != 0 ) {
        	throw new UnsupportedOperationException("Wrong number of NAMEPREFIX nodes");
        }

        String firstName = null;
        if ( authorNode.getElementsByTagName("FIRSTNAME").getLength() == 1 ) {
        	firstName = authorNode.getElementsByTagName("FIRSTNAME").item(0).getTextContent();
        } else if ( authorNode.getElementsByTagName("FIRSTNAME").getLength() != 0 ) {
        	throw new UnsupportedOperationException("Wrong number of FIRSTNAME nodes");
        }

        String middleName = null;
        if ( authorNode.getElementsByTagName("MIDDLENAME").getLength() == 1 ) {
        	middleName = authorNode.getElementsByTagName("MIDDLENAME").item(0).getTextContent();
        } else if ( authorNode.getElementsByTagName("MIDDLENAME").getLength() != 0 ) {
        	throw new UnsupportedOperationException("Wrong number of MIDDLENAME nodes");
        }

        String lastName = null;
        if ( authorNode.getElementsByTagName("LASTNAME").getLength() == 1 ) {
        	lastName = authorNode.getElementsByTagName("LASTNAME").item(0).getTextContent();
        } else if ( authorNode.getElementsByTagName("LASTNAME").getLength() != 0 ) {
        	throw new UnsupportedOperationException("Wrong number of LASTNAME nodes");
        }

        String nameSuffix = null;
        if ( authorNode.getElementsByTagName("NAMESUFFIX").getLength() == 1 ) {
        	nameSuffix = authorNode.getElementsByTagName("NAMESUFFIX").item(0).getTextContent();
        } else if ( authorNode.getElementsByTagName("NAMESUFFIX").getLength() != 0 ) {
        	throw new UnsupportedOperationException("Wrong number of NAMESUFFIX nodes");
        }

        String givenName = null;
        if ( authorNode.getElementsByTagName("GIVENNAME").getLength() == 1 ) {
        	givenName = authorNode.getElementsByTagName("GIVENNAME").item(0).getTextContent();
        } else if ( authorNode.getElementsByTagName("GIVENNAME").getLength() != 0 ) {
        	throw new UnsupportedOperationException("Wrong number of GIVENNAME nodes");
        }

        final ParserAuthorDto authorDto = new ParserAuthorDto(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
        return authorDto;
    }

    private ParserLinkDto parseLinkNode(final Element linkNode) {
        
        final NodeList titleNodes = linkNode.getElementsByTagName("T");
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = linkNode.getElementsByTagName("ST");
        final String subtitle = (subtitleNodes.getLength()==1) ? ((Element)subtitleNodes.item(0)).getTextContent() : null;

        final NodeList urlNodes = linkNode.getElementsByTagName("A");
        final String url = ((Element)urlNodes.item(0)).getTextContent();

        final NodeList languageNodes = linkNode.getElementsByTagName("L");
        final String languages[] = new String[languageNodes.getLength()];
        for (int k=0; k<languageNodes.getLength(); k++ ) languages[k] = ((Element)languageNodes.item(k)).getTextContent();

        final NodeList formatNodes = linkNode.getElementsByTagName("F");
        final String formats[] = new String[formatNodes.getLength()];
        for (int k=0; k<formatNodes.getLength(); k++ ) formats[k] = ((Element)formatNodes.item(k)).getTextContent();

        Integer durationHour = null, durationMinute = null, durationSecond = null;
        
        final NodeList durationNodes =  linkNode.getElementsByTagName("DURATION");
        
        if ( durationNodes.getLength()==1 ) {
            final Element durationNode = (Element)durationNodes.item(0);
            
            final NodeList durationHourNodes = durationNode.getElementsByTagName("HOUR");
            if ( durationHourNodes.getLength()==1 ) {
                durationHour = Integer.parseInt(durationHourNodes.item(0).getTextContent());
            }
            final NodeList durationMinuteNodes = durationNode.getElementsByTagName("MINUTE");
            if ( durationMinuteNodes.getLength()==1 ) {
                durationMinute = Integer.parseInt(durationMinuteNodes.item(0).getTextContent());
            }
            final NodeList durationSecondNodes = durationNode.getElementsByTagName("SECOND");
            if ( durationSecondNodes.getLength()==1 ) {
                durationSecond = Integer.parseInt(durationSecondNodes.item(0).getTextContent());
            }
        }
        
        final Attr statusAttribute = linkNode.getAttributeNode("status");
        final String status = (statusAttribute!=null) ? statusAttribute.getValue() : null;

        final Attr protectionAttribute = linkNode.getAttributeNode("protection");
        final String protection = (protectionAttribute!=null) ? protectionAttribute.getValue() : null;

        return new ParserLinkDto(title, subtitle, url, languages, formats, durationHour, durationMinute, durationSecond, status, protection);
    }
    

    /**
     * validate a DATE node
     * if the date is incorrect, throws a {@link UnsupportedOperationException}
     * if the date is correct, does nothing
     * 
     * @param file
     * @param dateYear
     * @param dateMonth
     * @param dateDay
     */
    private void validateDate(final File file, Integer dateYear, Integer dateMonth, Integer dateDay) {
        try {
            final LocalDateTime date = LocalDateTime.of(dateYear,
                                                        (dateMonth != null) ? dateMonth : 1,
                                                        (dateDay != null) ? dateDay : 1,
                                                        0,
                                                        0);
            if ((date.getYear() != dateYear) ||
                ((dateMonth != null) && (date.getMonthValue() != dateMonth)) ||
                ((dateDay != null) && (date.getDayOfMonth() != dateDay))) {
                throw new UnsupportedOperationException("Invalid values for DATE node in file " + file.getPath());
            }
        } catch (final DateTimeException e) {
            throw new UnsupportedOperationException("Invalid values for DATE node in file " + file.getPath() + " " + e.getMessage());                        
        }
    }
}
