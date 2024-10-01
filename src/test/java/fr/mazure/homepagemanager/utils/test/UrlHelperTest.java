package fr.mazure.homepagemanager.utils.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.utils.internet.UrlHelper;

/**
 * Test of UrlHelper class
 */
class UrlHelperTest {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/watch?app=desktop&v=3bTntGMnzfw&list=WL&index=7,https://www.youtube.com/watch?v=3bTntGMnzfw",
        "https://www.youtube.com/watch?v=3bTntGMnzfw,https://www.youtube.com/watch?v=3bTntGMnzfw",
        })
    void typicalYouTubeCleanUp(final String url,
                               final String expectedCleanedUrl) {
        Assertions.assertEquals(expectedCleanedUrl,
                                UrlHelper.removeQueryParameters(url, "app",
                                                                     "list",
                                                                     "index"));
    }
}
