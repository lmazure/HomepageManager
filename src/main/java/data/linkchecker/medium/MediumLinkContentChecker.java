package data.linkchecker.medium;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

/**
*
*/
public class MediumLinkContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
        public MediumLinkContentChecker(final String url,
                                    final LinkData linkData,
                                    final Optional<ArticleData> articleData,
                                    final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)MediumLinkContentParser::new);
    }
}