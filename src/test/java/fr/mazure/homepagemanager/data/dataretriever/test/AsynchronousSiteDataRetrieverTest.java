package fr.mazure.homepagemanager.data.dataretriever.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.dataretriever.AsynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;

/**
 * Tests of AsynchronousSiteDataRetriever
 */
class AsynchronousSiteDataRetrieverTest {

    @Test
    void basicHttpRequest() {

        final AsynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("http://example.com",
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               TestHelper.assertExampleComData(d);
                           },
                           false);
        Assertions.assertFalse(consumerHasBeenCalled.get());
        while (!consumerHasBeenCalled.get()) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }
    }

    @Test
    void basicHttpsRequest() {

        final AsynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://example.com",
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               TestHelper.assertExampleComData(d);
                           },
                           false);
        Assertions.assertFalse(consumerHasBeenCalled.get());
        while (!consumerHasBeenCalled.get()) {
            try {
                Thread.sleep(100);
            } catch (final InterruptedException e) {
                e.printStackTrace();
                Assertions.fail();
            }
        }
    }

    private AsynchronousSiteDataRetriever buildDataSiteRetriever() {
        return new AsynchronousSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}
