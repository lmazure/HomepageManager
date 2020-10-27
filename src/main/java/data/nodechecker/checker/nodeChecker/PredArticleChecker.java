package data.nodechecker.checker.nodeChecker;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XMLHelper;
import utils.XmlParsingException;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlParser;

public class PredArticleChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    public PredArticleChecker() {
        super(s_selector,
                PredArticleChecker::checkPredArticle, "the previous article is not the one defined by the 'pred' attribute");
    }

    private static CheckStatus checkPredArticle(final Element e) {

        final String pred = e.getAttribute("pred");
        if (pred.isEmpty()) return null;

        if (!XMLHelper.isOfType(e.getParentNode(), ElementType.ITEM)) {
            return new CheckStatus("Article has 'pred' attribute, but it is not in an <ITEM>");
        }
        final Element previousSibling = XMLHelper.getPreviousSiblingElement((Element)e.getParentNode());
        if (previousSibling == null) {
            return new CheckStatus("Article has 'pred' attribute, but there is no previous element");
        }
        if (!XMLHelper.isOfType(previousSibling, ElementType.ITEM)) {
            return new CheckStatus("Article has 'pred' attribute, but previous element is not an <ITEM>");
        }
        if (!XMLHelper.isOfType(previousSibling.getFirstChild(), ElementType.ARTICLE)) {
            return new CheckStatus("Article has 'pred' attribute, but previous <ITEM> does not contain an <ARTICLE>");
        }

        final Element previousArticle = (Element)previousSibling.getFirstChild();
        ArticleData previousArticleData;
        try {
            previousArticleData = XmlParser.parseArticleElement(previousArticle);
        } catch (@SuppressWarnings("unused") final XmlParsingException ex) {
            return new CheckStatus("Failed to parse article");
        }
        final String urlOfPreviousArticle = previousArticleData.getLinks().get(0).getUrl();

        if (!pred.equals(urlOfPreviousArticle)) {
            return new CheckStatus("Article has 'pred' article equal to \"" +
                                   pred +
                                   "\" while previous article has URL \"" +
                                   previousArticleData.getLinks().get(0).getUrl() +
                                   "\"");
        }

        return null;
    }
}
