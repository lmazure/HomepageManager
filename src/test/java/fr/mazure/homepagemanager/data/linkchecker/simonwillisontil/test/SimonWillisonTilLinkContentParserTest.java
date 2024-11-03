package fr.mazure.homepagemanager.data.linkchecker.simonwillisontil.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.simonwillisontil.SimonWillisonTilLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 *
 */
class SimonWillisonTilLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://til.simonwillison.net/google/gmail-compose-url,Simon,Willison,",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        check1Author(SimonWillisonTilLinkContentParser.class, url, null, expectedFirstName, null, expectedLastName, null, expectedGivenName);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://til.simonwillison.net/google/gmail-compose-url|Generating URLs to a Gmail compose window",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(SimonWillisonTilLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://til.simonwillison.net/google/gmail-compose-url",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(SimonWillisonTilLinkContentParser.class, url);

    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://til.simonwillison.net/google/gmail-compose-url,2024-03-12",
        })
    void testDate(final String url,
                  final String expectedPublicationDate) {
        checkDate(SimonWillisonTilLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://til.simonwillison.net/google/gmail-compose-url|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(SimonWillisonTilLinkContentParser.class, url, expectedLanguage);
    }
}
