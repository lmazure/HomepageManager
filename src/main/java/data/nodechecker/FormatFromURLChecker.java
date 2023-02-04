package data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.nodechecker.tagselection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

/**
*
*/
public class FormatFromURLChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.X
            });

    /**
    * constructor
    */
    public FormatFromURLChecker() {
        super(s_selector,
              FormatFromURLChecker::checkFormat, "given the URL, the format is incorrect");
    }

    private static CheckStatus checkFormat(final Element e) {

        String url = "";
        String format = "";

        final NodeList children = e.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            final Node child = children.item(j);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (XmlHelper.isOfType(child, ElementType.A)) {
                    url = child.getTextContent();
                }
                if (XmlHelper.isOfType(child, ElementType.F)) {
                    format = child.getTextContent();
                }
            }
        }

        if (url.toUpperCase().endsWith(".PDF") && !format.equals("PDF"))
           return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being PDF format", Optional.empty());

        if (url.toUpperCase().endsWith(".PS") && !format.equals("PostScript"))
            return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being PostScript format", Optional.empty());

        if (url.toUpperCase().endsWith(".WMV") && !format.equals("Windows Media Player"))
               return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being Windows Media Player format", Optional.empty());

        if (url.startsWith("https://www.youtube.com/watch?v=") && !format.equals("MP4"))
               return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being MP4 format", Optional.empty());

        if (url.startsWith("http://video.google.com/videoplay") && !format.equals("Flash Video"))
               return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being Flash Video format", Optional.empty());

        if ((url.startsWith("https://medium.com/") ||
             url.startsWith("https://www.ibm.com/")) && !(format.equals("HTML") || format.equals("MP3"))) // a HTML page may contain a MP3
            return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being HTML format", Optional.empty());

        if ((url.startsWith("https://www.numberphile.com/podcast/") ||
             url.startsWith("https://play.acast.com") ||
             url.startsWith("https://podcastaddict.com")) && !format.equals("MP3"))
            return new CheckStatus("ImproperFormat", "\"" + url + "\" is not indicated as being MP3 format", Optional.empty());

        return null;
    }
}
