package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkQuality;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParser;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 * Check that the links of an article have consistent attributes
 */
public class ArticleLinkAttributesChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    /**
    * constructor
    */
    public ArticleLinkAttributesChecker() {
        super(s_selector,
              ArticleLinkAttributesChecker::checkLinkAttibutes, "different attributes for the links of an article");
    }

    private static CheckStatus checkLinkAttibutes(final Element e) {

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (final XmlParsingException ex) {
            return new CheckStatus("ArticleParsingError",
                                   "Failed to parse article (" + ex.getMessage() + ")",
                                   Optional.empty());
        }
        if (articleData.links().size() == 1) {
            // only one link, nothing to check
            return null;
        }

        final boolean firstLinkIsObsolete = (articleData.links().get(0).getStatus() == LinkStatus.OBSOLETE);
        final LinkQuality firstLinkQuality = articleData.links().get(0).getQuality();
        for (int i = 1; i < articleData.links().size(); i++) {
            final boolean linkIsObsolete = (articleData.links().get(i).getStatus() == LinkStatus.OBSOLETE);
            if (linkIsObsolete != firstLinkIsObsolete) {
                return new CheckStatus("ArticleLinkAttributes",
                                       "Links of article \"" +
                                       articleData.links().get(i).getUrl() +
                                       "\" have different attributes - the first one has obsolete=" +
                                       firstLinkIsObsolete +
                                       " while the " +
                                       i +
                                       "-th link has obsolete=" +
                                       linkIsObsolete ,
                                       Optional.empty());
            }
            final LinkQuality linkQuality = articleData.links().get(i).getQuality();
            if (linkQuality != firstLinkQuality) {
                return new CheckStatus("ArticleLinkAttributes",
                                       "Links of article \"" +
                                       articleData.links().get(i).getUrl() +
                                       "\" have different attributes - the first one has quality=" +
                                       firstLinkQuality +
                                       " while the " +
                                       i +
                                       "-th link has quality=" +
                                       linkQuality ,
                                       Optional.empty());
            }
        }

        return null;
    }
}
