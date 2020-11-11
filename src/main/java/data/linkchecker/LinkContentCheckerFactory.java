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

        if (url.toString().startsWith("https://medium.com/")) {
            return new MediumLinkContentChecker(linkData, articleData, file);
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

        return new LinkContentChecker(linkData, articleData, file);
    }
}
