package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ElementType;

public class DurationPresenceChecker extends NodeChecker {

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.X
            });

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
                return new CheckStatus("PDF cannot have duration");
            }
            return null;
        }
        if (format.equals("Flash Video")) {
            if (!hasDuration) {
                return new CheckStatus("missing duration for Flash Video");
            }
            return null;
        }
        if (format.equals("Word")) {
            if (hasDuration) {
                return new CheckStatus("Word cannot have duration");
            }
            return null;
        }
        if (format.equals("PostScript")) {
            if (hasDuration) {
                return new CheckStatus("PostScript cannot have duration");
            }
            return null;
        }
        if (format.equals("Flash")) {
            if (hasDuration) {
                return new CheckStatus("Flash cannot have duration");
            }
            return null;
        }
        if (format.equals("PowerPoint")) {
            if (hasDuration) {
                return new CheckStatus("PowerPoint cannot have duration");
            }
            return null;
        }
        if (format.equals("ASCII")) {
            if (hasDuration) {
                return new CheckStatus("ASCII cannot have duration");
            }
            return null;
        }
        if (format.equals("RSS")) {
            if (hasDuration) {
                return new CheckStatus("RSS cannot have duration");
            }
            return null;
        }
        if (format.equals("RSS2")) {
            if (hasDuration) {
                return new CheckStatus("RSS2 cannot have duration");
            }
            return null;
        }
        if (format.equals("MP3")) {
            if (!hasDuration) {
                return new CheckStatus("missing duration for MP3");
            }
            return null;
        }
        if (format.equals("MP4")) {
            if (!hasDuration) {
                return new CheckStatus("missing duration for MP4");
            }
            return null;
        }
        if (format.equals("RealMedia")) {
            if (!hasDuration) {
                return new CheckStatus("missing duration for RealMedia");
            }
            return null;
        }
        if (format.equals("Windows Media Player")) {
            if (!hasDuration) {
                return new CheckStatus("missing duration for Windows Media Player");
            }
            return null;
        }
        if (format.equals("Atom")) {
            if (hasDuration) {
                return new CheckStatus("Atom cannot have duration");
            }
            return null;
        }
        if (format.equals("txt")) {
            if (hasDuration) {
                return new CheckStatus("txt cannot have duration");
            }
            return null;
        }

        return new CheckStatus("\"" + format + "\" is a unknown format to get duration presence");
    }
}
