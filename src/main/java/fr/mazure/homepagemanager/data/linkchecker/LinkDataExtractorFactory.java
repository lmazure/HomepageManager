package fr.mazure.homepagemanager.data.linkchecker;

import java.nio.file.Path;
import java.util.regex.Pattern;

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
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;

/**
 * Factory returning the LinkDataExtractor able to extract data from a given URL
 */
public class LinkDataExtractorFactory {

    private String _content;

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

        ThrowingLinkDataExtractor constructor = null;
        final Pattern mediumUrl = Pattern.compile("https://(.+\\.)?medium.com/.+");

        if (u.startsWith("https://arstechnica.com/")) {
            constructor = ArsTechnicaLinkContentParser::new;
        } else if (u.startsWith("https://www.baeldung.com/") &&
                   !u.equals("https://www.baeldung.com/")) {
            constructor = BaeldungLinkContentParser::new;
        } else if (url.startsWith("https://github.blog/")) {
            constructor = GithubBlogLinkContentParser::new;
        } else if (mediumUrl.matcher(u).matches()) {
            constructor = MediumLinkContentParser::new;
        } else if (u.matches("https://blogs.oracle.com/javamagazine/.+") ||
                   u.matches("https://blogs.oracle.com/java/.+")) {
            constructor = OracleBlogsLinkContentParser::new;
        } else if (u.startsWith("https://www.quantamagazine.org/")) {
            constructor = QuantaMagazineLinkContentParser::new;
        } else if (u.startsWith("https://stackoverflow.blog/")) {
            constructor = StackOverflowBlogContentParser::new;
        } else if (u.startsWith("https://spectrum.ieee.org/")) {
            constructor = SpectrumLinkContentParser::new;
        } else if (u.startsWith("https://www.youtube.com/watch?")) {
            constructor = YoutubeWatchLinkContentParser::new;
        } else if (u.startsWith("https://about.gitlab.com/blog/") && !u.equals("https://about.gitlab.com/blog/")) {
            constructor = GitlabBlogLinkContentParser::new;
        } else if (u.startsWith("https://www.wired.com/")) {
            constructor = WiredLinkContentParser::new;
        }

        if (constructor == null) {
            return null;
        }

        final SiteDataPersister persister = new SiteDataPersister(cacheDirectory);
        final SynchronousSiteDataRetriever retriever = new SynchronousSiteDataRetriever(persister);
        retriever.retrieve(url, this::handleLinkData, false);

        return constructor.apply(u, _content);
    }

    private void handleLinkData(@SuppressWarnings("unused") final Boolean isDataFresh,
                                final FullFetchedLinkData siteData) {
        if (siteData.dataFileSection().isPresent()) {
            _content = HtmlHelper.slurpFile(siteData.dataFileSection().get());
        }
    }

    @FunctionalInterface
    private interface ThrowingLinkDataExtractor {
        LinkDataExtractor apply(final String url, final String data) throws ContentParserException;
    }
}
