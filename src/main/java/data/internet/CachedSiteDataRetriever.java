package data.internet;

import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

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
     * @param maxAge maximum age in seconds
     */
    public void retrieve(final URL url,
                         final Consumer<SiteData> consumer,
                         final long maxAge) {

        final List<Instant> timestamps = _persister.getTimestampList(url);
        
        if (timestamps.size() > 0) {
            
            // call the consumer with the cached data
            final Instant lastTimestamp = timestamps.get(0);
            consumer.accept(_persister.retrieve(url, lastTimestamp));
            
            // stop here is the data is fresh enough
            if (lastTimestamp.isAfter(Instant.now().minusSeconds(maxAge))) {
                return;
            }
        }
        
        _retriever.retrieve(url, consumer);
    }
}
