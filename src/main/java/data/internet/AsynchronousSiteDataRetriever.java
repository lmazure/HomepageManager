package data.internet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Asynchronous retrieving of site data
 */
public class AsynchronousSiteDataRetriever {

    private final SynchronousSiteDataRetriever _retriever;
    private static final int s_nb_threads = 32;
    private static final ExecutorService s_threadPool = Executors.newFixedThreadPool(s_nb_threads);

    /**
     * @param persister data persister
     */
    public AsynchronousSiteDataRetriever(final SiteDataPersister persister) {
        _retriever = new SynchronousSiteDataRetriever(persister);
    }

    /**
     * @param url URL of the link to retrieve
     * @param consumer
     *   - its first argument is always true since the data is always fresh
     *   - its second argument is the site data
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     */
    public void retrieve(final String url,
                         final BiConsumer<Boolean, FullFetchedLinkData> consumer,
                         final boolean doNotUseCookies) {

        s_threadPool.execute(() -> _retriever.retrieve(url, consumer, doNotUseCookies));
    }
}
