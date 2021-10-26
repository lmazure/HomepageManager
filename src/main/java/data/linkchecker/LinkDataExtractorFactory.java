package data.linkchecker;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;
import data.linkchecker.youtubewatch.YoutubeWatchLinkDataExtractor;
import utils.ExitHelper;

public class LinkDataExtractorFactory {

    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final URL url) throws ContentParserException {

        final URL u = cleanUrl(url);

        if (u.toString().startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkDataExtractor(u, cacheDirectory);
        }

        if (url.toString().startsWith("https://www.youtube.com/watch?v=")) {
            return new YoutubeWatchLinkDataExtractor(u, cacheDirectory);
        }

        return null;
    }

    private static URL cleanUrl(URL url) {

        final String initialUrl = url.toString();

        final String cleanedUrl = initialUrl.replaceAll("/?utm_[^/]+$", "");

        try {
            return new URL(cleanedUrl);
        } catch (final MalformedURLException e) {
            ExitHelper.exit(e);
            // NOT REACHED
            return null;
        }
    }
}
