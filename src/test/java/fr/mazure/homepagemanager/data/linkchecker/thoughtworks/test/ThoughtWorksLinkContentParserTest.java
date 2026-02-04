package fr.mazure.homepagemanager.data.linkchecker.thoughtworks.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.thoughtworks.ThoughtWorksLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of ThoughtWorksLinkContentParser class
 */
class ThoughtWorksLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/exploring-ai-agent-platforms|Exploring AI agent platforms",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/architecture-antipatterns-pitfalls|Architecture antipatterns and pitfalls",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/themes-in-technology-radar-vol-32|Themes in Technology Radar Vol.32",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/generative-ai-s-uncanny-valley--problem-or-opportunity-|Generative AI's uncanny valley: Problem or opportunity?",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(ThoughtWorksLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/architecture-antipatterns-pitfalls|Good intentions, bad habits and ugly consequences",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(ThoughtWorksLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/exploring-ai-agent-platforms",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/themes-in-technology-radar-vol-32",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/generative-ai-s-uncanny-valley--problem-or-opportunity-",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(ThoughtWorksLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/exploring-ai-agent-platforms|2026-01-22",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/architecture-antipatterns-pitfalls|2026-01-08",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/themes-in-technology-radar-vol-32|2025-04-17",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/generative-ai-s-uncanny-valley--problem-or-opportunity-|2024-12-12",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(ThoughtWorksLinkContentParser.class, url, expectedDate);
    }

    
    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/exploring-ai-agent-platforms|PT37M59S",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/architecture-antipatterns-pitfalls|PT35M22S",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/themes-in-technology-radar-vol-32|PT38M41S",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/generative-ai-s-uncanny-valley--problem-or-opportunity-|PT28M51S",
        }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(ThoughtWorksLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/generative-ai-s-uncanny-valley--problem-or-opportunity-|Srinivasan||Raguraman|Lilly||Ryan",
        }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(ThoughtWorksLinkContentParser.class,
                      url,
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
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
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/exploring-ai-agent-platforms|Ben||O’Mahony|Fabian||Nonnenmacher|Ken||Mugrage",
        }, delimiter = '|')
    void test3Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2,
                      final String expectedFirstName3,
                      final String expectedMiddleName3,
                      final String expectedLastName3) {
        check3Authors(ThoughtWorksLinkContentParser.class,
                      url,
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      null,
                      null,
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/architecture-antipatterns-pitfalls|Mark||Richards|Raju||Gandhi|Sarah||Grey|Neal||Ford",
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/themes-in-technology-radar-vol-32|Birgitta||Böckeler|Neal||Ford|Lilly||Ryan|Prem||Chandrasekaran",
        }, delimiter = '|')
    void test4Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2,
                      final String expectedFirstName3,
                      final String expectedMiddleName3,
                      final String expectedLastName3,
                      final String expectedFirstName4,
                      final String expectedMiddleName4,
                      final String expectedLastName4) {
        check4Authors(ThoughtWorksLinkContentParser.class,
                      url,
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      null,
                      null,
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      null,
                      null,
                      null,
                      expectedFirstName4,
                      expectedMiddleName4,
                      expectedLastName4,
                      null,
                      null);
    }
    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.thoughtworks.com/insights/podcasts/technology-podcasts/exploring-ai-agent-platforms|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(ThoughtWorksLinkContentParser.class, url, expectedLanguage);
    }
}
