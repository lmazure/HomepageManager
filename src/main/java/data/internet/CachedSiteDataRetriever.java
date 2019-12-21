package data.internet;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Cached retrieving of site data
 */
public class CachedSiteDataRetriever {

    private final SiteDataPersister _persister;
    private final AsynchronousSiteDataRetriever _retriever;

    public CachedSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _retriever = new AsynchronousSiteDataRetriever(persister);
    }
    
    /**
     * @param url
     * @param consumer
     * its first argument is true is the data is fresh
     * (if the data is not fresh, it will be called a second time with fresh data)
     * its second argument is the site data
     * @param maxAge maximum age in seconds
     */
    public void retrieve(final URL url,
                         final BiConsumer<Boolean, SiteData> consumer,
                         final long maxAge) {

        final List<Instant> timestamps = _persister.getTimestampList(url);
        
        if (timestamps.size() > 0) {
            
            final Instant lastTimestamp = timestamps.get(0);
            final boolean isDataFresh = lastTimestamp.isAfter(Instant.now().minusSeconds(maxAge));

            // call the consumer with the cached data
            consumer.accept(Boolean.valueOf(isDataFresh), _persister.retrieve(url, lastTimestamp));
            
            // stop here is the data is fresh enough
            if (isDataFresh) {
                return;
            }
        }
        
        _retriever.retrieve(url, consumer);
    }
}
