package fr.mazure.homepagemanager.data.linkchecker.ibm;

import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class IbmLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     * @param retriever data retriever
     */
    public IbmLinkContentChecker(final String url,
                                 final LinkData linkData,
                                 final Optional<ArticleData> articleData,
                                 final FileSection file,
                                 final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)IbmLinkContentParser::new, retriever);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://developer.ibm.com/articles/") || UrlHelper.hasPrefix(url, "https://developer.ibm.com/tutorials/");
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {
        final LinkContentCheck result = super.checkGlobalData(data);
        if (result != null) {
            return result;
        }

        if (((IbmLinkContentParser)getParser()).articleIsLost()) {
            return new LinkContentCheck("LostArticle",
                                        "article is lost",
                                        Optional.empty());
        }

        return null;
    }
}
