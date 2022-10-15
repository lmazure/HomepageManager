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
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkFormat;

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

    public GitlabBlogLinkContentParser(final String url,
                                       final String data) {
        super(url);
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    public LocalDate getDateInternal() throws ContentParserException {
        return LocalDate.parse(HtmlHelper.cleanContent(s_dateParser.extract(_data)));
    }

    public List<AuthorData> getAuthors() throws ContentParserException {
        final String authors = s_authorParser.extract(_data);
        final List<AuthorData> authorList = new ArrayList<>();
        final String separator = " and ";
        if (authors.contains(separator)) {
            final String[] split = authors.split(separator);
            authorList.add(LinkContentParserUtils.getAuthor(split[0]));
            authorList.add(LinkContentParserUtils.getAuthor(split[1]));
        } else {
            authorList.add(LinkContentParserUtils.getAuthor(authors));
        }
        return authorList;
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(getDateInternal());
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        return getAuthors();
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return new ArrayList<>(0);
    }

    @Override
    public List<AuthorData> getPossibleAuthors()  {
        return new ArrayList<>(0);
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl().toString(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { Locale.ENGLISH },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }
}
