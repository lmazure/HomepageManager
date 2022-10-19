package data.linkchecker.gitlabblog;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class GitlabBlogLinkContentChecker extends ExtractorBasedLinkContentChecker {

    public GitlabBlogLinkContentChecker(final String url,
                                        final LinkData linkData,
                                        final Optional<ArticleData> articleData,
                                        final FileSection file) {
        super(url, linkData, articleData, file, (ThrowingLinkDataExtractor)GitlabBlogLinkContentParser::new);
    }

}
