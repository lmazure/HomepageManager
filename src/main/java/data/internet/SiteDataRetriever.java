package data.internet;

import java.net.URL;
import java.nio.file.Path;
import java.util.function.BiConsumer;

/**
 * Facade class for retrieving site data
 */
public class SiteDataRetriever {

    private final CachedSiteDataRetriever _retriever;
    private final SiteDataPersister _persister;

    /**
     * @param url
     * @param consumer
     * its first argument is true is the data is fresh
     * (if the data is not fresh, it will be called a second time with fresh data)
     * its second argument is the site data
     * @param maxAge maximum age in seconds
     */
    public SiteDataRetriever(final Path cachePath) {
        _persister = new SiteDataPersister(cachePath);
        _retriever = new CachedSiteDataRetriever(_persister);
    }

    /**
     * @param url
     * @param consumer
     * @param maxAge maximum age in seconds
     */
    public void retrieve(final URL url,
                         final BiConsumer<Boolean, SiteData> consumer,
                         final long maxAge) {

        _retriever.retrieve(url, consumer, maxAge);
    }
}
