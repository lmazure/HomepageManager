package fr.mazure.homepagemanager.data.linkchecker;

import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.chromium.ChromiumBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.ibm.IbmLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.medium.MediumLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.oracleblogs.OracleBlogsLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.quantamagazine.QuantaMagazineLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog.StackOverflowBlogContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.wired.WiredLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentChecker;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Factory returning the LinkContentChecker able to check a given URL
 */
public class LinkContentCheckerFactory {

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     * @return LinkContentChecker able to check the link
     */
    public static LinkContentChecker build(final String url,
                                           final LinkData linkData, // TODO we should not have to provide linkData and articleData to the factory
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

        if (url.startsWith("https://blog.chromium.org/")) {
            return new ChromiumBlogLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.matches("https://blogs.oracle.com/javamagazine/.+") ||
            url.matches("https://blogs.oracle.com/java/.+")) {
            return new OracleBlogsLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://developer.ibm.com/articles/") ||
            url.startsWith("https://developer.ibm.com/tutorials/")) {
            return new IbmLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://github.blog/")) {
            return new GithubBlogLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://medium.com/")) {
            return new MediumLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.wired.com/")) {
            return new WiredLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.youtube.com/channel/")) {
            return new YoutubeChannelUserLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://stackoverflow.blog/")) {
            return new StackOverflowBlogContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.youtube.com/user/")) {
            return new YoutubeChannelUserLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkContentChecker(url, linkData, articleData, file);
            //return new YoutubeWatchLinkContentChecker2(url, linkData, articleData, file);
        }

        if (url.matches("https://www.baeldung.com/.+")) {
            return new BaeldungLinkContentChecker(url, linkData, articleData, file);
        }

        if (url.startsWith("https://about.gitlab.com/blog/")) {
            return new GitlabBlogLinkContentChecker(url, linkData, articleData, file);
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
