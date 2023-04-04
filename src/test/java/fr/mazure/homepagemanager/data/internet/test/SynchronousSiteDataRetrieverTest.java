package fr.mazure.homepagemanager.data.internet.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;

/**
 * Tests of SynchronousSiteDataRetriever
 *
 */
public class SynchronousSiteDataRetrieverTest {

    @Test
    void basicHttpRequest() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("http://example.com",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               TestHelper.assertData(d);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void basicHttpsRequest() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://example.com",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               TestHelper.assertData(d);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    // this site returns an invalid cookie
    void softteam() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://www.softeam.fr",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               Assertions.assertEquals(301, HttpHelper.getResponseCodeFromHeaders(d.headers().get()));
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Disabled("https://www.lmet.fr is currently down")
    @Test
    // this site times out and returns an empty response
    void leMondeEnTique() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://www.lmet.fr/GSWeb/lmet.gswa",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               Assertions.assertEquals("No header", d.error().get());
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Disabled("I have not found yet a workaround for LinkedIn protection")
    @Test
    void linkedInRequest() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        retriever.retrieve("https://www.linkedin.com/in/thomas-cabaret-36766674/",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertEquals(200, HttpHelper.getResponseCodeFromHeaders(d.headers().get()));
                           },
                           false);
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        return new SynchronousSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}