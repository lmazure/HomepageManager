package fr.mazure.homepagemanager.data.dataretriever;

import java.util.function.Consumer;

/**
 * Cached retrieving of site data
 */
public class CachedSiteDataRetriever {

    private final SiteDataPersister _persister;
    private final SynchronousSiteDataRetriever _retriever;

    /**
     * @param persister data persister
     */
    public CachedSiteDataRetriever(final SiteDataPersister persister) {
        _persister = persister;
        _retriever = new SynchronousSiteDataRetriever(persister);
    }

    /**
     * @param url URL of the link to retrieve
     * @param consumer consumer its argument is the site data
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     */
    public void retrieve(final String url,
                         final Consumer<FullFetchedLinkData> consumer,
                         final boolean doNotUseCookies) {

        final FullFetchedLinkData data = _persister.retrieve(url);

        if (data != null) {
            consumer.accept(data);
            return;
        }

        _retriever.retrieve(url, consumer, doNotUseCookies);
    }
}
