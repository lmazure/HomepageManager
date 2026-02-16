package fr.mazure.homepagemanager.data.linkchecker.twiml.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.data.linkchecker.twiml.TwimlLinkContentParser;

/**
 * Tests of TwimlLinkContentParser
 */
class TwimlLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://twimlai.com/podcast/twimlai/building-an-ai-mathematician/|Building an AI Mathematician with Carina Hong",
        "https://twimlai.com/podcast/twimlai/ai-orchestration-for-smart-cities-and-the-enterprise/|AI Orchestration for Smart Cities and the Enterprise with Luke Norris, Robin Braun",
        "https://twimlai.com/podcast/twimlai/why-agents-are-stupid-what-we-can-do-about-it/|Why Agents Are Stupid & What We Can Do About It with Dan Jeffries",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(TwimlLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://twimlai.com/podcast/twimlai/building-an-ai-mathematician/|2025-11-04",
        "https://twimlai.com/podcast/twimlai/ai-orchestration-for-smart-cities-and-the-enterprise/|2025-11-12",
        "https://twimlai.com/podcast/twimlai/why-agents-are-stupid-what-we-can-do-about-it/|2024-12-16",
    }, delimiter = '|')
    void testPublicationDate(final String url,
                             final String expectedPublicationDate) {
        checkPublicationDate(TwimlLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://twimlai.com/podcast/twimlai/building-an-ai-mathematician/|Carina||Hong|Sam||Charrington",
        "https://twimlai.com/podcast/twimlai/why-agents-are-stupid-what-we-can-do-about-it/|Dan||Jeffries|Sam||Charrington",
    }, delimiter = '|')
    void test2Authors(final String url,
                     final String expectedFirstName1,
                     final String expectedMiddleName1,
                     final String expectedLastName1,
                     final String expectedFirstName2,
                     final String expectedMiddleName2,
                     final String expectedLastName2) {
        check2Authors(TwimlLinkContentParser.class,
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
        "https://twimlai.com/podcast/twimlai/ai-orchestration-for-smart-cities-and-the-enterprise/|Luke||Norris|Robin||Braun|Sam||Charrington",
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
        check3Authors(TwimlLinkContentParser.class,
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
                      null,
                      // author 3
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
        "https://twimlai.com/podcast/twimlai/building-an-ai-mathematician/|https://www.youtube.com/watch?v=rIW6iGqH0F8",
        "https://twimlai.com/podcast/twimlai/ai-orchestration-for-smart-cities-and-the-enterprise/|https://www.youtube.com/watch?v=sTPs-FOsDm8",
        "https://twimlai.com/podcast/twimlai/why-agents-are-stupid-what-we-can-do-about-it/|https://www.youtube.com/watch?v=50ydENhIJeM",
    }, delimiter = '|')
    void testOtherLink(final String url,
                       final String expectedOtherLink) {
        checkOtherLink(TwimlLinkContentParser.class, url, expectedOtherLink);
    }


    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://twimlai.com/podcast/twimlai/building-an-ai-mathematician/|PT54M53S",
        "https://twimlai.com/podcast/twimlai/ai-orchestration-for-smart-cities-and-the-enterprise/|PT54M17S",
        "https://twimlai.com/podcast/twimlai/why-agents-are-stupid-what-we-can-do-about-it/|PT1H8M19S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(TwimlLinkContentParser.class, url, expectedDuration);
    }
}
