package fr.mazure.homepagemanager.utils.internet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.utils.internet.YouTubeHelper;

/**
 * Tests of the YouTubeHelper class
 */
class YouTubeHelperTest {

    @ParameterizedTest
    @CsvSource(value = {
        "Oxide Computer Company|Querying Metrics with OxQL|https://www.youtube.com/watch?v=RTsXM3kcAaI",
    }, delimiter = '|')
    void testVideoUrlRetrieval(final String channelName,
                               final String videoTitle,
                               final String expectedUrl) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final YouTubeHelper helper = new YouTubeHelper();
        final Optional<String> url = helper.getVideoURL(channelName, videoTitle, retriever);
        assertEquals(expectedUrl, url.get());
    }
}
