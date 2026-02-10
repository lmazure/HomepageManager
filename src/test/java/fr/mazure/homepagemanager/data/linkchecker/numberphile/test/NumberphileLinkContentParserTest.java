package fr.mazure.homepagemanager.data.linkchecker.numberphile.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.numberphile.NumberphileLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests for NumberphileLinkContentParser
 */
class NumberphileLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.numberphile.com/videos/sashko-olenchenko-podcast|PODCAST: Making Math Videos in Ukraine - with Sashko Olenchenko|Making Math Videos in Ukraine",
        "https://www.numberphile.com/podcast/2018/11/21/fermats-last-theorem-with-ken-ribet|Fermat’s Last Theorem - with Ken Ribet",
        "https://www.numberphile.com/podcast/ron-graham-tribute|The Mathematical Showman - Ron Graham (1935-2020)|PODCAST: The Mathematical Showman - Ron Graham (1935-2020)"
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(NumberphileLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.numberphile.com/videos/sashko-olenchenko-podcast",
        "https://www.numberphile.com/podcast/2018/11/21/fermats-last-theorem-with-ken-ribet",
        "https://www.numberphile.com/podcast/ron-graham-tribute"
    })
    void testSubtitle(final String url) {
        checkNoSubtitle(NumberphileLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.numberphile.com/videos/sashko-olenchenko-podcast|2025-11-09",
        "https://www.numberphile.com/podcast/2018/11/21/fermats-last-theorem-with-ken-ribet|2018-11-22",
        "https://www.numberphile.com/podcast/ron-graham-tribute|2020-07-13"
    }, delimiter = '|')
    void testPublicationDate(final String url,
                             final String expectedDate) {
        checkPublicationDate(NumberphileLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.numberphile.com/videos/sashko-olenchenko-podcast|Sashko|Olenchenko",
        "https://www.numberphile.com/podcast/2018/11/21/fermats-last-theorem-with-ken-ribet|Ken|Ribet"
    }, delimiter = '|')
    void testTwoAuthors(final String url,
                        final String expectedGuestFirstName,
                        final String expectedGuestLastName) {
        check2Authors(NumberphileLinkContentParser.class,
                      url,
                      null,
                      expectedGuestFirstName,
                      null,
                      expectedGuestLastName,
                      null,
                      null,
                      null,
                      "Brady",
                      null,
                      "Haran",
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = { "https://www.numberphile.com/podcast/ron-graham-tribute" })
    void testOneAuthor(final String url) {
        check1Author(NumberphileLinkContentParser.class,
                     url,
                     null,
                     "Brady",
                     null,
                     "Haran",
                     null,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.numberphile.com/videos/sashko-olenchenko-podcast|PT50M49S",
        "https://www.numberphile.com/podcast/2018/11/21/fermats-last-theorem-with-ken-ribet|PT48M22S",
        "https://www.numberphile.com/podcast/ron-graham-tribute|PT39M2S"
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(NumberphileLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.numberphile.com/videos/sashko-olenchenko-podcast|https://www.youtube.com/watch?v=Nd813Dg93Bs",
        "https://www.numberphile.com/podcast/2018/11/21/fermats-last-theorem-with-ken-ribet|https://www.youtube.com/watch?v=NPOw4iIxN6o",
        "https://www.numberphile.com/podcast/ron-graham-tribute|https://www.youtube.com/watch?v=pT07GtfpsDI",
    }, delimiter = '|')
    void testOtherLink(final String url,
                       final String expectedOtherLink) {
        checkOtherLink(NumberphileLinkContentParser.class, url, expectedOtherLink);
    }
}
