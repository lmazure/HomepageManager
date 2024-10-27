package fr.mazure.homepagemanager.data.linkchecker.gitlabblog;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
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

    private static final String s_sourceName = "GitLab blog";

    private final String _data;
    private static final TextParser s_titleParser = new TextParser("<title>",
                                                                   "</title>",
                                                                   s_sourceName,
                                                                   "title");
    // the next regexp should be "//www.facebook.com(?:/sharer)?/sharer.php\\?u=https://about.gitlab.com/blog/", but GitLab screwed up their site
    // and used links such as "https://www.facebook.com/sharer/sharer.php?u=https://about.gitlab.com/blog/blog/2020/11/11/gitlab-for-agile-portfolio-planning-project-management/"
    private static final TextParser s_dateParser = new TextParser("//www.facebook.com(?:/sharer)?/sharer.php\\?u=https://about.gitlab.com(?:/blog)?/blog/",
                                                                  "\\d\\d\\d\\d/\\d\\d/\\d\\d",
                                                                  "/",
                                                                  s_sourceName,
                                                                  "date");
    private static final TextParser s_authorParser1 = new TextParser("<div class=\"slp-flex-initial slp-order-last sm:slp-order-first\">",
                                                                     "<span class=\"slp-mr-2 slp-hidden sm:slp-inline-block\">",
                                                                     s_sourceName,
                                                                     "author");
    private static final TextParser s_authorParser2 = new TextParser("<div class=\"author\" [^>]+>",
                                                                     "</div>",
                                                                     s_sourceName,
                                                                     "author");
    private static final DateTimeFormatter s_dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH);

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public GitlabBlogLinkContentParser(final String url,
                                       final String data,
                                       final CachedSiteDataRetriever retriever) {
        super(url, retriever);
        _data = data;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
      public static boolean isUrlManaged(final String url) {
        return url.startsWith("https://about.gitlab.com/blog/") && !url.equals("https://about.gitlab.com/blog/");
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

        // old blog entry
        if (opt.isPresent()) {
            final String cleanedText = HtmlHelper.cleanContent(opt.get());
            return LinkContentParserUtils.getAuthors(cleanedText);
        }

        // new blog entry
        final List<AuthorData> authors = new ArrayList<>(1);
        final List<String> extracted = s_authorParser2.extractMulti(_data);
        for (final String extract: extracted) {
            final String cleanedText = HtmlHelper.cleanContent(extract);
            if (cleanedText.equals("GitLab Security Team")) {
                continue;
            }
            final String removedTitle = cleanedText.replaceFirst(", (Chief Product Officer|co-founder|Guest Contributor|Ph\\.D\\.).*$", "");
            final List<AuthorData> author = LinkContentParserUtils.getAuthors(removedTitle);
            authors.addAll(author);
        }
        return authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl(),
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
