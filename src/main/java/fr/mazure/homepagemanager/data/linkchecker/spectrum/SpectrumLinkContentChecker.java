package fr.mazure.homepagemanager.data.linkchecker.spectrum;

import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Check data of an IEEE Spectrum link
 */
public class SpectrumLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     * @param retriever data retriever
     */
    public SpectrumLinkContentChecker(final String url,
                                      final LinkData linkData,
                                      final Optional<ArticleData> articleData,
                                      final FileSection file,
                                      final CachedSiteDataRetriever retriever) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)SpectrumLinkContentParser::new, retriever);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return SpectrumLinkContentParser.isUrlManaged(url);
    }
}
