package data.linkchecker;

import java.io.File;
import java.net.URL;
import java.util.Optional;

import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class LinkContentCheckerFactory {

    public static LinkContentChecker build(final URL url,
                                           final LinkData linkData,
                                           final Optional<ArticleData> articleData,
                                           final File file) {

        if (url.toString().matches(".*[\\.=]pdf")) {
            // PDF files are ignored for the time being
            return new NoCheckContentChecker(linkData, articleData, file);
        }

        if (url.toString().endsWith(".ps")) {
            // PostScript files are ignored
            return new NoCheckContentChecker(linkData, articleData, file);
        }

        if (url.toString().endsWith(".gz")) {
            // GZIP files are ignored
            return new NoCheckContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://medium.com/")) {
            return new MediumLinkContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://www.youtube.com/channel/")) {
            return new YoutubeChannelUserLinkContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://www.youtube.com/user/")) {
            return new YoutubeChannelUserLinkContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkContentChecker(linkData, articleData, file);
            //return new YoutubeWatchLinkContentChecker2(url, linkData, articleData, file);
        }

        if (url.toString().startsWith("https://twitter.com/")) {
            return new TwitterLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.toString().startsWith("https://blog.chromium.org/")) {
            return new ChromiumBlogLinkContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://spectrum.ieee.org/")) {
            return new NoCheckContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://www.facebook.com/")) {
            return new NoCheckContentChecker(linkData, articleData, file);
        }

        if (url.toString().startsWith("https://www.linkedin.com/")) {
            return new NoCheckContentChecker(linkData, articleData, file);
        }

        return new LinkContentChecker(linkData, articleData, file);
    }
}
