package fr.mazure.homepagemanager.data.linkchecker.gitlabblog;

import java.time.LocalDate;
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
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for GitLab blog
 */
public class GitlabBlogLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "GitLab blog";

    private final String _title;
    private final Optional<TemporalAccessor> _creationDate;
    private final List<AuthorData> _sureAuthors;
    private final List<ExtractedLinkData> _links;

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
     *
     * @throws ContentParserException Failure to extract the information
     */
    public GitlabBlogLinkContentParser(final String url,
                                       final String data,
                                       final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        _title = HtmlHelper.cleanContent(s_titleParser.extract(data))
                           .replaceFirst(" \\| GitLab$", "");

        final String str = HtmlHelper.cleanContent(s_dateParser.extract(data));
        _creationDate = Optional.of(LocalDate.parse(str, s_dateFormat));

        final Optional<String> opt = s_authorParser1.extractOptional(data);
        if (opt.isPresent()) {
            // old blog entry
            final String cleanedText = HtmlHelper.cleanContent(opt.get());
            _sureAuthors = LinkContentParserUtils.getAuthors(cleanedText);
        } else {
            // new blog entry
            final List<AuthorData> authors = new ArrayList<>(1);
            final List<String> extracted = s_authorParser2.extractMulti(data);
            for (final String extract: extracted) {
                final String cleanedText = HtmlHelper.cleanContent(extract);
                if (cleanedText.equals("GitLab Security Team")) {
                    continue;
                }
                final String removedTitle = cleanedText.replaceFirst(", (Chief Product Officer|co-founder|Guest Contributor|Ph\\.D\\.).*$", "");
                final List<AuthorData> author = LinkContentParserUtils.getAuthors(removedTitle);
                authors.addAll(author);
            }
            _sureAuthors = authors;
        }

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] { },
                                                                 url,
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { Locale.ENGLISH },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        _links = list;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
          return UrlHelper.hasPrefix(url, "https://about.gitlab.com/blog/");
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.empty();
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return _creationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _creationDate;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _sureAuthors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }
}
