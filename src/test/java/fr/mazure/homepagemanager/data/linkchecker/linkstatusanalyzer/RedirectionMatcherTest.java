package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

class RedirectionMatcherTest {

    @Test
    void basicSuccessfulMatches() {
        final RedirectionMatcher matcher = new RedirectionMatcher("direct success", Collections.<LinkStatus>emptySet());
        matcher.add("https?://" + RedirectionMatcher.ANY_STRING + "/?", Set.of(Integer.valueOf(200)), RedirectionMatcher.Multiplicity.ONE);
        matcher.compile();
        assertMatch("http://example.com", matcher, true);
    }

    @Test
    void basicSuccessfulDoesNotMatch() {
        final RedirectionMatcher matcher = new RedirectionMatcher("direct failure", Collections.<LinkStatus>emptySet());
        matcher.add("https?://" + RedirectionMatcher.ANY_STRING + "/?", Set.of(Integer.valueOf(500)), RedirectionMatcher.Multiplicity.ONE);
        matcher.compile();
        assertMatch("http://example.com", matcher, false);
    }

    @Test
    void nonExistingSiteMatches() {
        final RedirectionMatcher matcher = new RedirectionMatcher("unanswering server", Collections.<LinkStatus>emptySet());
        final Set<Integer> codes = new HashSet<>();
        codes.add((Integer)null);
        matcher.add("https?://" + RedirectionMatcher.ANY_STRING + "/?", codes, RedirectionMatcher.Multiplicity.ONE);
        matcher.compile();
        assertMatch("http://non.existing.site.com", matcher, true);
    }

    @Test
    void basicRedirectionToEntryPageDoesNotMatch() {
        final RedirectionMatcher matcher = new RedirectionMatcher("redirect success", Collections.<LinkStatus>emptySet());
        matcher.add("https?://" + RedirectionMatcher.ANY_STRING + "/?", Set.of(Integer.valueOf(200)), RedirectionMatcher.Multiplicity.ONE);
        matcher.compile();
        assertMatch("http://www.onlamp.com/pub/a/onlamp/2004/11/11/smrthome_hks1.html", matcher, false);
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
