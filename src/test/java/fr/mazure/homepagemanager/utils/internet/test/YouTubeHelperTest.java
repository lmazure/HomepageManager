package fr.mazure.homepagemanager.utils.internet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.utils.internet.YouTubeHelper;

/**
 * Test of the YouTubeHelper class
 */
class YouTubeHelperTest {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "Oxide Computer Company|Querying Metrics with OxQL|https://www.youtube.com/watch?v=RTsXM3kcAaI",
    }, delimiter = '|')
    void testVideoUrlRetrieval(final String channelName, final String videoTitle, final String expectedUrl) {
		Optional<String> url = null;
		try {
			url = YouTubeHelper.getVideoURL(channelName, videoTitle);
			assertEquals(expectedUrl, url.get());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
