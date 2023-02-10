package data.linkchecker.gitlabblog;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.ExtractedLinkData;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.TextParser;
import utils.internet.HtmlHelper;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

/**
 * Data extractor for GitLab blog
*/
public class GitlabBlogLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private static final TextParser s_titleParser = new TextParser("<meta content='",
                                                                   "[^']+",
                                                                   "' property='og:title'/>",
                                                                   "GitLab blog",
                                                                   "title");
    private static final TextParser s_dateParser = new TextParser("<meta content='blog/blog-posts/",
                                                                  "[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]",
                                                                  "-[^']+' property='og:relative_path'/>",
                                                                  "GitLab blog",
                                                                  "date");
    private static final TextParser s_authorParser = new TextParser("<div class=\"slp-flex-initial slp-order-last sm:slp-order-first\">",
                                                                    "<span class=\"slp-mr-2 slp-hidden sm:slp-inline-block\">·</span>",
                                                                    "GitLab blog",
                                                                    "author");

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public GitlabBlogLinkContentParser(final String url,
                                       final String data) {
        super(url);
        _data = data;
    }

    @Override
    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(LocalDate.parse(HtmlHelper.cleanContent(s_dateParser.extract(_data))));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final String authors = s_authorParser.extract(_data);
        return LinkContentParserUtils.getAuthors(authors);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
