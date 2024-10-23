package fr.mazure.homepagemanager.data.linkchecker.lexfridman.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.lexfridman.LexFridmanLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of LexFridmanLinkContentParser
 */
class LexFridmanLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk/|Elon Musk: Tesla Autopilot",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(LexFridmanLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk/",
    }, delimiter = '|')
    void testSubtitle(final String url) {
        checkNoSubtitle(LexFridmanLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk/|2019-04-12",
    }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkDate(LexFridmanLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk/|PT32M45S",
        "https://lexfridman.com/graham-hancock|PT2H33M2S"
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(LexFridmanLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk/|Lex||Fridman",
    }, delimiter = '|')
    void test1Author(final String url,
                     final String expectedFirstName,
                     final String expectedMiddleName,
                     final String expectedLastName) {
        check1Author(LexFridmanLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     null);
    }
}
