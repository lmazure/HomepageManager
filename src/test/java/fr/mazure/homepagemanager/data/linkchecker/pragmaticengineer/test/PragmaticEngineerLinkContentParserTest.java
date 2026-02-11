package fr.mazure.homepagemanager.data.linkchecker.pragmaticengineer.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.pragmaticengineer.PragmaticEngineerLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of PragmaticEngineerLinkContentParser
 */
class PragmaticEngineerLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://newsletter.pragmaticengineer.com/p/martin-fowler|How AI will change software engineering \u2013 with Martin Fowler",
        "https://newsletter.pragmaticengineer.com/p/the-third-golden-age-of-software|The third golden age of software engineering \u2013 thanks to AI, with Grady Booch",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(PragmaticEngineerLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://newsletter.pragmaticengineer.com/p/martin-fowler|Martin Fowler breaks down how AI is transforming software architecture and development, from refactoring and deterministic techniques to the timeless principles that still anchor great engineering.",
        "https://newsletter.pragmaticengineer.com/p/the-third-golden-age-of-software|I sit down with Grady Booch to put today’s AI automation claims in historical context and explain why software engineering is entering another golden age, not disappearing.",
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(PragmaticEngineerLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://newsletter.pragmaticengineer.com/p/martin-fowler|2025-11-19",
        "https://newsletter.pragmaticengineer.com/p/the-third-golden-age-of-software|2026-02-04",
    }, delimiter = '|')
    void testPublicationDate(final String url,
                             final String expectedPublicationDate) {
        checkPublicationDate(PragmaticEngineerLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://newsletter.pragmaticengineer.com/p/martin-fowler|Martin||Fowler|Gergely||Orosz",
        "https://newsletter.pragmaticengineer.com/p/the-third-golden-age-of-software|Grady||Booch|Gergely||Orosz",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(PragmaticEngineerLinkContentParser.class,
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
        "https://newsletter.pragmaticengineer.com/p/martin-fowler|https://www.youtube.com/watch?v=CQmI4XKTa0U",
        "https://newsletter.pragmaticengineer.com/p/the-third-golden-age-of-software|https://www.youtube.com/watch?v=OfMAtaocvJw",
    }, delimiter = '|')
    void testOtherLink(final String url,
                       final String expectedOtherLink) {
        checkOtherLink(PragmaticEngineerLinkContentParser.class, url, expectedOtherLink);
    }

    
    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://newsletter.pragmaticengineer.com/p/martin-fowler|PT1H48M53S",
        "https://newsletter.pragmaticengineer.com/p/the-third-golden-age-of-software|PT1H17M5S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(PragmaticEngineerLinkContentParser.class, url, expectedDuration);
    }
}
