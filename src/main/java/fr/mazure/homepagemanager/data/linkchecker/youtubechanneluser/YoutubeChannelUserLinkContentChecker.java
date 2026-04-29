package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class YoutubeChannelUserLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param retriever data retriever
     */
    public YoutubeChannelUserLinkContentChecker(final String url,
                                                final LinkData linkData,
                                                final Optional<ArticleData> articleData,
                                                final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, (LinkDataExtractorBuilder)YoutubeChannelUserLinkContentParser::new, retriever);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://www.youtube.com/channel/") ||
               UrlHelper.hasPrefix(url, "https://www.youtube.com/user/");
    }

    @Override
    public final List<LinkContentCheck> check() throws ContentParserException {
        try {
            return check(null);
        } catch (final ContentParserException e) {
            throw new ContentParserException("Failed to check data of \"" + getUrl() + "\"", e);
        }
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {

        super.checkGlobalData(data);

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {
        return null;
    }
}
