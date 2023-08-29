package fr.mazure.homepagemanager.data.dataretriever;

import java.nio.file.Path;
import java.util.function.BiConsumer;

/**
 * Facade class for retrieving site data
 */
public class SiteDataRetriever {

    private final CachedSiteDataRetriever _retriever;
    private final SiteDataPersister _persister;

    /**
     * @param path directory where the persistence files should be written
     */
    public SiteDataRetriever(final Path path) {
        _persister = new SiteDataPersister(path);
        _retriever = new CachedSiteDataRetriever(_persister);
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

        _retriever.retrieve(url, consumer, maxAge, doNotUseCookies);
    }
}
