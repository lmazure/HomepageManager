package data.nodechecker.checker.nodeChecker;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import utils.XmlHelper;
import utils.XmlParsingException;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlParser;

public class ArticleDateChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    public ArticleDateChecker() {
        super(s_selector,
              ArticleDateChecker::checkArticleDatesToPageDate, "incorrect article creation/publication date compared to page date",
              ArticleDateChecker::checkArticleDateToPreviousArticleDate, "article not properly sorted according to date");
    }

    private static CheckStatus checkArticleDatesToPageDate(final Element e) {

        Optional<TemporalAccessor> pageDate;
        try {
            pageDate = getPageDate(e);
        } catch (@SuppressWarnings("unused") final XmlParsingException ex) {
            return new CheckStatus("Failed to parse page date");
        }
        if (pageDate.isEmpty()) {
            // should not happen
            return null;
        }

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (@SuppressWarnings("unused") final XmlParsingException ex) {
            return new CheckStatus("Failed to parse article");
        }
        final Optional<TemporalAccessor> creationDate = articleData.getDate();
        if (creationDate.isPresent()) {
            if (compareTemporalAccesssor(creationDate.get(), pageDate.get()) > 0) {
                return new CheckStatus("Creation date of article \"" +
                                       articleData.getLinks().get(0).getUrl() +
                                       "\" (" +
                                       creationDate.get() +
                                       ") is after page date (" +
                                       pageDate.get() +
                                       ")");
            }
        }

        for (final LinkData l: articleData.getLinks()) {
            final Optional<TemporalAccessor> publicationDate = l.getPublicationDate();
            if (publicationDate.isPresent()) {
                if (compareTemporalAccesssor(publicationDate.get(), pageDate.get()) > 0) {
                    return new CheckStatus("Publication date of article \"" +
                                           l.getUrl() +
                                           "\" (" +
                                           publicationDate.get() +
                                           ") is after page date (" +
                                           pageDate.get() +
                                           ")");
                }
                if (compareTemporalAccesssor(publicationDate.get(), creationDate.get()) < 0) {
                    return new CheckStatus("Publication date of article \"" +
                                           l.getUrl() +
                                           "\" (" +
                                           publicationDate.get() +
                                           ") is before creation date (" +
                                           creationDate.get() +
                                           ")");
                }
            }
        }

        return null;
    }

    private static CheckStatus checkArticleDateToPreviousArticleDate(final Element e) {

        final Element firstArticleOfChain = getFirstArticleOfArticleChain(e);
        if (firstArticleOfChain == null) {
            return new CheckStatus("Incorrect chain or articles");
        }
        if (firstArticleOfChain != e) {
            // we only verify the first article of the chain
            return null;
        }

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (@SuppressWarnings("unused") final XmlParsingException e1x) {
            return new CheckStatus("Failed to parse article");
        }
        final Optional<TemporalAccessor> creationDate = articleData.getDate();

        if (!XmlHelper.isOfType(e.getParentNode(), ElementType.ITEM)) return null;
        final Element previousSibling = XmlHelper.getPreviousSiblingElement((Element)e.getParentNode());
        if (previousSibling == null) return null;
        if (!XmlHelper.isOfType(previousSibling, ElementType.ITEM)) return null;
        if (!XmlHelper.isOfType(previousSibling.getFirstChild(), ElementType.ARTICLE)) return null;
        final Element previousArticle = (Element)previousSibling.getFirstChild();
        final Element firstArticleOfPreviousChain = getFirstArticleOfArticleChain(previousArticle);
        if (firstArticleOfPreviousChain == null) {
            return new CheckStatus("Incorrect chain or articles");
        }

        ArticleData previousArticleData;
        try {
            previousArticleData = XmlParser.parseArticleElement(firstArticleOfPreviousChain);
        } catch (@SuppressWarnings("unused") final XmlParsingException ex) {
            return new CheckStatus("Failed to parse article");
        }
        final Optional<TemporalAccessor> previousCreationDate = previousArticleData.getDate();

        if (previousCreationDate.isPresent() && creationDate.isEmpty()) {
            return new CheckStatus("Article \"" +
                                   articleData.getLinks().get(0).getUrl() +
                                   "\" has no date while being after article \"" +
                                   previousArticleData.getLinks().get(0).getUrl() +
                                   "\" which has a date");
        }

        if (previousCreationDate.isEmpty()) return null;

        if (compareTemporalAccesssor(previousCreationDate.get(), creationDate.get()) > 0) {
            return new CheckStatus("Creation date of article \"" +
                    articleData.getLinks().get(0).getUrl() +
                    "\" (" +
                    creationDate.get() +
                    ") is before creation date (" +
                    previousCreationDate.get() +
                    ") of previous article \"" +
                    previousArticleData.getLinks().get(0).getUrl() +
                    "\"");
        }

        return null;
    }

    /**
     * return the first article of a list of articles linked by 'predecessor'
     * if the 'predecessor' chain is incorrect, return null
     *
     * @param e
     * @return
     */
    private static Element getFirstArticleOfArticleChain(final Element e) {

        final String predecessor = e.getAttribute("predecessor");
        if (predecessor.isEmpty()) return e;

        if (!XmlHelper.isOfType(e.getParentNode(), ElementType.ITEM)) return null;
        final Element previousSibling = XmlHelper.getPreviousSiblingElement((Element)e.getParentNode());
        if (previousSibling == null) return null;
        if (!XmlHelper.isOfType(previousSibling, ElementType.ITEM)) return null;
        if (!XmlHelper.isOfType(previousSibling.getFirstChild(), ElementType.ARTICLE)) return null;
        final Element previousArticle = (Element)previousSibling.getFirstChild();

        return getFirstArticleOfArticleChain(previousArticle);
    }

    private static Optional<TemporalAccessor> getPageDate(final Element e) throws XmlParsingException {
        final List<Element> date = XmlHelper.getChildrenByElementType(e.getOwnerDocument().getDocumentElement(), ElementType.DATE);
        if (date.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(XmlParser.parseDateElement(date.get(0)));
    }

    private static int compareTemporalAccesssor(final TemporalAccessor accessor1,
                                                final TemporalAccessor accessor2) {
        final int year1 = accessor1.get(ChronoField.YEAR);
        final int year2 = accessor2.get(ChronoField.YEAR);
        final int month1 = accessor1.isSupported(ChronoField.MONTH_OF_YEAR) ? accessor1.get(ChronoField.MONTH_OF_YEAR) : 0;
        final int month2 = accessor2.isSupported(ChronoField.MONTH_OF_YEAR) ? accessor2.get(ChronoField.MONTH_OF_YEAR) : 0;
        final int day1 = accessor1.isSupported(ChronoField.DAY_OF_MONTH) ? accessor1.get(ChronoField.DAY_OF_MONTH) : 0;
        final int day2 = accessor2.isSupported(ChronoField.DAY_OF_MONTH) ? accessor2.get(ChronoField.DAY_OF_MONTH) : 0;

        return (((year1 * 1000) + month1) * 100 + day1) - (((year2 * 1000) + month2) * 100 + day2);
    }
}
