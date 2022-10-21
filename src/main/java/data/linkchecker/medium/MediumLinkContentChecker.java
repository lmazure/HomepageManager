package data.linkchecker.medium;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class MediumLinkContentChecker extends ExtractorBasedLinkContentChecker {

        public MediumLinkContentChecker(final String url,
                                    final LinkData linkData,
                                    final Optional<ArticleData> articleData,
                                    final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)MediumLinkContentParser::new);
    }
}