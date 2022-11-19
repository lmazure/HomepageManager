package data.internet.test;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import utils.FileHelper;

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
                           (final Boolean b, final SiteData d) -> {
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
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertFalse(consumerHasBeenCalled.get());
                               consumerHasBeenCalled.set(true);
                               Assertions.assertTrue(b.booleanValue());
                               TestHelper.assertData(d);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Disabled("I have not found yet a workaround for LinkedIn protection")
    @Test
    void linkedInRequest() {

        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        retriever.retrieve("https://www.linkedin.com/in/thomas-cabaret-36766674/",
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertEquals(200, d.getHttpCode().get().intValue());
                           },
                           false);
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = TestHelper.getTestDatapath(getClass());
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SynchronousSiteDataRetriever(new SiteDataPersister(cachePath));
    }
}