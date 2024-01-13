package fr.mazure.homepagemanager.data.linkchecker;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.chromium.ChromiumBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.ibm.IbmLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.medium.MediumLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.oracleblogs.OracleBlogsLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.quantamagazine.QuantaMagazineLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.spectrum.SpectrumLinkContentChecker;
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

    @FunctionalInterface
    private interface ThrowingLinkContentChecker {
        LinkContentChecker apply(final String url, final LinkData linkData, final Optional<ArticleData> articleData, final FileSection file);
    }

    private record CheckerData(Predicate<String> predicate, ThrowingLinkContentChecker constructor) {}
    
    private static final List<CheckerData> s_extractors = List.of(new CheckerData(ArsTechnicaLinkContentChecker::isUrlManaged, ArsTechnicaLinkContentChecker::new),
                                                                  new CheckerData(ChromiumBlogLinkContentChecker::isUrlManaged, ChromiumBlogLinkContentChecker::new),
                                                                  new CheckerData(OracleBlogsLinkContentChecker::isUrlManaged, OracleBlogsLinkContentChecker::new),
                                                                  new CheckerData(IbmLinkContentChecker::isUrlManaged, IbmLinkContentChecker::new),
                                                                  new CheckerData(GithubBlogLinkContentChecker::isUrlManaged, GithubBlogLinkContentChecker::new),
                                                                  new CheckerData(MediumLinkContentChecker::isUrlManaged, MediumLinkContentChecker::new),
                                                                  new CheckerData(WiredLinkContentChecker::isUrlManaged, WiredLinkContentChecker::new),
                                                                  new CheckerData(QuantaMagazineLinkContentChecker::isUrlManaged, QuantaMagazineLinkContentChecker::new),
                                                                  new CheckerData(YoutubeChannelUserLinkContentChecker::isUrlManaged, YoutubeChannelUserLinkContentChecker::new),
                                                                  new CheckerData(StackOverflowBlogContentChecker::isUrlManaged, StackOverflowBlogContentChecker::new),
                                                                  new CheckerData(SpectrumLinkContentChecker::isUrlManaged, SpectrumLinkContentChecker::new),
                                                                  new CheckerData(YoutubeChannelUserLinkContentChecker::isUrlManaged, YoutubeChannelUserLinkContentChecker::new),
                                                                  new CheckerData(YoutubeWatchLinkContentChecker::isUrlManaged, YoutubeWatchLinkContentChecker::new),
                                                                  new CheckerData(BaeldungLinkContentChecker::isUrlManaged, BaeldungLinkContentChecker::new),
                                                                  new CheckerData(GitlabBlogLinkContentChecker::isUrlManaged, GitlabBlogLinkContentChecker::new)
                                                                 );
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

        for (final CheckerData extractorData: s_extractors) {
            if (extractorData.predicate.test(url)) {
                return extractorData.constructor.apply(url, linkData, articleData, file);
                
            }
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
