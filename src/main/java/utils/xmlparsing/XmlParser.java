package utils.xmlparsing;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {

    public static ArticleData parseArticleNode(final Element articleNode) {
        
        if (!articleNode.getTagName().equals("ARTICLE")) {
            throw new UnsupportedOperationException("parseArticleNode called with wrong node (" + articleNode.getTagName() + ")");            
        }

        final List<Element> dateNodes =  getChildElements(articleNode, "DATE");
        Optional<TemporalAccessor> date = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateNode(dateNodes.get(0));
            date = Optional.of(dt);             
        } else if (dateNodes.size() > 1) {
            throw new UnsupportedOperationException("Wrong number of DATE nodes (" + dateNodes.size() + ")");
        }
        
        final List<LinkData> links = new ArrayList<LinkData>();
        for (final Element linkNode: getChildElements(articleNode, "X")) {
            links.add(XmlParser.parseXNode(linkNode));
        }

        final List<AuthorData> authors = new ArrayList<AuthorData>();
        for (final Element authorNode: getChildElements(articleNode, "AUTHOR")) {
            authors.add(XmlParser.parseAuthorNode(authorNode));
        }

        return new ArticleData(date, authors, links);
    }

    public static LinkData parseXNode(final Element xNode) {
        
        if (!xNode.getTagName().equals("X")) {
            throw new UnsupportedOperationException("parseXNode called with wrong node (" + xNode.getTagName() + ")");            
        }
        
        final NodeList titleNodes = xNode.getElementsByTagName("T");
        if (titleNodes.getLength() != 1) {
            throw new UnsupportedOperationException("Wrong number of T nodes (" + titleNodes.getLength() + ") in \"" + xNode.getTextContent() + "\"");
        }
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = xNode.getElementsByTagName("ST");
        final String subtitles[] = new String[subtitleNodes.getLength()];
        for (int k = 0; k < subtitleNodes.getLength(); k++) {
            subtitles[k] = ((Element)subtitleNodes.item(k)).getTextContent();
        }

        final List<Element> urlNodes = getChildElements(xNode, "A");
        if (urlNodes.size() != 1) {
            throw new UnsupportedOperationException("Wrong number of A nodes (" + urlNodes.size() + ") in \"" + title + "\"");
        }
        final String url = urlNodes.get(0).getTextContent();

        final NodeList languageNodes = xNode.getElementsByTagName("L");
        if (languageNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of L nodes (" + languageNodes.getLength() + ") in \"" + title + "\"");
        }
        final String languages[] = new String[languageNodes.getLength()];
        for (int k = 0; k < languageNodes.getLength(); k++) {
            languages[k] = ((Element)languageNodes.item(k)).getTextContent();
        }

        final NodeList formatNodes = xNode.getElementsByTagName("F");
        if (formatNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of F nodes (" + formatNodes.getLength() + ") in \"" + title + "\"");
        }
        final String formats[] = new String[formatNodes.getLength()];
        for (int k = 0; k < formatNodes.getLength(); k++) {
            formats[k] = ((Element)formatNodes.item(k)).getTextContent();
        }
        
        final NodeList durationNodes =  xNode.getElementsByTagName("DURATION");
        Optional<Duration> duration = Optional.empty();
        if (durationNodes.getLength() == 1) {
            duration = Optional.of(parseDurationNode((Element)durationNodes.item(0)));
        }  else if (durationNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of DURATION nodes (" + durationNodes.getLength() + ") in \"" + title + "\"");
        }

        final List<Element> dateNodes =  getChildElements(xNode, "DATE");
        Optional<TemporalAccessor> publicationDate = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateNode(dateNodes.get(0));
            publicationDate = Optional.of(dt);             
        } else if (dateNodes.size() > 1) {
            throw new UnsupportedOperationException("Wrong number of DATE nodes (" + dateNodes.size() + ") in \"" + title + "\"");
        }
        
        final Attr statusAttribute = xNode.getAttributeNode("status");
        final Optional<String> status = (statusAttribute != null) ? Optional.of(statusAttribute.getValue())
                                                                  : Optional.empty();

        final Attr protectionAttribute = xNode.getAttributeNode("protection");
        final Optional<String> protection = (protectionAttribute != null) ? Optional.of(protectionAttribute.getValue())
                                                                          : Optional.empty();

        return new LinkData(title, subtitles, url, status, protection, formats, languages, duration, publicationDate);
    }
    
    public static AuthorData parseAuthorNode(final Element authorNode) {
        
        if (!authorNode.getTagName().equals("AUTHOR")) {
            throw new UnsupportedOperationException("parseAuthorNode called with wrong node (" + authorNode.getTagName() + ")");            
        }
        
        Optional<String> namePrefix = Optional.empty();
        if (authorNode.getElementsByTagName("NAMEPREFIX").getLength() == 1) {
            namePrefix = Optional.of(authorNode.getElementsByTagName("NAMEPREFIX").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("NAMEPREFIX").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMEPREFIX nodes (" + authorNode.getElementsByTagName("NAMEPREFIX").getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> firstName = Optional.empty();
        if (authorNode.getElementsByTagName("FIRSTNAME").getLength() == 1) {
            firstName = Optional.of(authorNode.getElementsByTagName("FIRSTNAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("FIRSTNAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of FIRSTNAME nodes (" + authorNode.getElementsByTagName("FIRSTNAME").getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> middleName = Optional.empty();
        if (authorNode.getElementsByTagName("MIDDLENAME").getLength() == 1) {
            middleName = Optional.of(authorNode.getElementsByTagName("MIDDLENAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("MIDDLENAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MIDDLENAME nodes (" + authorNode.getElementsByTagName("MIDDLENAME").getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> lastName = Optional.empty();
        if (authorNode.getElementsByTagName("LASTNAME").getLength() == 1) {
            lastName = Optional.of(authorNode.getElementsByTagName("LASTNAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("LASTNAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of LASTNAME nodes (" + authorNode.getElementsByTagName("LASTNAME").getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> nameSuffix = Optional.empty();
        if (authorNode.getElementsByTagName("NAMESUFFIX").getLength() == 1) {
            nameSuffix = Optional.of(authorNode.getElementsByTagName("NAMESUFFIX").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("NAMESUFFIX").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMESUFFIX nodes (" + authorNode.getElementsByTagName("NAMESUFFIX").getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> givenName = Optional.empty();
        if (authorNode.getElementsByTagName("GIVENNAME").getLength() == 1) {
            givenName = Optional.of(authorNode.getElementsByTagName("GIVENNAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("GIVENNAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of GIVENNAME nodes( (" + authorNode.getElementsByTagName("GIVENNAME").getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        return new AuthorData(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
    }

    public static TemporalAccessor parseDateNode(final Element dateNode) {

        if (!dateNode.getTagName().equals("DATE")) {
            throw new UnsupportedOperationException("parseDateNode called with wrong node (" + dateNode.getTagName() + ")");            
        }
        
        final NodeList yearNodes = dateNode.getElementsByTagName("YEAR");
        if (yearNodes.getLength() == 1) {
            final int year = Integer.parseInt(yearNodes.item(0).getTextContent());
            final NodeList monthNodes = dateNode.getElementsByTagName("MONTH");
            if (monthNodes.getLength() == 1) {
                final int month = Integer.parseInt(monthNodes.item(0).getTextContent());
                final NodeList dayNodes = dateNode.getElementsByTagName("DAY");
                if (dayNodes.getLength() == 1) {
                    final int day = Integer.parseInt(dayNodes.item(0).getTextContent());
                    return LocalDate.of(year, month, day);
                } else if (dayNodes.getLength() > 1) {
                    throw new UnsupportedOperationException("Wrong number of DAY nodes (" + dayNodes.getLength() + ") in string \"" + dateNode.getTextContent() + "\"");
                }
                return YearMonth.of(year, month);
            } else if (monthNodes.getLength() > 1) {
                throw new UnsupportedOperationException("Wrong number of MONTH nodes (" + monthNodes.getLength() + ") in string \"" + dateNode.getTextContent() + "\"");
            }
            return Year.of(year);
        }
        throw new UnsupportedOperationException("Wrong number of YEAR nodes (" + yearNodes.getLength() + ") in string \"" + dateNode.getTextContent() + "\"");
    }

    public static Duration parseDurationNode(final Element durationNode) {

        if (!durationNode.getTagName().equals("DURATION")) {
            throw new UnsupportedOperationException("parseDurationNode called with wrong node (" + durationNode.getTagName() + ")");            
        }

        Duration duration;

        final NodeList secondsNodes = durationNode.getElementsByTagName("SECOND");
        if (secondsNodes.getLength() == 1) {
        	final long seconds = Long.parseLong(secondsNodes.item(0).getTextContent());
        	duration = Duration.ofSeconds(seconds);
        } else {
            throw new UnsupportedOperationException("Wrong number of SECOND nodes (" + secondsNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }

        final NodeList minutesNodes = durationNode.getElementsByTagName("MINUTE");
        if (minutesNodes.getLength() == 1) {
            final long minutes = Long.parseLong(minutesNodes.item(0).getTextContent());
			duration = duration.plusMinutes(minutes);
        } else if (minutesNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MINUTE nodes (" + minutesNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }
        
        final NodeList hoursNodes = durationNode.getElementsByTagName("HOUR");
        if (hoursNodes.getLength() == 1) {
            final long  hours = Long.parseLong(hoursNodes.item(0).getTextContent());
			duration = duration.plusHours(hours);
        } else if (hoursNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of HOUR nodes (" + hoursNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }
        
        return duration;
    }

    private static List<Element> getChildElements(final Element element,
                                                  final String tag) {
        
        final List<Element> list = new ArrayList<Element>();
        
        final NodeList children =  element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {            
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final Element child = (Element)children.item(i);
                if (child.getTagName().equals(tag)) {
                    list.add(child);
                }
            }
        }
        
        return list;
    }
}
