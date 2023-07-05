package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.internet.test.TestHelper;

class RedirectionMatcherTest {

    @Test
    void basicSuccessfulMatch() {
        final RedirectionMatcher matcher = new RedirectionMatcher();
        matcher.add(".*", Set.of(Integer.valueOf(200)), RedirectionMatcherElement.Multiplicity.ONE);
        matcher.compile();
        assertMatch("http://example.com", matcher, true);
    }

    @Test
    void basicSuccessfulNonMatch() {
        final RedirectionMatcher matcher = new RedirectionMatcher();
        matcher.add(".*", Set.of(Integer.valueOf(500)), RedirectionMatcherElement.Multiplicity.ONE);
        matcher.compile();
        assertMatch("http://example.com", matcher, false);
    }

    private void assertMatch(final String url,
                             final RedirectionMatcher matcher,
                             final boolean expectedMatch) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                (final Boolean b, final FullFetchedLinkData d) -> {
                    consumerHasBeenCalled.set(true);
                    if (expectedMatch) {
                        Assertions.assertTrue(matcher.doesRedirectionMatch(d));                        
                    } else {
                        Assertions.assertFalse(matcher.doesRedirectionMatch(d));
                    }
                },
                false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        return new SynchronousSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}
