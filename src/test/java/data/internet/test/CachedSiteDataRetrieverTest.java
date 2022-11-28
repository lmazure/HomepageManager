package data.internet.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.CachedSiteDataRetriever;
import data.internet.FullFetchedLinkData;

/**
 * Tests of CachedSiteDataRetriever
 *
 */
public class CachedSiteDataRetrieverTest {

    @Test
    void properlyCached() {

        final CachedSiteDataRetriever retriever = buildDataSiteRetriever();
        final String url = "http://example.com";

        // the first retrieval must not use the cache
        final AtomicBoolean firstConsumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(firstConsumerHasBeenCalled.get());
                               firstConsumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               TestHelper.assertData(d);
                           },
                           3600,
                           false);
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
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(secondConsumerHasBeenCalled.get());
                               secondConsumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               TestHelper.assertData(d);
                           },
                           3600,
                           false);
        Assertions.assertTrue(secondConsumerHasBeenCalled.get());

        // the third retrieval must use the cache and call twice
        final AtomicBoolean thirdConsumerHasBeenCalledOnce = new AtomicBoolean(false);
        final AtomicBoolean thirdConsumerHasBeenCalledTwice = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               if (thirdConsumerHasBeenCalledOnce.get()) {
                                   Assertions.assertFalse(thirdConsumerHasBeenCalledTwice.get());
                                   thirdConsumerHasBeenCalledTwice.set(true);
                                   Assertions.assertTrue(b.booleanValue());
                               } else {
                                   thirdConsumerHasBeenCalledOnce.set(true);
                                   Assertions.assertFalse(b.booleanValue());
                               }
                               TestHelper.assertData(d);
                           },
                           0,
                           false);
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
        return new CachedSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}
