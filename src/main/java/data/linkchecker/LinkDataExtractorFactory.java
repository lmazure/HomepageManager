package data.linkchecker;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;
import utils.ExitHelper;

public class LinkDataExtractorFactory {

    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final URL url) {

        final URL u = cleanUrl(url);

        if (u.toString().startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkDataExtractor(u, cacheDirectory);
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
