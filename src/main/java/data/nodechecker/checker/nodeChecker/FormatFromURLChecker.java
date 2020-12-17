package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class FormatFromURLChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.X
            });

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

        if (url.toUpperCase().endsWith("PDF") && !format.equals("PDF"))
           return new CheckStatus("\"" + url + "\" is not indicated as being a PDF format");

        if (url.toUpperCase().endsWith("WMV") && !format.equals("Windows Media Player"))
               return new CheckStatus("\"" + url + "\" is not indicated as being a Windows Media Player format");

        if (url.contains("youtube.com/watch") && !format.equals("MP4"))
               return new CheckStatus("\"" + url + "\" is not indicated as being a MP4 format");

        if (url.contains("video.google.com/videoplay") && !format.equals("Flash Video"))
               return new CheckStatus("\"" + url + "\" is not indicated as being a Flash Video format");

        return null;
    }
}
