package fr.mazure.homepagemanager.utils.internet.test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.internet.FeedHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.FeedFormat;

/**
 * Test of FeedHelper class
 */
class FeedHelperTest {

    @ParameterizedTest
    @CsvSource({
        "https://earendil.com/posts/feed.atom,Atom",
        "https://earendil.com/posts/feed.rss,RSS2",
        })
    void testFormatOfRealFeeds(final String url,
                               final String expectedFormatName) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String content = FileHelper.slurpFileSection(d.dataFileSection().get(),
                                                                                  StandardCharsets.UTF_8);
                               Assertions.assertEquals(FeedFormat.valueOf(expectedFormatName),
                                                       FeedHelper.getFormat(content));
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @SuppressWarnings("static-method")
    @Test
    void atomFeedIsProperlyDetected() {
        final String content = "<?xml version='1.0' encoding='utf-8'?>\n" +
                               "<feed xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en\">\n" +
                               "  <title>title</title>\n" +
                               "</feed>";
        Assertions.assertEquals(FeedFormat.Atom, FeedHelper.getFormat(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void rss2FeedIsProperlyDetected() {
        // the RSS 2.0 feed declares the Atom namespace to embed <atom:link>
        final String content = "<?xml version='1.0' encoding='utf-8'?>\n" +
                               "<rss xmlns:atom=\"http://www.w3.org/2005/Atom\" version=\"2.0\">\n" +
                               "  <channel>\n" +
                               "    <title>title</title>\n" +
                               "  </channel>\n" +
                               "</rss>";
        Assertions.assertEquals(FeedFormat.RSS2, FeedHelper.getFormat(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void rss1FeedIsProperlyDetected() {
        final String content = "<?xml version=\"1.0\"?>\n" +
                               "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
                               "         xmlns=\"http://purl.org/rss/1.0/\">\n" +
                               "  <channel>\n" +
                               "    <title>title</title>\n" +
                               "  </channel>\n" +
                               "</rdf:RDF>";
        Assertions.assertEquals(FeedFormat.RSS, FeedHelper.getFormat(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void nonFeedContentIsRejected() {
        Assertions.assertThrows(IllegalArgumentException.class,
                                () -> FeedHelper.getFormat("<html><body>not a feed</body></html>"));
    }
}
