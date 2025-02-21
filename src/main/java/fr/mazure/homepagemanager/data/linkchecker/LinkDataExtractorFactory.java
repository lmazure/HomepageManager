package fr.mazure.homepagemanager.data.linkchecker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiPredicate;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.dzone.DZoneLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.huggingface.HuggingFaceLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.lexfridman.LexFridmanLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.medium.MediumLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.oracleblogs.OracleBlogsLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.oxideandfriends.OxideAndFriendsLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.quantamagazine.QuantaMagazineLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.simonwillison.SimonWillisonLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.simonwillisontil.SimonWillisonTilLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.spectrum.SpectrumLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog.StackOverflowBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.substack.SubstackLinkContentParser;
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

    private record ExtractorData(BiPredicate<String, CachedSiteDataRetriever> predicate, Constructor<LinkDataExtractor> constructor) {}

    private static final List<ExtractorData> s_extractors = new java.util.ArrayList<>();

    static {
        final List<Class<? extends LinkDataExtractor>> extractors = List.of(
                ArsTechnicaLinkContentParser.class,
                BaeldungLinkContentParser.class,
                DZoneLinkContentParser.class,
                GithubBlogLinkContentParser.class,
                GitlabBlogLinkContentParser.class,
                HuggingFaceLinkContentParser.class,
                LexFridmanLinkContentParser.class,
                MediumLinkContentParser.class,
                OracleBlogsLinkContentParser.class,
                OxideAndFriendsLinkContentParser.class,
                QuantaMagazineLinkContentParser.class,
                SimonWillisonLinkContentParser.class,
                SimonWillisonTilLinkContentParser.class,
                SpectrumLinkContentParser.class,
                StackOverflowBlogLinkContentParser.class,
                SubstackLinkContentParser.class,
                WiredLinkContentParser.class,
                YoutubeWatchLinkContentParser.class
               );
        for (final Class<?> clazz: extractors) {
            try {
                final Method method = clazz.getDeclaredMethod("isUrlManaged", String.class);
                @SuppressWarnings("unchecked")
                final Constructor<LinkDataExtractor> cons = (Constructor<LinkDataExtractor>)clazz.getConstructor(String.class, String.class, CachedSiteDataRetriever.class);
                s_extractors.add(new ExtractorData((final String url, final CachedSiteDataRetriever _) -> {
                                                       try {
                                                           return ((Boolean)method.invoke(null, url)).booleanValue();
                                                       } catch (final IllegalAccessException | InvocationTargetException e) {
                                                           ExitHelper.exit(e);
                                                           // NOTREACHED
                                                           return false;
                                                       }
                                                   },
                                                   cons));
            } catch (final NoSuchMethodException e) {
                ExitHelper.exit(e);
            }
        }
    }

    /**
     * @param url URL to check
     * @param retriever data retriever
     * @return LinkDataExtractor able to extract data from the link, null if there is no such LinkDataExtractor
     * @throws ContentParserException Failure to extract the information
     */
    public static LinkDataExtractor build(final String url,
                                          final CachedSiteDataRetriever retriever) throws ContentParserException {
        final LinkDataExtractorFactory factory = new LinkDataExtractorFactory();
        return factory.create(url, retriever);
    }

    private LinkDataExtractor create(final String url,
                                     final CachedSiteDataRetriever retriever) {

        final String u = UrlHelper.removeQueryParameters(url, "utm_source",
                                                              "utm_medium",
                                                              "utm_campaign",
                                                              "utm_content",
                                                              "utm_term");

        for (final ExtractorData extractorData: s_extractors){
            if (extractorData.predicate.test(u, retriever)) {
                retriever.retrieve(url, this::handleLinkData, false);
                try {
                    return extractorData.constructor.newInstance(u, _content, retriever);
                } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException e) {
                    ExitHelper.exit(e);
                    // NOTREACHED
                    return null;
                }
            }
        }

        return null;
    }

    private void handleLinkData(final FullFetchedLinkData siteData) {
        if (siteData.dataFileSection().isPresent()) {
            _content = HtmlHelper.slurpFile(siteData.dataFileSection().get());
        }
    }
}
