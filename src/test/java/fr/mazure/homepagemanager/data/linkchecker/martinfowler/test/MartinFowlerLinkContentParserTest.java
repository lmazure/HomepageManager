package fr.mazure.homepagemanager.data.linkchecker.martinfowler.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.martinfowler.MartinFowlerLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of MartinFowlerLinkContentParser
 */
class MartinFowlerLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://martinfowler.com/articles/refactoring-2nd-ed.html|The Second Edition of “Refactoring”",
        "https://martinfowler.com/articles/mocksArentStubs.html|Mocks Aren't Stubs",
        "https://martinfowler.com/articles/exploring-gen-ai/to-vibe-or-not-vibe.html|To vibe or not to vibe",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(MartinFowlerLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://martinfowler.com/articles/refactoring-2nd-ed.html",
            "https://martinfowler.com/articles/mocksArentStubs.html",
            "https://martinfowler.com/articles/exploring-gen-ai/to-vibe-or-not-vibe.html",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(MartinFowlerLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://martinfowler.com/articles/2023-chatgpt-xu-hao.html|Xu Hao uses chain of thought and general knowledge prompting with ChatGPT when writing self-testing code",
            "https://martinfowler.com/articles/function-call-LLM.html|Building AI Agents that interact with the external world.",
            // old article
            "https://martinfowler.com/articles/patterns-legacy-displacement/revert-to-source.html|Identify the originating source of data and integrate to that",
        }, delimiter = '|')
    void tesSubtitle(final String url,
                     final String expectedSubtitle) {
        checkSubtitle(MartinFowlerLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://martinfowler.com/articles/refactoring-2nd-ed.html|2018-06-01",
        "https://martinfowler.com/articles/mocksArentStubs.html|2007-01-02",
        "https://martinfowler.com/articles/exploring-gen-ai/to-vibe-or-not-vibe.html|2025-09-23",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(MartinFowlerLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://martinfowler.com/articles/refactoring-2nd-ed.html|Martin|Fowler",
        "https://martinfowler.com/articles/mocksArentStubs.html|Martin|Fowler",
        "https://martinfowler.com/articles/exploring-gen-ai/to-vibe-or-not-vibe.html|Birgitta|Böckeler",
        }, delimiter = '|')
    void test1Author(final String url,
                     final String expectedFirstName,
                     final String expectedLastName) {
        check1Author(MartinFowlerLinkContentParser.class, url, null, expectedFirstName, null, expectedLastName, null, null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://martinfowler.com/articles/exploring-gen-ai/software-supply-chain-attack-surface.html|Jim|Gumbley|Lilly|Ryan",
        "https://martinfowler.com/articles/engineering-practices-llm.html|David|Tan|Jessie|Wang",
        }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedLastName2) {
        check2Authors(MartinFowlerLinkContentParser.class,
                      url,
                      null,
                      expectedFirstName1,
                      null,
                      expectedLastName1,
                      null,
                      null,
                      null,
                      expectedFirstName2,
                      null,
                      expectedLastName2,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://martinfowler.com/articles/patterns-legacy-displacement/revert-to-source.html|Ian|Cartwright|Rob|Horn|James|Lewis",
        }, delimiter = '|')
    void test3Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedLastName2,
                      final String expectedFirstName3,
                      final String expectedLastName3) {
        check3Authors(MartinFowlerLinkContentParser.class,
                      url,
                      null,
                      expectedFirstName1,
                      null,
                      expectedLastName1,
                      null,
                      null,
                      null,
                      expectedFirstName2,
                      null,
                      expectedLastName2,
                      null,
                      null,
                      null,
                      expectedFirstName3,
                      null,
                      expectedLastName3,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://martinfowler.com/articles/refactoring-2nd-ed.html|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(MartinFowlerLinkContentParser.class, url, expectedLanguage);
    }
}
