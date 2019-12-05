package data.internet;

import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AsynchronousSiteDataRetriever {

    final private SynchronousSiteDataRetriever _retriever;
    private final ExecutorService _threadPool;
    
    public AsynchronousSiteDataRetriever(final Path cachePath) {
        _retriever = new SynchronousSiteDataRetriever(cachePath);
        _threadPool = Executors.newFixedThreadPool(8);
    }
    
    public void retrieve(final URL url,
                         final Consumer<SiteData> consumer) {
        
        _threadPool.execute(() -> {
            _retriever.retrieve(url, consumer);
        });
    }
}
