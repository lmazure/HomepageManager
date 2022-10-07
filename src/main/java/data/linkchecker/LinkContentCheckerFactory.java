package data.linkchecker;

import java.util.Optional;

import data.linkchecker.arstechnica.ArsTechnicaLinkContentChecker;
import data.linkchecker.baeldung.BaeldungLinkContentChecker;
import data.linkchecker.chromium.ChromiumBlogLinkContentChecker;
import data.linkchecker.gitlabblog.GitlabBlogLinkContentChecker;
import data.linkchecker.ibm.IbmLinkContentChecker;
import data.linkchecker.medium.MediumLinkContentChecker;
import data.linkchecker.oracleblogs.OracleBlogsLinkContentChecker;
import data.linkchecker.quantamagazine.QuantaMagazineLinkContentChecker;
import data.linkchecker.twitter.TwitterLinkContentChecker;
import data.linkchecker.wired.WiredLinkContentChecker;
import data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentChecker;
import data.linkchecker.youtubewatch.YoutubeWatchLinkContentChecker;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class LinkContentCheckerFactory {

    public static LinkContentChecker build(final String url,
                                           final LinkData linkData,
                                           final Optional<ArticleData> articleData,
                                           final FileSection file) {

        if (url.matches(".*[\\.=]pdf")) {
            // PDF files are ignored for the time being
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (url.endsWith(".ps")) {
            // PostScript files are ignored
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (url.endsWith(".gz")) {
            // GZIP files are ignored
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://arstechnica.com/")) {
            return new ArsTechnicaLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.matches("https://blogs.oracle.com/javamagazine/.+")) {
            return new OracleBlogsLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://developer.ibm.com/articles/") ||
            url.startsWith("https://developer.ibm.com/tutorials/")) {
            return new IbmLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://medium.com/")) {
            return new MediumLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.youtube.com/channel/")) {
            return new YoutubeChannelUserLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.youtube.com/user/")) {
            return new YoutubeChannelUserLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkContentChecker(url, linkData, articleData, file);
            //return new YoutubeWatchLinkContentChecker2(url, linkData, articleData, file);
        }

        if (url.startsWith("https://twitter.com/")) {
            return new TwitterLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://blog.chromium.org/")) {
            return new ChromiumBlogLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.matches("https://www.baeldung.com/.+")) {
            return new BaeldungLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://about.gitlab.com/blog/")) {
            return new GitlabBlogLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.wired.com/")) {
            return new WiredLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://spectrum.ieee.org/")) {
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.facebook.com/")) {
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.linkedin.com/")) {
            return new NoCheckContentChecker(url, linkData, articleData, file);
        }

        return new LinkContentChecker(url, linkData, articleData, file);
    }
}
