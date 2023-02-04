package data.nodechecker;

import java.util.Optional;

import org.w3c.dom.Element;

import data.nodechecker.tagselection.InclusionTagSelector;
import utils.XmlHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlParser;
import utils.xmlparsing.XmlParsingException;

/**
*
*/
public class PredecessorArticleChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    /**
    * constructor
    */
    public PredecessorArticleChecker() {
        super(s_selector,
              PredecessorArticleChecker::checkPredArticle, "the previous article is not the one defined by the 'predecessor' attribute");
    }

    private static CheckStatus checkPredArticle(final Element e) {

        final String predecessor = e.getAttribute("predecessor");
        if (predecessor.isEmpty()) {
            return null;
        }

        if (!XmlHelper.isOfType(e.getParentNode(), ElementType.ITEM)) {
            return new CheckStatus("IncorrectPredecessorArticle",
                                   "Article has 'predecessor' attribute, but it is not in an <ITEM>",
                                   Optional.empty());
        }
        final Element previousSibling = XmlHelper.getPreviousSiblingElement((Element)e.getParentNode());
        if (previousSibling == null) {
            return new CheckStatus("IncorrectPredecessorArticle",
                                   "Article has 'predecessor' attribute, but there is no previous element",
                                   Optional.empty());
        }
        if (!XmlHelper.isOfType(previousSibling, ElementType.ITEM)) {
            return new CheckStatus("IncorrectPredecessorArticle",
                                   "Article has 'predecessor' attribute, but previous element is not an <ITEM>",
                                   Optional.empty());
        }
        if (!XmlHelper.isOfType(previousSibling.getFirstChild(), ElementType.ARTICLE)) {
            return new CheckStatus("IncorrectPredecessorArticle",
                                   "Article has 'predecessor' attribute, but previous <ITEM> does not contain an <ARTICLE>",
                                   Optional.empty());
        }

        final Element previousArticle = (Element)previousSibling.getFirstChild();
        ArticleData previousArticleData;
        try {
            previousArticleData = XmlParser.parseArticleElement(previousArticle);
        } catch (final XmlParsingException ex) {
            return new CheckStatus("ArticleParsingError",
                                   "Failed to parse article (" + ex.getMessage() + ")",
                                   Optional.empty());
        }
        final String urlOfPreviousArticle = previousArticleData.links().get(0).getUrl();

        if (!predecessor.equals(urlOfPreviousArticle)) {
            return new CheckStatus("IncorrectPredecessorArticle",
                                   "Article has 'predecessor' article equal to \"" +
                                   predecessor +
                                   "\" while previous article has URL \"" +
                                   previousArticleData.links().get(0).getUrl() +
                                   "\"",
                                   Optional.empty());
        }

        return null;
    }
}
