package data.linkchecker;

import java.nio.file.Path;

import data.linkchecker.arstechnica.ArsTechnicaLinkDataExtractor;
import data.linkchecker.baeldung.BaeldungLinkDataExtractor;
import data.linkchecker.gitlabblog.GitlabBlogLinkDataExtractor;
import data.linkchecker.medium.MediumLinkDataExtractor;
import data.linkchecker.oracleblogs.OracleBlogsLinkDataExtractor;
import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;
import data.linkchecker.wired.WiredLinkDataExtractor;
import data.linkchecker.youtubewatch.YoutubeWatchLinkDataExtractor;
import utils.UrlHelper;

public class LinkDataExtractorFactory {

    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final String url) throws ContentParserException { //TODO URGENT get rid of URL here

        final String u = UrlHelper.removeQueryParameters(url, "utm_source"
                                                            , "utm_medium");

        if (u.startsWith("https://arstechnica.com/")) {
            return new ArsTechnicaLinkDataExtractor(u, cacheDirectory);
        }

        if (u.startsWith("https://www.baeldung.com/") && !u.equals("https://www.baeldung.com/")) {
            return new BaeldungLinkDataExtractor(u, cacheDirectory);
        }

        if (u.startsWith("https://medium.com/")) {
            return new MediumLinkDataExtractor(u, cacheDirectory);
        }

        if (u.matches("https://blogs.oracle.com/javamagazine/.+")) {
            final String u2 = u.replace("/post/", "/");
            return new OracleBlogsLinkDataExtractor(u2, cacheDirectory);
        }

        if (u.startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkDataExtractor(u, cacheDirectory);
        }

        if (u.startsWith("https://www.youtube.com/watch?v=")) {
            final String u2 = UrlHelper.removeQueryParameters(u, "app",
                                                                 "list",
                                                                 "index");
            return new YoutubeWatchLinkDataExtractor(u2, cacheDirectory);
        }

        if (u.startsWith("https://about.gitlab.com/blog/")) {
            return new GitlabBlogLinkDataExtractor(u, cacheDirectory);
        }

        if (u.startsWith("https://www.wired.com/")) {
            return new WiredLinkDataExtractor(u, cacheDirectory);
        }

        return null;
    }
}
