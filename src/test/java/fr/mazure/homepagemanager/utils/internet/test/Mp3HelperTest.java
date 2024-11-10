package fr.mazure.homepagemanager.utils.internet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.utils.internet.Mp3Helper;

/**
 * Tests of Mp3Helper class
 */
class Mp3HelperTest extends Mp3Helper {


    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://samplelib.com/lib/preview/mp3/sample-12s.mp3|PT12S",
        "https://content.blubrry.com/takeituneasy/lex_ai_jimmy_wales.mp3?_=1|PT3H19M37S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expecteDuration) {
        final Mp3Helper helper = new Mp3Helper();
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(Mp3HelperTest.class);
        assertEquals(Duration.parse(expecteDuration), helper.getMp3Duration(url, retriever));
    }
}
