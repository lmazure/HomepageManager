package fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog;

import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ExtractorBasedLinkContentChecker;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

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
