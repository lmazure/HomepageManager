package fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of DwarkeshPodcastLinkContentParser
 */
class DwarkeshPodcastLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/richard-sutton\u00a7Richard Sutton \u2013 Father of RL thinks LLMs are a dead end",
        "https://www.dwarkesh.com/p/elon-musk\u00a7Elon Musk \u2014 \"In 36 months, the cheapest place to put AI will be space\u201d",
    }, delimiter = '\u00a7')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(DwarkeshPodcastLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/richard-sutton§Watch now (66 mins) | LLMs aren\u2019t Bitter-Lesson-pilled",
        "https://www.dwarkesh.com/p/elon-musk§\u201cThose who live in software land are about to have a hard lesson in hardware.\u201d",
    }, delimiter = '§')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(DwarkeshPodcastLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/richard-sutton|Richard||Sutton|Dwarkesh||Patel",
        "https://www.dwarkesh.com/p/elon-musk|Elon||Musk|Dwarkesh||Patel",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(DwarkeshPodcastLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
                      // author 2
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/richard-sutton|https://www.youtube.com/watch?v=21EYKqUsPfg",
        "https://www.dwarkesh.com/p/elon-musk|https://www.youtube.com/watch?v=BYXbuik3dgA",
    }, delimiter = '|')
    void testOtherLink(final String url,
                       final String expectedOtherLink) {
        checkOtherLink(DwarkeshPodcastLinkContentParser.class, url, expectedOtherLink);
    }
}
