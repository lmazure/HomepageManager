package fr.mazure.homepagemanager.data.linkchecker.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.internet.test.TestHelper;

/**
 * Tests to ensure that SynchronousSiteDataRetriever properly handles redirections
 *
 */
public class LinkRedirectionTest {

    @Test
    void youTube() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               Assertions.assertEquals("https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA", d.url());
                               Assertions.assertEquals("https://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2FUC6nSFpj9HTCZ5t-N3Rm3-HA%3Fcbrd%3D1&gl=FR&m=0&pc=yt&cm=2&hl=en&src=1", d.previousRedirection().url());
                               Assertions.assertEquals("https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA?cbrd=1&ucbcb=1", d.previousRedirection().previousRedirection().url());
                               Assertions.assertNull(d.previousRedirection().previousRedirection().previousRedirection());
                               consumerHasBeenCalled.set(true);
                           },
                           true);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
