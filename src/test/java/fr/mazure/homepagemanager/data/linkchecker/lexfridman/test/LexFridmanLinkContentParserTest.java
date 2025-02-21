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
        "https://lexfridman.com/elon-musk|Elon Musk: Tesla Autopilot",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(LexFridmanLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk",
    }, delimiter = '|')
    void testSubtitle(final String url) {
        checkNoSubtitle(LexFridmanLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk|2019-04-12",
    }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(LexFridmanLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk|PT33M",
        "https://lexfridman.com/graham-hancock|PT2H41M33S"
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(LexFridmanLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://lexfridman.com/elon-musk|Elon||Musk|Lex||Fridman",
        "https://lexfridman.com/mark-zuckerberg-3|Mark||Zuckerberg|Lex||Fridman|",
        "https://lexfridman.com/aaron-smith-levin|Aaron||Smith-Levin|Lex||Fridman|",
        "https://lexfridman.com/po-shen-loh|Po-Shen||Loh|Lex||Fridman|",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(LexFridmanLinkContentParser.class,
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
        "https://lexfridman.com/deepseek-dylan-patel-nathan-lambert/|Lex||Fridman",
    }, delimiter = '|')
    void doNotCrashIfNoNameInTitle(final String url,
                                   final String expectedFirstName1,
                                   final String expectedMiddleName1,
                                   final String expectedLastName1) {
        check1Author(LexFridmanLinkContentParser.class,
                     url,
                     // author 1
                     null,
                     expectedFirstName1,
                     expectedMiddleName1,
                     expectedLastName1,
                     null,
                     null);
    }}
