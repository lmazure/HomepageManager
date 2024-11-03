package fr.mazure.homepagemanager.data.linkchecker.simonwillison.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.simonwillison.SimonWillisonLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of SimonWillisonLinkContentParser
 */
class SimonWillisonLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "hhttps://simonwillison.net/2024/Mar/8/gpt-4-barrier/,Simon,Willison,",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        check1Author(SimonWillisonLinkContentParser.class, url, null, expectedFirstName, null, expectedLastName, null, expectedGivenName);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://simonwillison.net/2024/Mar/8/gpt-4-barrier/|The GPT-4 barrier has finally been broken",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(SimonWillisonLinkContentParser.class, url, expectedTitle);

    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://simonwillison.net/2024/Mar/8/gpt-4-barrier/",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(SimonWillisonLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://simonwillison.net/2024/Mar/8/gpt-4-barrier/,2024-03-08",
        "https://simonwillison.net/2024/Mar/22/claude-and-chatgpt-case-study/,2024-03-22",
        })
    void testDate(final String url,
                  final String expectedPublicationDate) {
        checkDate(SimonWillisonLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://simonwillison.net/2024/Mar/8/gpt-4-barrier/|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(SimonWillisonLinkContentParser.class, url, expectedLanguage);
    }
}
