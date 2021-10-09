package data.linkchecker;

import java.net.URL;
import java.nio.file.Path;

import data.linkchecker.quantamagazine.QuantaMagazineLinkDataExtractor;

public class LinkDataExtractorFactory {

    public static LinkDataExtractor build(final Path cacheDirectory,
                                          final URL url) {

        if (url.toString().startsWith("https://www.quantamagazine.org/")) {
            return new QuantaMagazineLinkDataExtractor(url, cacheDirectory);
        }

        return null;
    }
}
