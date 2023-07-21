package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.internet.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.RedirectionData.Match;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

class RedirectionDataTest {

    @Test
    void youtubeChannel() {
        assertMatch("https://www.youtube.com/channel/UCUHW94eEFW7hkUMVaZz4eDg", "from Google channel to cookies configuration", Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }

    private void assertMatch(final String url,
                             final String expectedMatcherName,
                             final Set<LinkStatus> expectedStatuses) {
        final RedirectionData data = new RedirectionData();
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                (final Boolean b, final FullFetchedLinkData d) -> {
                    consumerHasBeenCalled.set(true);
                    final Match effectiveStatuses = data.getMatch(d);
                    assertEquals(expectedMatcherName, effectiveStatuses.name());
                    assertEquals(expectedStatuses, effectiveStatuses.statuses());
                },
                false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        return new SynchronousSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}
