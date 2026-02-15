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
        "https://www.dwarkesh.com/p/richard-sutton|Richard Sutton – Father of RL thinks LLMs are a dead end",
        "https://www.dwarkesh.com/p/elon-musk|Elon Musk — \"In 36 months, the cheapest place to put AI will be space\u201d",
        "https://www.dwarkesh.com/p/thoughts-on-ai-progress-dec-2025-video|An audio version of my blog post, Thoughts on AI progress (Dec 2025)",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(DwarkeshPodcastLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/richard-sutton|LLMs aren’t Bitter-Lesson-pilled",
        "https://www.dwarkesh.com/p/elon-musk|“Those who live in software land are about to have a hard lesson in hardware.”",
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(DwarkeshPodcastLinkContentParser.class, url, expectedSubtitle);
    }


    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/thoughts-on-ai-progress-dec-2025-video",
    }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(DwarkeshPodcastLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.dwarkesh.com/p/richard-sutton|2025-09-26",
            "https://www.dwarkesh.com/p/elon-musk|2026-02-05",
    }, delimiter = '|')
    void testPublicationDate(final String url,
                             final String expectedPublicationDate) {
        checkPublicationDate(DwarkeshPodcastLinkContentParser.class, url, expectedPublicationDate);
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

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.dwarkesh.com/p/richard-sutton|PT1H6M22S",
        "https://www.dwarkesh.com/p/elon-musk|PT2H49M45S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(DwarkeshPodcastLinkContentParser.class, url, expectedDuration);
    }
}
