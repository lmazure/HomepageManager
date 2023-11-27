package fr.mazure.homepagemanager.data.nodechecker;

import java.util.Locale;
import java.util.Optional;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParser;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 * Check that the comment of an article is consistent with the characteristics of the article 
 */
public class ArticleCommentChecker  extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    /**
    * constructor
    */
    public ArticleCommentChecker() {
        super(s_selector,
              ArticleCommentChecker::checkCommentReferringSubtitle, "comment refers a non-existant subtitle");
    }

    private static CheckStatus checkCommentReferringSubtitle(final Element e) {

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (final XmlParsingException ex) {
            return new CheckStatus("ArticleParsingError",
                                   "Failed to parse article (" + ex.getMessage() + ")",
                                   Optional.empty());
        }
        if (articleData.comment().toLowerCase(Locale.ENGLISH).contains("subtitle") && (articleData.links().get(0).getSubtitles().length == 0)) {
            return new CheckStatus("CommentReferringMissingSubtitle",
                                   "Comment refers a non-existant subtitle",
                                   Optional.empty());
        }
        return null;
    }
}
