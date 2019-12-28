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

        Optional<Integer> durationHour = Optional.empty();
        Optional<Integer> durationMinute = Optional.empty();
        Optional<Integer> durationSecond = Optional.empty();
        
        final NodeList durationNodes =  linkNode.getElementsByTagName("DURATION");
        
        if ( durationNodes.getLength()==1 ) {
            final Element durationNode = (Element)durationNodes.item(0);
            
            final NodeList durationHourNodes = durationNode.getElementsByTagName("HOUR");
            if (durationHourNodes.getLength() == 1) {
                durationHour = Optional.of(Integer.parseInt(durationHourNodes.item(0).getTextContent()));
            }
            final NodeList durationMinuteNodes = durationNode.getElementsByTagName("MINUTE");
            if (durationMinuteNodes.getLength() == 1) {
                durationMinute = Optional.of(Integer.parseInt(durationMinuteNodes.item(0).getTextContent()));
            }
            final NodeList durationSecondNodes = durationNode.getElementsByTagName("SECOND");
            if (durationSecondNodes.getLength() == 1) {
                durationSecond = Optional.of(Integer.parseInt(durationSecondNodes.item(0).getTextContent()));
            }
        }
        
        final Attr statusAttribute = linkNode.getAttributeNode("status");
        final Optional<String> status = (statusAttribute != null) ? Optional.of(statusAttribute.getValue())
                                                                  : Optional.empty();

        final Attr protectionAttribute = linkNode.getAttributeNode("protection");
        final Optional<String> protection = (protectionAttribute != null) ? Optional.of(protectionAttribute.getValue())
                                                                          : Optional.empty();

        return new LinkData(title, subtitle, url, status, protection, formats, languages, durationHour, durationMinute, durationSecond);
    }
}
