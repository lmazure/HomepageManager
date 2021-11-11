package data.linkchecker;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import data.linkchecker.arstechnica.ArsTechnicaLinkDataExtractor;
import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;
import data.linkchecker.youtubewatch.YoutubeWatchLinkDataExtractor;
import utils.ExitHelper;
import utils.StringHelper;

public class LinkDataExtractorFactory {

    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final URL url) throws ContentParserException {

        final URL u = cleanUrl(url);

        if (url.toString().startsWith("https://arstechnica.com/")) {
            return new ArsTechnicaLinkDataExtractor(u, cacheDirectory);
        }

        if (u.toString().startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkDataExtractor(u, cacheDirectory);
        }

        if (url.toString().startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkDataExtractor(u, cacheDirectory);
        }

        return null;
    }

    private static URL cleanUrl(final URL url) {

        final String initialUrl = url.toString();

        final String cleanedUrl = initialUrl.replaceAll("/?utm_[^/]+$", "");

        return StringHelper.convertStringToUrl(cleanedUrl);
    }
}
