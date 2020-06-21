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
        
        if (!articleNode.getTagName().equals(NodeType.ARTICLE.toString())) {
            throw new UnsupportedOperationException("parseArticleNode called with wrong node (" + articleNode.getTagName() + ")");            
        }

        final List<Element> dateNodes =  getChildElements(articleNode, NodeType.DATE.toString());
        Optional<TemporalAccessor> date = Optional.empty();
        if (dateNodes.size() == 1) {
            final TemporalAccessor dt = XmlParser.parseDateNode(dateNodes.get(0));
            date = Optional.of(dt);             
        } else if (dateNodes.size() > 1) {
            throw new UnsupportedOperationException("Wrong number of DATE nodes (" + dateNodes.size() + ")");
        }
        
        final List<LinkData> links = new ArrayList<LinkData>();
        for (final Element linkNode: getChildElements(articleNode, NodeType.X.toString())) {
            links.add(XmlParser.parseXNode(linkNode));
        }

        final List<AuthorData> authors = new ArrayList<AuthorData>();
        for (final Element authorNode: getChildElements(articleNode, NodeType.AUTHOR.toString())) {
            authors.add(XmlParser.parseAuthorNode(authorNode));
        }

        return new ArticleData(date, authors, links);
    }

    public static LinkData parseXNode(final Element xNode) {
        
        if (!xNode.getTagName().equals(NodeType.X.toString())) {
            throw new UnsupportedOperationException("parseXNode called with wrong node (" + xNode.getTagName() + ")");            
        }
        
        final NodeList titleNodes = xNode.getElementsByTagName(NodeType.T.toString());
        if (titleNodes.getLength() != 1) {
            throw new UnsupportedOperationException("Wrong number of T nodes (" + titleNodes.getLength() + ") in \"" + xNode.getTextContent() + "\"");
        }
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = xNode.getElementsByTagName(NodeType.ST.toString());
        final String subtitles[] = new String[subtitleNodes.getLength()];
        for (int k = 0; k < subtitleNodes.getLength(); k++) {
            subtitles[k] = ((Element)subtitleNodes.item(k)).getTextContent();
        }

        final List<Element> urlNodes = getChildElements(xNode, NodeType.A.toString());
        if (urlNodes.size() != 1) {
            throw new UnsupportedOperationException("Wrong number of A nodes (" + urlNodes.size() + ") in \"" + title + "\"");
        }
        final String url = urlNodes.get(0).getTextContent();

        final NodeList languageNodes = xNode.getElementsByTagName(NodeType.L.toString());
        if (languageNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of L nodes (" + languageNodes.getLength() + ") in \"" + title + "\"");
        }
        final String languages[] = new String[languageNodes.getLength()];
        for (int k = 0; k < languageNodes.getLength(); k++) {
            languages[k] = ((Element)languageNodes.item(k)).getTextContent();
        }

        final NodeList formatNodes = xNode.getElementsByTagName(NodeType.F.toString());
        if (formatNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of F nodes (" + formatNodes.getLength() + ") in \"" + title + "\"");
        }
        final String formats[] = new String[formatNodes.getLength()];
        for (int k = 0; k < formatNodes.getLength(); k++) {
            formats[k] = ((Element)formatNodes.item(k)).getTextContent();
        }
        
        final NodeList durationNodes =  xNode.getElementsByTagName(NodeType.DURATION.toString());
        Optional<Duration> duration = Optional.empty();
        if (durationNodes.getLength() == 1) {
            duration = Optional.of(parseDurationNode((Element)durationNodes.item(0)));
        }  else if (durationNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of DURATION nodes (" + durationNodes.getLength() + ") in \"" + title + "\"");
        }

        final List<Element> dateNodes =  getChildElements(xNode, NodeType.DATE.toString());
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
        
        if (!authorNode.getTagName().equals(NodeType.AUTHOR.toString())) {
            throw new UnsupportedOperationException("parseAuthorNode called with wrong node (" + authorNode.getTagName() + ")");            
        }
        
        Optional<String> namePrefix = Optional.empty();
        if (authorNode.getElementsByTagName(NodeType.NAMEPREFIX.toString()).getLength() == 1) {
            namePrefix = Optional.of(authorNode.getElementsByTagName(NodeType.NAMEPREFIX.toString()).item(0).getTextContent());
        } else if (authorNode.getElementsByTagName(NodeType.NAMEPREFIX.toString()).getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMEPREFIX nodes (" + authorNode.getElementsByTagName(NodeType.NAMEPREFIX.toString()).getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> firstName = Optional.empty();
        if (authorNode.getElementsByTagName(NodeType.FIRSTNAME.toString()).getLength() == 1) {
            firstName = Optional.of(authorNode.getElementsByTagName(NodeType.FIRSTNAME.toString()).item(0).getTextContent());
        } else if (authorNode.getElementsByTagName(NodeType.FIRSTNAME.toString()).getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of FIRSTNAME nodes (" + authorNode.getElementsByTagName(NodeType.FIRSTNAME.toString()).getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> middleName = Optional.empty();
        if (authorNode.getElementsByTagName(NodeType.MIDDLENAME.toString()).getLength() == 1) {
            middleName = Optional.of(authorNode.getElementsByTagName(NodeType.MIDDLENAME.toString()).item(0).getTextContent());
        } else if (authorNode.getElementsByTagName(NodeType.MIDDLENAME.toString()).getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MIDDLENAME nodes (" + authorNode.getElementsByTagName(NodeType.MIDDLENAME.toString()).getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> lastName = Optional.empty();
        if (authorNode.getElementsByTagName(NodeType.LASTNAME.toString()).getLength() == 1) {
            lastName = Optional.of(authorNode.getElementsByTagName(NodeType.LASTNAME.toString()).item(0).getTextContent());
        } else if (authorNode.getElementsByTagName(NodeType.LASTNAME.toString()).getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of LASTNAME nodes (" + authorNode.getElementsByTagName(NodeType.LASTNAME.toString()).getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> nameSuffix = Optional.empty();
        if (authorNode.getElementsByTagName(NodeType.NAMESUFFIX.toString()).getLength() == 1) {
            nameSuffix = Optional.of(authorNode.getElementsByTagName(NodeType.NAMESUFFIX.toString()).item(0).getTextContent());
        } else if (authorNode.getElementsByTagName(NodeType.NAMESUFFIX.toString()).getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMESUFFIX nodes (" + authorNode.getElementsByTagName(NodeType.NAMESUFFIX.toString()).getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        Optional<String> givenName = Optional.empty();
        if (authorNode.getElementsByTagName(NodeType.GIVENNAME.toString()).getLength() == 1) {
            givenName = Optional.of(authorNode.getElementsByTagName(NodeType.GIVENNAME.toString()).item(0).getTextContent());
        } else if (authorNode.getElementsByTagName(NodeType.GIVENNAME.toString()).getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of GIVENNAME nodes( (" + authorNode.getElementsByTagName(NodeType.GIVENNAME.toString()).getLength() + ") in string \"" + authorNode.getTextContent() + "\"");
        }

        return new AuthorData(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
    }

    public static TemporalAccessor parseDateNode(final Element dateNode) {

        if (!dateNode.getTagName().equals(NodeType.DATE.toString())) {
            throw new UnsupportedOperationException("parseDateNode called with wrong node (" + dateNode.getTagName() + ")");            
        }
        
        final NodeList yearNodes = dateNode.getElementsByTagName(NodeType.YEAR.toString());
        if (yearNodes.getLength() == 1) {
            final int year = Integer.parseInt(yearNodes.item(0).getTextContent());
            final NodeList monthNodes = dateNode.getElementsByTagName(NodeType.MONTH.toString());
            if (monthNodes.getLength() == 1) {
                final int month = Integer.parseInt(monthNodes.item(0).getTextContent());
                final NodeList dayNodes = dateNode.getElementsByTagName(NodeType.DAY.toString());
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

        if (!durationNode.getTagName().equals(NodeType.DURATION.toString())) {
            throw new UnsupportedOperationException("parseDurationNode called with wrong node (" + durationNode.getTagName() + ")");            
        }

        Duration duration;

        final NodeList secondsNodes = durationNode.getElementsByTagName(NodeType.SECOND.toString());
        if (secondsNodes.getLength() == 1) {
        	final long seconds = Long.parseLong(secondsNodes.item(0).getTextContent());
        	duration = Duration.ofSeconds(seconds);
        } else {
            throw new UnsupportedOperationException("Wrong number of SECOND nodes (" + secondsNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }

        final NodeList minutesNodes = durationNode.getElementsByTagName(NodeType.MINUTE.toString());
        if (minutesNodes.getLength() == 1) {
            final long minutes = Long.parseLong(minutesNodes.item(0).getTextContent());
			duration = duration.plusMinutes(minutes);
        } else if (minutesNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MINUTE nodes (" + minutesNodes.getLength() + ") in string \"" + durationNode.getTextContent() + "\"");
        }
        
        final NodeList hoursNodes = durationNode.getElementsByTagName(NodeType.HOUR.toString());
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
