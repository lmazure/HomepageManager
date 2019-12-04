package data.internet.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.AsynchronousSiteDataRetriever;
import data.internet.SiteData;

public class AsynchronousSiteDataRetrieverTest {

    @Test
    void basicHttpRequest() {
        
        final AsynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("http://example.com"),
                           (SiteData d) -> {
                               consumerHasBeenCalled.set(true);
                               TestHelper.assertData(d);
                           });
        Assertions.assertFalse(consumerHasBeenCalled.get());
        while (!consumerHasBeenCalled.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }
    }

    @Test
    void basicHttpsRequest() {
        
        final AsynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://example.com"),
                           (SiteData d) -> {
                               consumerHasBeenCalled.set(true);
                               TestHelper.assertData(d);
                           });
        Assertions.assertFalse(consumerHasBeenCalled.get());
        while (!consumerHasBeenCalled.get()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }
    }

    private AsynchronousSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\SynchronousSiteDataRetrieverTest");
        return new AsynchronousSiteDataRetriever(cachePath);
    }
}
