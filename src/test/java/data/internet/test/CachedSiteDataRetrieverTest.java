package data.internet.test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.CachedSiteDataRetriever;
import data.internet.SiteData;
import data.internet.SiteDataPersister;
import utils.FileHelper;

public class CachedSiteDataRetrieverTest {

    @Test
    void properlyCached() {
        
        final CachedSiteDataRetriever retriever = buildDataSiteRetriever();
        final URL url = TestHelper.buildURL("http://example.com");
        
        // the first retrieval must not use the cache
        final AtomicBoolean firstConsumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (SiteData d) -> {
                               Assertions.assertFalse(firstConsumerHasBeenCalled.get());
                               firstConsumerHasBeenCalled.set(true);
                               TestHelper.assertData(d);
                           },
                           3600);
        Assertions.assertFalse(firstConsumerHasBeenCalled.get());
        while (!firstConsumerHasBeenCalled.get()) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }

        // the second retrieval must use the cache and not call twice
        final AtomicBoolean secondConsumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (SiteData d) -> {
                               Assertions.assertFalse(secondConsumerHasBeenCalled.get());
                               secondConsumerHasBeenCalled.set(true);
                               TestHelper.assertData(d);
                           },
                           3600);
        Assertions.assertTrue(secondConsumerHasBeenCalled.get());

        
        // the third retrieval must use the cache and call twice
        final AtomicBoolean thirdConsumerHasBeenCalledOnce = new AtomicBoolean(false);
        final AtomicBoolean thirdConsumerHasBeenCalledTwice = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (SiteData d) -> {
                               if (thirdConsumerHasBeenCalledOnce.get()) {
                                   Assertions.assertFalse(thirdConsumerHasBeenCalledTwice.get());
                                   thirdConsumerHasBeenCalledTwice.set(true);
                               } else {
                                   thirdConsumerHasBeenCalledOnce.set(true);
                               }
                               TestHelper.assertData(d);
                           },
                           0);
        Assertions.assertTrue(thirdConsumerHasBeenCalledOnce.get());
        Assertions.assertFalse(thirdConsumerHasBeenCalledTwice.get());
        while (!thirdConsumerHasBeenCalledTwice.get()) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }
    }

    private CachedSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\test\\CachedSiteDataRetrieverTest");
        FileHelper.deleteDirectory(cachePath.toFile());
        return new CachedSiteDataRetriever(new SiteDataPersister(cachePath));
    }
}
