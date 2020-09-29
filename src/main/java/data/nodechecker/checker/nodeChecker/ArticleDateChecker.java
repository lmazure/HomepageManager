package data.nodechecker.checker.nodeChecker;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.XmlParser;

public class ArticleDateChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[2];
        a[0] = new NodeRule() { @Override
            public CheckStatus checkElement(final Element e) { return checkArticleDatesToPageDate(e);}
                            @Override
                            public String getDescription() { return "incorrect article creation/publication date compared to page date"; } };
        a[1] = new NodeRule() { @Override
            public CheckStatus checkElement(final Element e) { return checkArticleDateToPreviousArticleDate(e);}
            @Override
            public String getDescription() { return "article not properly sorter according to date"; } };
        return a;
    }

    private CheckStatus checkArticleDatesToPageDate(final Element e) {

        final Optional<TemporalAccessor> pageDate = getPageDate(e);
        if (pageDate.isEmpty()) {
            // should not happen
            return null;
        }

        final ArticleData articleData = XmlParser.parseArticleElement(e);
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

    private CheckStatus checkArticleDateToPreviousArticleDate(final Element e) {

        final ArticleData articleData = XmlParser.parseArticleElement(e);
        final Optional<TemporalAccessor> creationDate = articleData.getDate();

        if (!XMLHelper.isOfType(e.getParentNode(), ElementType.ITEM)) return null;
        Node previousSibling = e.getParentNode().getPreviousSibling();
        if (previousSibling == null) return null;
        if (previousSibling.getNodeType() == Node.TEXT_NODE) {
            // skip indentation
            previousSibling = previousSibling.getPreviousSibling();
            if (previousSibling == null) return null;
        }
        if (!XMLHelper.isOfType(previousSibling, ElementType.ITEM)) return null;
        if (!XMLHelper.isOfType(previousSibling.getFirstChild(), ElementType.ARTICLE)) return null;

        
        final ArticleData previousArticleData = XmlParser.parseArticleElement((Element)previousSibling.getFirstChild());
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

    private Optional<TemporalAccessor> getPageDate(final Element e) {
        final List<Element> date = XMLHelper.getChildrenByNodeType(e.getOwnerDocument().getDocumentElement(), ElementType.DATE);
        if (date.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(XmlParser.parseDateElement(date.get(0)));
    }
    
    private int compareTemporalAccesssor(final TemporalAccessor accessor1,
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
