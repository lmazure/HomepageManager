package data.linkchecker;

import java.net.URL;
import java.nio.file.Path;

import data.linkchecker.arstechnica.ArsTechnicaLinkDataExtractor;
import data.linkchecker.baeldung.BaeldungLinkDataExtractor;
import data.linkchecker.gitlabblog.GitlabBlogLinkDataExtractor;
import data.linkchecker.medium.MediumLinkDataExtractor;
import data.linkchecker.oracleblogs.OracleBlogsLinkDataExtractor;
import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;
import data.linkchecker.youtubewatch.YoutubeWatchLinkDataExtractor;
import utils.StringHelper;

public class LinkDataExtractorFactory {

    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final URL url) throws ContentParserException {

        final String urlString = url.toString();
        final URL u = cleanUrl(url);

        if (urlString.startsWith("https://arstechnica.com/")) {
            return new ArsTechnicaLinkDataExtractor(u, cacheDirectory);
        }

        if (urlString.startsWith("https://www.baeldung.com/") && !urlString.equals("https://www.baeldung.com/")) {
            return new BaeldungLinkDataExtractor(u, cacheDirectory);
        }


        if (urlString.startsWith("https://medium.com/")) {
            return new MediumLinkDataExtractor(u, cacheDirectory);
        }

        if (urlString.matches("https://blogs.oracle.com/javamagazine/.+")) {
            return new OracleBlogsLinkDataExtractor(u, cacheDirectory);
        }

        if (urlString.startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkDataExtractor(u, cacheDirectory);
        }

        if (urlString.startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkDataExtractor(u, cacheDirectory);
        }

        if (urlString.startsWith("https://about.gitlab.com/blog/")) {
            return new GitlabBlogLinkDataExtractor(u, cacheDirectory);
        }

        return null;
    }

    private static URL cleanUrl(final URL url) {
        final String initialUrl = url.toString();
        final String cleanedUrl = initialUrl.replaceAll("/?utm_[^/]+$", "").replaceAll("\\?$", "");
        return StringHelper.convertStringToUrl(cleanedUrl);
    }
}
