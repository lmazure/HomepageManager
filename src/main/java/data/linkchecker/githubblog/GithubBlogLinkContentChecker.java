package data.linkchecker.githubblog;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class GithubBlogLinkContentChecker extends ExtractorBasedLinkContentChecker {

    public GithubBlogLinkContentChecker(final String url,
                                        final LinkData linkData,
                                        final Optional<ArticleData> articleData,
                                        final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)GithubBlogLinkContentParser::new);
    }
}
