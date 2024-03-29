package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlHelper;

/**
 *
 */
public class DurationPresenceChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.X
            });

    /**
    * constructor
    */
    public DurationPresenceChecker() {
        super(s_selector,
              DurationPresenceChecker::checkDuration, "given the format, the duration is missing");
    }
    private static CheckStatus checkDuration(final Element e) { //TODO the presence of a duration should be attached to each value of the enul

        boolean hasDuration = false;
        String format = "";

        final NodeList children = e.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            final Node child = children.item(j);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (XmlHelper.isOfType(child, ElementType.DURATION)) {
                    hasDuration = true;
                }
                if (XmlHelper.isOfType(child, ElementType.F)) {
                    format = child.getTextContent();
                }
            }
        }

        if (format.equals("HTML")) {
            return null;
        }

        if (format.equals("PDF")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "PDF cannot have duration", Optional.empty());
            }
            return null;
        }
        if (format.equals("Flash Video")) {
            if (!hasDuration) {
                return new CheckStatus("MissingDuration", "missing duration for Flash Video", Optional.empty());
            }
            return null;
        }
        if (format.equals("Word")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "Word cannot have duration", Optional.empty());
            }
            return null;
        }
        if (format.equals("PostScript")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "PostScript cannot have duration", Optional.empty());
            }
            return null;
        }
        if (format.equals("Flash")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "Flash cannot have duration", Optional.empty());
            }
            return null;
        }
        if (format.equals("PowerPoint")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "PowerPoint cannot have duration", Optional.empty());
            }
            return null;
        }
        if (format.equals("ASCII")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "ASCII cannot have duration", Optional.empty());
            }
            return null;
        }
        if (format.equals("MP3")) {
            if (!hasDuration) {
                return new CheckStatus("MissingDuration", "missing duration for MP3", Optional.empty());
            }
            return null;
        }
        if (format.equals("MP4")) {
            if (!hasDuration) {
                return new CheckStatus("MissingDuration", "missing duration for MP4", Optional.empty());
            }
            return null;
        }
        if (format.equals("RealMedia")) {
            if (!hasDuration) {
                return new CheckStatus("MissingDuration", "missing duration for RealMedia", Optional.empty());
            }
            return null;
        }
        if (format.equals("Windows Media Player")) {
            if (!hasDuration) {
                return new CheckStatus("MissingDuration", "missing duration for Windows Media Player", Optional.empty());
            }
            return null;
        }
        if (format.equals("txt")) {
            if (hasDuration) {
                return new CheckStatus("SpuriousDuration", "txt cannot have duration", Optional.empty());
            }
            return null;
        }

        return new CheckStatus("UnkwonFormat", "\"" + format + "\" is an unknown format to get duration presence", Optional.empty());
    }
}
