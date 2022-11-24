package data.linkchecker;

import java.nio.file.Path;

import data.internet.FullFetchedLinkData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import data.linkchecker.arstechnica.ArsTechnicaLinkContentParser;
import data.linkchecker.baeldung.BaeldungLinkContentParser;
import data.linkchecker.githubblog.GithubBlogLinkContentParser;
import data.linkchecker.gitlabblog.GitlabBlogLinkContentParser;
import data.linkchecker.medium.MediumLinkContentParser;
import data.linkchecker.oracleblogs.OracleBlogsLinkContentParser;
import data.linkchecker.quantamagazine.QuantaMagazineLinkContentParser;
import data.linkchecker.wired.WiredLinkContentParser;
import data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import utils.internet.HtmlHelper;
import utils.internet.UrlHelper;

/**
 *
 */
public class LinkDataExtractorFactory {

    private String _content;

    /**
     * @param cacheDirectory
     * @param url
     * @return
     * @throws ContentParserException
     */
    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final String url) throws ContentParserException {
        final LinkDataExtractorFactory factory = new LinkDataExtractorFactory();
        return factory.create(cacheDirectory, url);
    }

    private LinkDataExtractor create(final Path cacheDirectory,
                                     final String url) throws ContentParserException {

        String u = UrlHelper.removeQueryParameters(url, "utm_source"
                                                      , "utm_medium");

        ThrowingLinkDataExtractor constructor = null;

        if (u.startsWith("https://arstechnica.com/")) {
            constructor = ArsTechnicaLinkContentParser::new;
        }

        if (u.startsWith("https://www.baeldung.com/") && !u.equals("https://www.baeldung.com/")) {
            constructor = BaeldungLinkContentParser::new;
        }

        if (url.startsWith("https://github.blog/")) {
            constructor = GithubBlogLinkContentParser::new;
        }

        if (u.startsWith("https://medium.com/")) {
            constructor = MediumLinkContentParser::new;
        }

        if (u.matches("https://blogs.oracle.com/javamagazine/.+")) {
            u = u.replace("/post/", "/");
            constructor = OracleBlogsLinkContentParser::new;
        }

        if (u.startsWith("https://www.quantamagazine.org/")) {
            constructor = QuantaMagazineLinkContentParser::new;
        }

        if (u.startsWith("https://www.youtube.com/watch?")) {
            u = UrlHelper.removeQueryParameters(u, "app",
                                                   "feature",
                                                   "index",
                                                   "list",
                                                   "t");
            constructor = YoutubeWatchLinkContentParser::new;
        }

        if (u.startsWith("https://about.gitlab.com/blog/")) {
            constructor = GitlabBlogLinkContentParser::new;
        }

        if (u.startsWith("https://www.wired.com/")) {
            constructor = WiredLinkContentParser::new;
        }

        if (constructor == null) {
            return null;
        }

        final SiteDataPersister persister = new SiteDataPersister(cacheDirectory);
        final SynchronousSiteDataRetriever retriever = new SynchronousSiteDataRetriever(persister);
        retriever.retrieve(url, this::handleLinkData, false);

        return constructor.apply(u, _content);
    }

    private void handleLinkData(@SuppressWarnings("unused") final Boolean isDataFresh,
                                final FullFetchedLinkData siteData) {
        if (siteData.dataFileSection().isPresent()) {
            _content = HtmlHelper.slurpFile(siteData.dataFileSection().get());
        }
    }

    @FunctionalInterface
    private interface ThrowingLinkDataExtractor {
        LinkDataExtractor apply(final String url, final String data) throws ContentParserException;
    }
}
