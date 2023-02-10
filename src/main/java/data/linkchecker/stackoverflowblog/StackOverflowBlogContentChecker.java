package data.linkchecker.stackoverflowblog;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

/**
 * * Check data for a link toward a StackOverflow blog
 */
public class StackOverflowBlogContentChecker extends ExtractorBasedLinkContentChecker {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public StackOverflowBlogContentChecker(final String url,
                                           final LinkData linkData,
                                           final Optional<ArticleData> articleData,
                                           final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)StackOverflowBlogContentParser::new);
    }
}
