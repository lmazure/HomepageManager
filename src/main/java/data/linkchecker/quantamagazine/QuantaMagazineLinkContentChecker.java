package data.linkchecker.quantamagazine;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

/**
*
*/
public class QuantaMagazineLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved kink data
     */
    public QuantaMagazineLinkContentChecker(final String url,
                                            final LinkData linkData,
                                            final Optional<ArticleData> articleData,
                                            final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)QuantaMagazineLinkContentParser::new);
    }
}
