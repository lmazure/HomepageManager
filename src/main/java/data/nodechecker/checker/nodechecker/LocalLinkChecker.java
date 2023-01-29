package data.nodechecker.checker.nodechecker;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.InclusionTagSelector;
import utils.FileHelper;
import utils.xmlparsing.ElementType;

/**
* Checks that local links are not dead
*/
public class LocalLinkChecker extends NodeChecker {

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.A
            });

    private static Path _homepagePath; // TODO should not be static!

    /**
    * constructor
    */
    public LocalLinkChecker() {
        super(s_selector,
                LocalLinkChecker::checkLocalLink, "the local link is dead");
    }

    /**
    * @param homepagePath path to the directory containing the pages
    */
    public static void setHomepagePath(final Path homepagePath) { // TODO what the fuck!
        _homepagePath = homepagePath;
    }

    private static CheckStatus checkLocalLink(final Element e) {

        final String url = e.getTextContent();

        if(url.indexOf(':') > 0) {
            // not a local link
            return null;
        }

        // check file presence
        final Path file = _homepagePath.resolve(url.replaceFirst("^\\.\\./", "")
                                                   .replaceFirst("#.*$", "")
                                                   .replaceFirst("\\.html$", ".xml"));
        if (!Files.exists(file)) {
            return new CheckStatus("IncorrectLocalLink", "the file \"" + file + "\" does not exist");
        }

        // check anchor presence
        if (url.indexOf('#') < 0 ) {
            return null;
        }
        final String anchor = url.replaceFirst(".*#", "");
        final String fileContent = FileHelper.slurpFile(file.toFile(), StandardCharsets.UTF_8);
        if (fileContent.indexOf("<ANCHOR>" + anchor + "</ANCHOR>") < 0) {
            return new CheckStatus("IncorrectLocalLink", "the file \"" + file + "\" does not contain the anchor \"" + anchor + "\"");
        }

        return null;
    }
}

