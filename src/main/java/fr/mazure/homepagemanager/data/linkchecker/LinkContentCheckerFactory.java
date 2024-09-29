package fr.mazure.homepagemanager.data.linkchecker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.chromium.ChromiumBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.dzone.DZoneLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.ibm.IbmLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.medium.MediumLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.oracleblogs.OracleBlogsLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.quantamagazine.QuantaMagazineLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.spectrum.SpectrumLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog.StackOverflowBlogLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.substack.SubstackLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.wired.WiredLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentChecker;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentChecker;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 * Factory returning the LinkContentChecker able to check a given URL
 */
public class LinkContentCheckerFactory {

    private record CheckerData(Predicate<String> predicate, Constructor<LinkContentChecker> constructor) {}

    private static final List<CheckerData> s_checkers = new java.util.ArrayList<>();

    static {
        final List<Class<? extends LinkContentChecker>> checkers = List.of(
                ArsTechnicaLinkContentChecker.class,
                BaeldungLinkContentChecker.class,
                ChromiumBlogLinkContentChecker.class,
                GithubBlogLinkContentChecker.class,
                GitlabBlogLinkContentChecker.class,
                IbmLinkContentChecker.class,
                MediumLinkContentChecker.class,
                DZoneLinkContentChecker.class,
                OracleBlogsLinkContentChecker.class,
                QuantaMagazineLinkContentChecker.class,
                SpectrumLinkContentChecker.class,
                StackOverflowBlogLinkContentChecker.class,
                SubstackLinkContentChecker.class,
                WiredLinkContentChecker.class,
                YoutubeChannelUserLinkContentChecker.class,
                YoutubeWatchLinkContentChecker.class
               );
        for (final Class<?> clazz: checkers) {
            try {
                final Method method = clazz.getDeclaredMethod("isUrlManaged", String.class);
                @SuppressWarnings("unchecked")
                final Constructor<LinkContentChecker> cons = (Constructor<LinkContentChecker>)clazz.getConstructor(String.class, LinkData.class, Optional.class, FileSection.class);
                s_checkers.add(new CheckerData((final String url) -> {
                                                   try {
                                                       return ((Boolean)method.invoke(null, url)).booleanValue();
                                                   } catch (final IllegalAccessException | InvocationTargetException e) {
                                                       ExitHelper.exit(e);
                                                       // NOTREACHED
                                                       return false;
                                                   }},
                                               cons));
            } catch (final NoSuchMethodException e) {
                ExitHelper.exit(e);
            }
        }
    }

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

        for (final CheckerData checkerData: s_checkers) {
            if (checkerData.predicate.test(url)) {
                try {
                    return checkerData.constructor.newInstance(url, linkData, articleData, file);
                } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    ExitHelper.exit(e);
                    // NOTREACHED
                    return null;
                }
            }
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
