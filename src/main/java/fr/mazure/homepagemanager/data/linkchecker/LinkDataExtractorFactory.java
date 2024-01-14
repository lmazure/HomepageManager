package fr.mazure.homepagemanager.data.linkchecker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SiteDataPersister;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.medium.MediumLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.oracleblogs.OracleBlogsLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.quantamagazine.QuantaMagazineLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.spectrum.SpectrumLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog.StackOverflowBlogContentParser;
import fr.mazure.homepagemanager.data.linkchecker.wired.WiredLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;

/**
 * Factory returning the LinkDataExtractor able to extract data from a given URL
 */
public class LinkDataExtractorFactory {

    private String _content;

    @FunctionalInterface
    private interface ThrowingLinkDataExtractor {
        LinkDataExtractor apply(final String url, final String data) throws ContentParserException;
    }

    private record ExtractorData(Predicate<String> predicate, ThrowingLinkDataExtractor constructor) {}


    private static final List<ExtractorData> s_extractors = new java.util.ArrayList<>();

    static {
        final List<Class<?>> extractors = List.of(
                ArsTechnicaLinkContentParser.class,
                BaeldungLinkContentParser.class,
                GithubBlogLinkContentParser.class,
                MediumLinkContentParser.class,
                OracleBlogsLinkContentParser.class,
                QuantaMagazineLinkContentParser.class,
                StackOverflowBlogContentParser.class,
                SpectrumLinkContentParser.class,
                YoutubeWatchLinkContentParser.class,
                GitlabBlogLinkContentParser.class,
                WiredLinkContentParser.class
               );
        for (final Class<?> clazz: extractors) {
            try {
                final Method method = clazz.getDeclaredMethod("isUrlManaged", String.class);
                final Constructor<?> cons = clazz.getConstructor(String.class, String.class);
                s_extractors.add(new ExtractorData((final String url) -> {
                                                       try {
                                                           return ((Boolean)method.invoke(null, url)).booleanValue();
                                                       } catch (final IllegalAccessException | InvocationTargetException e) {
                                                           ExitHelper.exit(e);
                                                           // NOTREACHED
                                                           return false;
                                                       }},
                                                   (final String url, final String data) -> {
                                                       try {
                                                           return (LinkDataExtractor)cons.newInstance(url, data);
                                                       } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                                                           ExitHelper.exit(e);
                                                           // NOTREACHED
                                                           return null;
                                                       }}));
            } catch (final NoSuchMethodException e) {
                ExitHelper.exit(e);
            }
        }
    }

    /**
     * @param cacheDirectory directory where the persistence files should be written
     * @param url URL to check
     * @return LinkDataExtractor able to extract data from the link, null if there is no such LinkDataExtractor
     * @throws ContentParserException Failure to extract the information
     */
    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final String url) throws ContentParserException {
        final LinkDataExtractorFactory factory = new LinkDataExtractorFactory();
        return factory.create(cacheDirectory, url);
    }

    private LinkDataExtractor create(final Path cacheDirectory,
                                     final String url) throws ContentParserException {

        final String u = UrlHelper.removeQueryParameters(url, "utm_source",
                                                              "utm_medium",
                                                              "utm_campaign",
                                                              "utm_content",
                                                              "utm_term");

        for (final ExtractorData extractorData: s_extractors){
            if (extractorData.predicate.test(u)) {
                final SiteDataPersister persister = new SiteDataPersister(cacheDirectory);
                final SynchronousSiteDataRetriever retriever = new SynchronousSiteDataRetriever(persister);
                retriever.retrieve(url, this::handleLinkData, false);
                return extractorData.constructor.apply(u, _content);
                
            }
        }

        return null;
    }

    private void handleLinkData(@SuppressWarnings("unused") final Boolean isDataFresh,
                                final FullFetchedLinkData siteData) {
        if (siteData.dataFileSection().isPresent()) {
            _content = HtmlHelper.slurpFile(siteData.dataFileSection().get());
        }
    }


}
