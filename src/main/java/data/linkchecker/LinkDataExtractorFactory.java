package data.linkchecker;

import java.nio.file.Path;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import data.linkchecker.arstechnica.ArsTechnicaLinkDataExtractor;
import data.linkchecker.baeldung.BaeldungLinkDataExtractor;
import data.linkchecker.githubblog.GithubBlogLinkContentParser;
import data.linkchecker.gitlabblog.GitlabBlogLinkDataExtractor;
import data.linkchecker.medium.MediumLinkDataExtractor;
import data.linkchecker.oracleblogs.OracleBlogsLinkDataExtractor;
import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;
import data.linkchecker.wired.WiredLinkDataExtractor;
import data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import utils.HtmlHelper;
import utils.UrlHelper;

public class LinkDataExtractorFactory {

    private String _content;

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
            constructor = ArsTechnicaLinkDataExtractor::new;
        }

        if (u.startsWith("https://www.baeldung.com/") && !u.equals("https://www.baeldung.com/")) {
            constructor = BaeldungLinkDataExtractor::new;
        }


        if (url.startsWith("https://github.blog/")) {
            constructor = GithubBlogLinkContentParser::new;
        }

        if (u.startsWith("https://medium.com/")) {
            constructor = MediumLinkDataExtractor::new;
        }

        if (u.matches("https://blogs.oracle.com/javamagazine/.+")) {
            u = u.replace("/post/", "/");
            constructor = OracleBlogsLinkDataExtractor::new;
        }

        if (u.startsWith("https://www.quantamagazine.org/")) {
            constructor = QuantaMagazineLinkDataExtractor::new;
        }

        if (u.startsWith("https://www.youtube.com/watch?")) {
            u = UrlHelper.removeQueryParameters(u, "app",
                                                   "feature",
                                                   "list",
                                                   "index");
            constructor = YoutubeWatchLinkContentParser::new;
        }

        if (u.startsWith("https://about.gitlab.com/blog/")) {
            constructor = GitlabBlogLinkDataExtractor::new;
        }

        if (u.startsWith("https://www.wired.com/")) {
            constructor = WiredLinkDataExtractor::new;
        }

        if (constructor == null) {
            return null;
        }

        final SiteDataPersister persister = new SiteDataPersister(cacheDirectory);
        final SynchronousSiteDataRetriever retriever = new SynchronousSiteDataRetriever(persister);
        retriever.retrieve(url, this::handleLinkData);

        return constructor.apply(u, _content);
    }

    private void handleLinkData(@SuppressWarnings("unused") final Boolean isDataFresh,
                                final SiteData siteData) {
        if (siteData.getDataFile().isPresent()) {
            _content = HtmlHelper.slurpFile(siteData.getDataFile().get());
        }
    }
    
    @FunctionalInterface
    private interface ThrowingLinkDataExtractor {
        LinkDataExtractor apply(final String url, final String data) throws ContentParserException;
    }
}
