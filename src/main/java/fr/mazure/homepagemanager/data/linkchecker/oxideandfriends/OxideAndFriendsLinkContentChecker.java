package fr.mazure.homepagemanager.data.linkchecker.oxideandfriends;

import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Check data of an Oxide and Friends blog
 */
public class OxideAndFriendsLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param retriever data retriever
     */
    public OxideAndFriendsLinkContentChecker(final String url,
                                             final LinkData linkData,
                                             final Optional<ArticleData> articleData,
                                             final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, (LinkDataExtractorBuilder)OxideAndFriendsLinkContentParser::new, retriever);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return OxideAndFriendsLinkContentParser.isUrlManaged(url);
    }
}
