package data.nodechecker.checker.nodeChecker;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.NodeType;
import utils.xmlparsing.XmlParser;

public class ArticleDateChecker extends NodeChecker {

    final static InclusionTagSelector s_selector = new InclusionTagSelector( new NodeType[] {
            NodeType.ARTICLE
            } );

    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkArticleDatesToPageDate(e);}
                            @Override
                            public String getDescription() { return "incorrect article date compared to page date"; } };
        return a;
    }

    private CheckStatus checkArticleDatesToPageDate(final Element e) {

        final Optional<TemporalAccessor> pageDate = getPageDate(e);
        if (pageDate.isEmpty()) {
            // should not happen
            return null;
        }

        final ArticleData articleData = XmlParser.parseArticleNode(e);
        final Optional<TemporalAccessor> creationDate = articleData.getDate();
        if (creationDate.isPresent()) {
            if (compareTemporalAccesssor(creationDate.get(), pageDate.get()) > 0) {
                return new CheckStatus("Creation date of article \"" + articleData.getLinks().get(0).getUrl() + "\" (" + creationDate.get() + ") is after page date (" + pageDate.get() + ")");
            }
        }

        for (LinkData l: articleData.getLinks()) {
            final Optional<TemporalAccessor> publicationDate = l.getPublicationDate();
            if (publicationDate.isPresent()) {
                if (compareTemporalAccesssor(publicationDate.get(), pageDate.get()) > 0) {
                    return new CheckStatus("Publication date of article \"" + l.getUrl() + "\" (" + publicationDate.get() + ") is after page date (" + pageDate.get() + ")");
                }
                if (compareTemporalAccesssor(publicationDate.get(), creationDate.get()) < 0) {
                    return new CheckStatus("Publication date of article \"" + l.getUrl() + "\" (" + publicationDate.get() + ") is before creation date (" + creationDate.get() + ")");
                }
            }
        }

        return null;
    }

    private Optional<TemporalAccessor> getPageDate(final Element e) {
        final List<Element> date = XMLHelper.getChildrenByNodeType(e.getOwnerDocument().getDocumentElement(), NodeType.DATE);
        if (date.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(XmlParser.parseDateNode(date.get(0)));
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
