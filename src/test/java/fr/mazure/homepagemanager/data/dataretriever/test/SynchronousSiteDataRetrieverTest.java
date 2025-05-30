package fr.mazure.homepagemanager.data.dataretriever.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;

/**
 * Tests of SynchronousSiteDataRetriever
 */
class SynchronousSiteDataRetrieverTest {

    @Test
    void basicHttpRequest() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("http://example.com",
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               TestHelper.assertExampleComData(d);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void basicHttpsRequest() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://example.com",
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               TestHelper.assertExampleComData(d);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    @Disabled("the site is broken: its certficate is invalid")
    // this site returns an invalid cookie
    void softteam() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://www.softeam.fr",
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               Assertions.assertEquals(301, HttpHelper.getResponseCodeFromHeaders(d.headers().get()));
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        return new SynchronousSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}