package fr.mazure.homepagemanager.data.linkchecker.gitlabblog;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for GitLab blog
*/
public class GitlabBlogLinkContentParser extends LinkDataExtractor {

    private final String _data;
    private static final TextParser s_titleParser = new TextParser("<title>",
                                                                   "</title>",
                                                                   "GitLab blog",
                                                                   "title");
    // the next regexp should be "//www.facebook.com(?:/sharer)?/sharer.php\\?u=https://about.gitlab.com/blog/", but GitLab screwed up their site
    // and used links such as "https://www.facebook.com/sharer/sharer.php?u=https://about.gitlab.com/blog/blog/2020/11/11/gitlab-for-agile-portfolio-planning-project-management/"
    private static final TextParser s_dateParser = new TextParser("//www.facebook.com(?:/sharer)?/sharer.php\\?u=https://about.gitlab.com(?:/blog)?/blog/",
                                                                  "\\d\\d\\d\\d/\\d\\d/\\d\\d",
                                                                  "/",
                                                                  "GitLab blog",
                                                                  "date");
    private static final TextParser s_authorParser1 = new TextParser("<div class=\"slp-flex-initial slp-order-last sm:slp-order-first\">",
                                                                     "<span class=\"slp-mr-2 slp-hidden sm:slp-inline-block\">",
                                                                     "GitLab blog",
                                                                     "author");
    private static final TextParser s_authorParser2 = new TextParser("<div class=\"author\" [^>]+>",
                                                                     "</div>",
                                                                     "GitLab blog",
                                                                     "author");
    private static final DateTimeFormatter s_dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH);

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
        return HtmlHelper.cleanContent(s_titleParser.extract(_data))
                         .replaceFirst(" \\| GitLab$", "");
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        return Optional.of(s_dateFormat.parse(HtmlHelper.cleanContent(s_dateParser.extract(_data))));
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        final Optional<String> opt = s_authorParser1.extractOptional(_data);
        final String authors = opt.isPresent() ? opt.get() : s_authorParser2.extract(_data);
        final String cleanedText = HtmlHelper.cleanContent(authors);
        return LinkContentParserUtils.getAuthors(cleanedText);
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
