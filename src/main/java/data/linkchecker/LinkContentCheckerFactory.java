package data.linkchecker;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import data.linkchecker.arstechnica.ArsTechnicaLinkContentChecker;
import data.linkchecker.baeldung.BaeldungLinkContentChecker;
import data.linkchecker.chromium.ChromiumBlogLinkContentChecker;
import data.linkchecker.gitlabblog.GitlabBlogLinkContentChecker;
import data.linkchecker.medium.MediumLinkContentChecker;
import data.linkchecker.oracleblogs.OracleBlogsLinkContentChecker;
import data.linkchecker.quantamagazine.QuantaMagazineLinkContentChecker;
import data.linkchecker.twitter.TwitterLinkContentChecker;
import data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentChecker;
import data.linkchecker.youtubewatch.YoutubeWatchLinkContentChecker;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class LinkContentCheckerFactory {

    public static LinkContentChecker build(final URL url,
                                           final LinkData linkData,
                                           final Optional<ArticleData> articleData,
                                           final File file) {

        final String urlString = url.toString();

        if (urlString.matches(".*[\\.=]pdf")) {
            // PDF files are ignored for the time being
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (urlString.endsWith(".ps")) {
            // PostScript files are ignored
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (urlString.endsWith(".gz")) {
            // GZIP files are ignored
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://arstechnica.com/")) {
            return new ArsTechnicaLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.matches("https://blogs.oracle.com/javamagazine/.+")) {
            return new OracleBlogsLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://medium.com/")) {
            return new MediumLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.youtube.com/channel/")) {
            return new YoutubeChannelUserLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.youtube.com/user/")) {
            return new YoutubeChannelUserLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkContentChecker(url, linkData, articleData, file);
            //return new YoutubeWatchLinkContentChecker2(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://twitter.com/")) {
            return new TwitterLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://blog.chromium.org/")) {
            return new ChromiumBlogLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.baeldung.com/") && !urlString.equals("https://www.baeldung.com/")) {
            return new BaeldungLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://about.gitlab.com/blog/")) {
            return new GitlabBlogLinkContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://spectrum.ieee.org/")) {
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.facebook.com/")) {
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (urlString.startsWith("https://www.linkedin.com/")) {
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        return new LinkContentChecker(url, linkData, articleData, file);
    }
}
