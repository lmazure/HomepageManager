package utils.xmlparsing;

import java.util.Optional;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlParser {
    
    public static LinkData parseXNode(final Element linkNode) {
        
        final NodeList titleNodes = linkNode.getElementsByTagName("T");
        final String title = ((Element)titleNodes.item(0)).getTextContent();

        final NodeList subtitleNodes = linkNode.getElementsByTagName("ST");
        final Optional<String> subtitle = (subtitleNodes.getLength() == 1) ? Optional.of(((Element)subtitleNodes.item(0)).getTextContent())
                                                                           : Optional.empty();

        final NodeList urlNodes = linkNode.getElementsByTagName("A");
        final String url = ((Element)urlNodes.item(0)).getTextContent();

        final NodeList languageNodes = linkNode.getElementsByTagName("L");
        final String languages[] = new String[languageNodes.getLength()];
        for (int k = 0; k < languageNodes.getLength(); k++) {
            languages[k] = ((Element)languageNodes.item(k)).getTextContent();
        }

        final NodeList formatNodes = linkNode.getElementsByTagName("F");
        final String formats[] = new String[formatNodes.getLength()];
        for (int k = 0; k < formatNodes.getLength(); k++ ) {
            formats[k] = ((Element)formatNodes.item(k)).getTextContent();
        }
        
        final NodeList durationNodes =  linkNode.getElementsByTagName("DURATION");
        
        Optional<DurationData> duration = Optional.empty();
        if ( durationNodes.getLength()==1 ) {
            final Element durationNode = (Element)durationNodes.item(0);
            
            Optional<Integer> durationHour = Optional.empty();
            final NodeList durationHourNodes = durationNode.getElementsByTagName("HOUR");
            if (durationHourNodes.getLength() == 1) {
                durationHour = Optional.of(Integer.parseInt(durationHourNodes.item(0).getTextContent()));
            }

            Optional<Integer> durationMinute = Optional.empty();
            final NodeList durationMinuteNodes = durationNode.getElementsByTagName("MINUTE");
            if (durationMinuteNodes.getLength() == 1) {
                durationMinute = Optional.of(Integer.parseInt(durationMinuteNodes.item(0).getTextContent()));
            }

            Integer durationSecond = 0;
            final NodeList durationSecondNodes = durationNode.getElementsByTagName("SECOND");
            if (durationSecondNodes.getLength() == 1) {
                durationSecond = Integer.parseInt(durationSecondNodes.item(0).getTextContent());
            }
            duration = Optional.of(new DurationData(durationSecond, durationMinute, durationHour));
        }
        
        final Attr statusAttribute = linkNode.getAttributeNode("status");
        final Optional<String> status = (statusAttribute != null) ? Optional.of(statusAttribute.getValue())
                                                                  : Optional.empty();

        final Attr protectionAttribute = linkNode.getAttributeNode("protection");
        final Optional<String> protection = (protectionAttribute != null) ? Optional.of(protectionAttribute.getValue())
                                                                          : Optional.empty();

        return new LinkData(title, subtitle, url, status, protection, formats, languages, duration);
    }
    
    public static AuthorData parseAuthorNode(final Element authorNode) {
        
        Optional<String> namePrefix = Optional.empty();
        if ( authorNode.getElementsByTagName("NAMEPREFIX").getLength() == 1 ) {
            namePrefix = Optional.of(authorNode.getElementsByTagName("NAMEPREFIX").item(0).getTextContent());
        } else if ( authorNode.getElementsByTagName("NAMEPREFIX").getLength() != 0 ) {
            throw new UnsupportedOperationException("Wrong number of NAMEPREFIX nodes");
        }

        Optional<String> firstName = Optional.empty();
        if ( authorNode.getElementsByTagName("FIRSTNAME").getLength() == 1 ) {
            firstName = Optional.of(authorNode.getElementsByTagName("FIRSTNAME").item(0).getTextContent());
        } else if ( authorNode.getElementsByTagName("FIRSTNAME").getLength() != 0 ) {
            throw new UnsupportedOperationException("Wrong number of FIRSTNAME nodes");
        }

        Optional<String> middleName = Optional.empty();
        if ( authorNode.getElementsByTagName("MIDDLENAME").getLength() == 1 ) {
            middleName = Optional.of(authorNode.getElementsByTagName("MIDDLENAME").item(0).getTextContent());
        } else if ( authorNode.getElementsByTagName("MIDDLENAME").getLength() != 0 ) {
            throw new UnsupportedOperationException("Wrong number of MIDDLENAME nodes");
        }

        Optional<String> lastName = Optional.empty();
        if ( authorNode.getElementsByTagName("LASTNAME").getLength() == 1 ) {
            lastName = Optional.of(authorNode.getElementsByTagName("LASTNAME").item(0).getTextContent());
        } else if ( authorNode.getElementsByTagName("LASTNAME").getLength() != 0 ) {
            throw new UnsupportedOperationException("Wrong number of LASTNAME nodes");
        }

        Optional<String> nameSuffix = Optional.empty();
        if ( authorNode.getElementsByTagName("NAMESUFFIX").getLength() == 1 ) {
            nameSuffix = Optional.of(authorNode.getElementsByTagName("NAMESUFFIX").item(0).getTextContent());
        } else if ( authorNode.getElementsByTagName("NAMESUFFIX").getLength() != 0 ) {
            throw new UnsupportedOperationException("Wrong number of NAMESUFFIX nodes");
        }

        Optional<String> givenName = Optional.empty();
        if ( authorNode.getElementsByTagName("GIVENNAME").getLength() == 1 ) {
            givenName = Optional.of(authorNode.getElementsByTagName("GIVENNAME").item(0).getTextContent());
        } else if ( authorNode.getElementsByTagName("GIVENNAME").getLength() != 0 ) {
            throw new UnsupportedOperationException("Wrong number of GIVENNAME nodes");
        }

        return new AuthorData(namePrefix, firstName, middleName, lastName, nameSuffix, givenName);
    }

    public static DateData parseDateNode(final Element dateNode) {

        Integer dateYear = 0;
        final NodeList dateYearNodes = dateNode.getElementsByTagName("YEAR");
        if (dateYearNodes.getLength() == 1) {
            dateYear = Integer.parseInt(dateYearNodes.item(0).getTextContent());
        } else {
            throw new UnsupportedOperationException("Wrong number of YEAR nodes");
        }

        Optional<Integer> dateMonth = Optional.empty();
        final NodeList dateMonthNodes = dateNode.getElementsByTagName("MONTH");
        if (dateMonthNodes.getLength() == 1) {
            dateMonth = Optional.of(Integer.parseInt(dateMonthNodes.item(0).getTextContent()));
        } else if (dateMonthNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of MONTH nodes");
        }
        
        Optional<Integer> dateDay = Optional.empty();
        final NodeList dateDayNodes = dateNode.getElementsByTagName("DAY");
        if (dateDayNodes.getLength() == 1) {
            dateDay = Optional.of(Integer.parseInt(dateDayNodes.item(0).getTextContent()));
        } else if (dateDayNodes.getLength() > 1) {
            throw new UnsupportedOperationException("Wrong number of DAY nodes");
        }

        return new DateData(dateYear, dateMonth, dateDay);
    }
}
