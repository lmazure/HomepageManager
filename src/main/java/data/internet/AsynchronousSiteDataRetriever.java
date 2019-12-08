package data.internet;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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
    
    public void retrieve(final URL url,
                         final Consumer<SiteData> consumer) {
        
        _threadPool.execute(() -> {
            _retriever.retrieve(url, consumer);
        });
    }
}
