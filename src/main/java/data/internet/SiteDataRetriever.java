package data.internet;

import java.nio.file.Path;
import java.util.function.BiConsumer;

/**
 * Facade class for retrieving site data
 */
public class SiteDataRetriever {

    private final CachedSiteDataRetriever _retriever;
    private final SiteDataPersister _persister;

    /**
     *
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
    public void retrieve(final String url,
                         final BiConsumer<Boolean, SiteData> consumer,
                         final long maxAge) {

        _retriever.retrieve(url, consumer, maxAge);
    }
}
