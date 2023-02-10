package fr.mazure.homepagemanager.data.internet;

import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Cached retrieving of site data
 */
public class CachedSiteDataRetriever {

    private final SiteDataPersister _persister;
    private final AsynchronousSiteDataRetriever _retriever;

    /**
     * @param persister data persister
     */
    public CachedSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _retriever = new AsynchronousSiteDataRetriever(persister);
    }

    /**
     * @param url URL of the link to retrieve
     * @param consumer
     *   - its first argument is true is the data is fresh
     *     (if the data is not fresh, it will be called a second time with fresh data)
     *   - its second argument is the site data
     * @param maxAge maximum age in seconds
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     */
    public void retrieve(final String url,
                         final BiConsumer<Boolean, FullFetchedLinkData> consumer,
                         final long maxAge,
                         final boolean doNotUseCookies) {

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

        _retriever.retrieve(url, consumer, doNotUseCookies);
    }
}
