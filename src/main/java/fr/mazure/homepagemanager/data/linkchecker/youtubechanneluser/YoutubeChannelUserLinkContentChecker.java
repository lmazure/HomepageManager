package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser;

import java.time.temporal.TemporalAccessor;
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
public class YoutubeChannelUserLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     * @param retriever data retriever
     */
    public YoutubeChannelUserLinkContentChecker(final String url,
                                                final LinkData linkData,
                                                final Optional<ArticleData> articleData,
                                                final FileSection file,
                                                final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)YoutubeChannelUserLinkContentParser::new, retriever);
	}

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return UrlHelper.hasPrefix(url, "https://www.youtube.com/channel/");
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {

        super.checkGlobalData(data);

        final YoutubeChannelUserLinkContentParser parser = (YoutubeChannelUserLinkContentParser)getParser();
        if (parser.getErrorMessage().isPresent()) {
            return new LinkContentCheck("LinkDataRetrievalFailure",
                                        parser.getErrorMessage().get(),
                                        Optional.empty());
        }

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
