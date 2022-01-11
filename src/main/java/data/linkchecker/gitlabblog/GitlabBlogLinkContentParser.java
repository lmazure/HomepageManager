package data.linkchecker.gitlabblog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import data.linkchecker.TextParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class GitlabBlogLinkContentParser {

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

    public GitlabBlogLinkContentParser(final String data) {
        _data = data;
    }

    public String getTitle() throws ContentParserException {
        return HtmlHelper.cleanContent(s_titleParser.extract(_data));
    }

    public LocalDate getDate() throws ContentParserException {
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
}
