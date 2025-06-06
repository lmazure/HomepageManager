package fr.mazure.homepagemanager.data.dataretriever.test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of CachedSiteDataRetriever
 */
class CachedSiteDataRetrieverTest {

    @Test
    @Disabled // TODO !!! needs to be re-instanciated
    void properlyCached() {

        final CachedSiteDataRetriever retriever = buildDataSiteRetriever();
        final String url = "http://example.com";

        // the first retrieval must not use the cache
        final AtomicBoolean firstConsumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(firstConsumerHasBeenCalled.get());
                               firstConsumerHasBeenCalled.set(true);
                               TestHelper.assertExampleComData(d);
                           },
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
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(secondConsumerHasBeenCalled.get());
                               secondConsumerHasBeenCalled.set(true);
                               TestHelper.assertExampleComData(d);
                           },
                           false);
        Assertions.assertTrue(secondConsumerHasBeenCalled.get());

        // the third retrieval must use the cache and call twice
        final AtomicBoolean thirdConsumerHasBeenCalledOnce = new AtomicBoolean(false);
        final AtomicBoolean thirdConsumerHasBeenCalledTwice = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               if (thirdConsumerHasBeenCalledOnce.get()) {
                                   Assertions.assertFalse(thirdConsumerHasBeenCalledTwice.get());
                                   thirdConsumerHasBeenCalledTwice.set(true);
                               } else {
                                   thirdConsumerHasBeenCalledOnce.set(true);
                               }
                               TestHelper.assertExampleComData(d);
                           },
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

    @Test
    @Disabled
    void testSimultaneousRetrieval() {
        final int nbThread = 200;
        final CachedSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicInteger numberOfConsumerCalls = new AtomicInteger();
        final String url = "https://www.baeldung.com/crawler4j";
        for (int i = 0; i < nbThread; i++) {
            retriever.retrieve(url,
                    (final FullFetchedLinkData d) -> {
                        Assertions.assertTrue(d.dataFileSection().isPresent());
                        final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                        try {
                            final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data, retriever);
                            Assertions.assertEquals("A Guide to Crawler4j", parser.getTitle());
                        } catch (final ContentParserException e) {
                            Assertions.fail("BaeldungLinkContentParser construvtor threw " + e.getMessage());
                        }
                        numberOfConsumerCalls.addAndGet(1);
                    },
                    false);
        }
        while (numberOfConsumerCalls.get() != nbThread) {
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
