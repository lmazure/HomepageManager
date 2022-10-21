package data.linkchecker.baeldung;

import java.util.Optional;

import data.linkchecker.ExtractorBasedLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class BaeldungLinkContentChecker extends ExtractorBasedLinkContentChecker {

    public BaeldungLinkContentChecker(final String url,
                                      final LinkData linkData,
                                      final Optional<ArticleData> articleData,
                                      final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)BaeldungLinkContentParser::new);
    }
}
