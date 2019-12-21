package data.internet;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Asynchronous retrieving of site data
 */
public class AsynchronousSiteDataRetriever {

    final private SynchronousSiteDataRetriever _retriever;
    private final ExecutorService _threadPool;
    
    public AsynchronousSiteDataRetriever(final SiteDataPersister persister) {
        _retriever = new SynchronousSiteDataRetriever(persister);
        _threadPool = Executors.newFixedThreadPool(8);
    }
    
    /**
     * @param url
     * @param consumer
     * its first argument is always true since the data is always fresh
     * its second argument is the site data
     * @param maxAge maximum age in seconds
     */
    public void retrieve(final URL url,
                         final BiConsumer<Boolean, SiteData> consumer) {
        
        _threadPool.execute(() -> {
            _retriever.retrieve(url, consumer);
        });
    }
}
