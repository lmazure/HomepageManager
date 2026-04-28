package fr.mazure.homepagemanager.data.linkchecker.lexfridman;

import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Check data of a Lex Fridman podcast
 */
public class LexFridmanLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param retriever site data retriever
     */
    public LexFridmanLinkContentChecker(final String url,
                                        final LinkData linkData,
                                        final Optional<ArticleData> articleData,
                                        final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, (LinkDataExtractorBuilder)LexFridmanLinkContentParser::new, retriever);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return LexFridmanLinkContentParser.isUrlManaged(url);
    }
}
