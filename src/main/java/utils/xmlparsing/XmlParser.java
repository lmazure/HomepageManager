package utils.xmlparsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser {
    
    public static LinkData parseXNode(final Element xNode) {
        
        final NodeList titleNodes = xNode.getElementsByTagName("T");
        if (titleNodes.getLength() != 1) {
            throw new UnsupportedOperationException("Wrong number of T nodes");
        }
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = xNode.getElementsByTagName("ST");
        final String subtitles[] = new String[subtitleNodes.getLength()];
        for (int k = 0; k < subtitleNodes.getLength(); k++) {
            subtitles[k] = ((Element)subtitleNodes.item(k)).getTextContent();
        }

        final NodeList urlNodes = xNode.getElementsByTagName("A");
        if (urlNodes.getLength() != 1) {
            throw new UnsupportedOperationException("Wrong number of A nodes");
        }
        final String url = ((Element)urlNodes.item(0)).getTextContent();

        final NodeList languageNodes = xNode.getElementsByTagName("L");
        if (languageNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of L nodes");
        }
        final String languages[] = new String[languageNodes.getLength()];
        for (int k = 0; k < languageNodes.getLength(); k++) {
            languages[k] = ((Element)languageNodes.item(k)).getTextContent();
        }

        final NodeList formatNodes = xNode.getElementsByTagName("F");
        if (formatNodes.getLength() == 0) {
            throw new UnsupportedOperationException("Wrong number of F nodes");
        }
        final String formats[] = new String[formatNodes.getLength()];
        for (int k = 0; k < formatNodes.getLength(); k++) {
            formats[k] = ((Element)formatNodes.item(k)).getTextContent();
        }
        
        final NodeList durationNodes =  xNode.getElementsByTagName("DURATION");
        Optional<DurationData> duration = Optional.empty();
        if (durationNodes.getLength() == 1) {
            duration = Optional.of(parseDurationNode((Element)durationNodes.item(0)));
        }  else if (durationNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of DURATION nodes");
        }
        
        final Attr statusAttribute = xNode.getAttributeNode("status");
        final Optional<String> status = (statusAttribute != null) ? Optional.of(statusAttribute.getValue())
                                                                  : Optional.empty();

        final Attr protectionAttribute = xNode.getAttributeNode("protection");
        final Optional<String> protection = (protectionAttribute != null) ? Optional.of(protectionAttribute.getValue())
                                                                          : Optional.empty();

        return new LinkData(title, subtitles, url, status, protection, formats, languages, duration);
    }
    
    public static AuthorData parseAuthorNode(final Element authorNode) {
        
        Optional<String> namePrefix = Optional.empty();
        if (authorNode.getElementsByTagName("NAMEPREFIX").getLength() == 1) {
            namePrefix = Optional.of(authorNode.getElementsByTagName("NAMEPREFIX").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("NAMEPREFIX").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMEPREFIX nodes");
        }

        Optional<String> firstName = Optional.empty();
        if (authorNode.getElementsByTagName("FIRSTNAME").getLength() == 1) {
            firstName = Optional.of(authorNode.getElementsByTagName("FIRSTNAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("FIRSTNAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of FIRSTNAME nodes");
        }

        Optional<String> middleName = Optional.empty();
        if (authorNode.getElementsByTagName("MIDDLENAME").getLength() == 1) {
            middleName = Optional.of(authorNode.getElementsByTagName("MIDDLENAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("MIDDLENAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MIDDLENAME nodes");
        }

        Optional<String> lastName = Optional.empty();
        if (authorNode.getElementsByTagName("LASTNAME").getLength() == 1) {
            lastName = Optional.of(authorNode.getElementsByTagName("LASTNAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("LASTNAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of LASTNAME nodes");
        }

        Optional<String> nameSuffix = Optional.empty();
        if (authorNode.getElementsByTagName("NAMESUFFIX").getLength() == 1) {
            nameSuffix = Optional.of(authorNode.getElementsByTagName("NAMESUFFIX").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("NAMESUFFIX").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of NAMESUFFIX nodes");
        }

        Optional<String> givenName = Optional.empty();
        if (authorNode.getElementsByTagName("GIVENNAME").getLength() == 1) {
            givenName = Optional.of(authorNode.getElementsByTagName("GIVENNAME").item(0).getTextContent());
        } else if (authorNode.getElementsByTagName("GIVENNAME").getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of GIVENNAME nodes");
        }

        return new AuthorData(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
    }

    public static DateData parseDateNode(final Element dateNode) {

        Integer year = 0;
        final NodeList yearNodes = dateNode.getElementsByTagName("YEAR");
        if (yearNodes.getLength() == 1) {
            year = Integer.parseInt(yearNodes.item(0).getTextContent());
        } else {
            throw new UnsupportedOperationException("Wrong number of YEAR nodes");
        }

        Optional<Integer> month = Optional.empty();
        final NodeList monthNodes = dateNode.getElementsByTagName("MONTH");
        if (monthNodes.getLength() == 1) {
            month = Optional.of(Integer.parseInt(monthNodes.item(0).getTextContent()));
        } else if (monthNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MONTH nodes");
        }
        
        Optional<Integer> day = Optional.empty();
        final NodeList dayNodes = dateNode.getElementsByTagName("DAY");
        if (dayNodes.getLength() == 1) {
            day = Optional.of(Integer.parseInt(dayNodes.item(0).getTextContent()));
        } else if (dayNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of DAY nodes");
        }

        return new DateData(year, month, day);
    }

    public static DurationData parseDurationNode(final Element durationNode) {

        Integer seconds = 0;
        final NodeList secondsNodes = durationNode.getElementsByTagName("SECOND");
        if (secondsNodes.getLength() == 1) {
            seconds = Integer.parseInt(secondsNodes.item(0).getTextContent());
        } else {
            throw new UnsupportedOperationException("Wrong number of SECOND nodes");
        }

        Optional<Integer> minutes = Optional.empty();
        final NodeList minutesNodes = durationNode.getElementsByTagName("MINUTE");
        if (minutesNodes.getLength() == 1) {
            minutes = Optional.of(Integer.parseInt(minutesNodes.item(0).getTextContent()));
        } else if (minutesNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MINUTE nodes");
        }
        
        Optional<Integer> hours = Optional.empty();
        final NodeList hoursNodes = durationNode.getElementsByTagName("HOUR");
        if (hoursNodes.getLength() == 1) {
            hours = Optional.of(Integer.parseInt(hoursNodes.item(0).getTextContent()));
        } else if (hoursNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of HOUR nodes");
        }

        return new DurationData(seconds, minutes, hours);
    }

    public static ArticleData parseArticleNode(final Element articleNode) {
        
        final NodeList dateNodes =  articleNode.getElementsByTagName("DATE");
        Optional<DateData> dateData = Optional.empty();
        if (dateNodes.getLength() == 1) {
            final DateData dt = XmlParser.parseDateNode((Element)dateNodes.item(0));
            dateData = Optional.of(dt);             
        }
        
        final NodeList linkNodes =  articleNode.getChildNodes();
        final List<LinkData> links = new ArrayList<LinkData>();
        for (int i = 0; i < linkNodes.getLength(); i++) {            
            if (linkNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            final Element linkNode = (Element)linkNodes.item(i);
            if (linkNode.getTagName().compareTo("X") != 0) continue;
            links.add(XmlParser.parseXNode(linkNode));
        }

        final NodeList authorNodes =  articleNode.getElementsByTagName("AUTHOR");
        final List<AuthorData> authors = new ArrayList<AuthorData>(authorNodes.getLength());
        for (int i = 0; i < authorNodes.getLength(); i++) {
            authors.add(XmlParser.parseAuthorNode((Element)authorNodes.item(i)));
        }

        return new ArticleData(dateData, authors, links);
    }
}
