package fr.mazure.homepagemanager.data.linkchecker.quantamagazine;

import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Check data for a link toward a Quanta Magazine article
 */
public class QuantaMagazineLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public QuantaMagazineLinkContentChecker(final String url,
                                            final LinkData linkData,
                                            final Optional<ArticleData> articleData,
                                            final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)QuantaMagazineLinkContentParser::new);
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        return QuantaMagazineLinkContentParser.isUrlManaged(url);
    }
}
