package fr.mazure.homepagemanager.data.linkchecker.arminronacher.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.arminronacher.ArminRonacherLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of ArminRonacherLinkContentParser
 */
class ArminRonacherLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lucumr.pocoo.org/2026/8/19/what-is-reasoning/|Armin|Ronacher|",
        "https://lucumr.pocoo.org/2026/9/14/interpreting-pangram/|Armin|Ronacher|",
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        check1Author(ArminRonacherLinkContentParser.class, url, null, expectedFirstName, null, expectedLastName, null, expectedGivenName);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lucumr.pocoo.org/2026/8/19/what-is-reasoning/|What Is Reasoning",
        "https://lucumr.pocoo.org/2026/9/14/interpreting-pangram/|Interpreting Pangram",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(ArminRonacherLinkContentParser.class, url, expectedTitle);

    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://lucumr.pocoo.org/2026/8/19/what-is-reasoning/",
            "https://lucumr.pocoo.org/2026/9/14/interpreting-pangram/",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(ArminRonacherLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lucumr.pocoo.org/2026/8/19/what-is-reasoning/|2026-08-19",
        "https://lucumr.pocoo.org/2026/9/14/interpreting-pangram/|2026-09-14",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedPublicationDate) {
        checkCreationDate(ArminRonacherLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lucumr.pocoo.org/2026/8/19/what-is-reasoning/|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(ArminRonacherLinkContentParser.class, url, expectedLanguage);
    }
}
