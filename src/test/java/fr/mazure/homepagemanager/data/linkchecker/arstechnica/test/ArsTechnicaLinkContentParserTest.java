package fr.mazure.homepagemanager.data.linkchecker.arstechnica.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of ArsTechnicaLinkContentParser
 */
class ArsTechnicaLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/|Consumer Reports shows Tesla Autopilot works with no one in the driver’s seat",
        "https://arstechnica.com/information-technology/2022/09/uber-was-hacked-to-its-core-purportedly-by-an-18-year-old-here-are-the-basics/|Uber was breached to its core, purportedly by an 18-year-old. Here’s what’s known",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(ArsTechnicaLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/|Consumer Reports argues Tesla needs a better driver-monitoring system.",
        "https://arstechnica.com/science/2023/03/brightest-ever-gamma-ray-burst-the-boat-continues-to-puzzle-astronomers/|No evidence of associated supernova, and afterglow radio data contradicts current models."
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(ArsTechnicaLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,2021-04-22",
        "https://arstechnica.com/science/2021/08/with-covid-cases-and-deaths-rising-more-unvaccinated-are-lining-up-for-shots/,2021-08-21",
        })
    void testDate(final String url,
                  final String expectedDate) {
        checkDate(ArsTechnicaLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/|Timothy|B.|Lee",
        "https://arstechnica.com/information-technology/2021/09/travis-ci-flaw-exposed-secrets-for-thousands-of-open-source-projects/|Ax||Sharma",
        "https://arstechnica.com/gaming/2023/01/dd-maker-still-wants-to-revoke-earlier-versions-of-open-gaming-license/|Kyle||Orland",
        // the next article contains "-" and digits in the person URL
        "https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/|Tim||De Chant",
        // the next article contains "_" and digits in the person URL
        "https://arstechnica.com/gaming/2022/08/crypto-driven-gpu-crash-makes-nvidia-miss-q2-projections-by-1-4-billion/|Andrew||Cunningham",
        // the next article ends with wired.com
        "https://arstechnica.com/information-technology/2022/09/mystery-hackers-are-hyperjacking-targets-for-insidious-spying/|Andy||Greenberg",
        // the next article ends with Financial Times
        "https://arstechnica.com/tech-policy/2022/12/twitter-rival-mastodon-rejects-funding-to-preserve-nonprofit-status/|Ian||Johnston",
        // the next article ends with Inside Climate News
        "https://arstechnica.com/cars/2023/03/why-its-time-to-officially-get-over-your-ev-range-anxiety/|Dan||Gearino",
        // the first name and last name are separated by a non breaking space
        "https://arstechnica.com/gadgets/2023/01/the-generative-ai-revolution-has-begun-how-did-we-get-here/|Haomiao||Huang",
    }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {

        check1Author(ArsTechnicaLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://arstechnica.com/information-technology/2023/03/chinese-search-giant-launches-ai-chatbot-with-prerecorded-demo/|Ryan||McMorrow|Qianer||Liu",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(ArsTechnicaLinkContentParser.class,
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
        "https://arstechnica.com/tech-policy/2022/09/texts-show-roll-call-of-tech-figures-tried-to-help-elon-musk-in-twitter-deal/|Hannah||Murphy|James||Fontanella-Khan|Sujeet||Indap",
        "https://arstechnica.com/science/2023/03/radio-interference-from-satellites-is-threatening-astronomy/|Christopher|Gordon|De Pree|Christopher|R.|Anderson|Mariya||Zheleva",
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
        check3Authors(ArsTechnicaLinkContentParser.class,
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
    @CsvSource({
        "https://arstechnica.com/information-technology/2012/03/microsoft-announces-cloud-building-with-tfs-feature-packs-for-visual-studio/",
        })
    void testAuthorAbsence(final String url) {
        check0Author(ArsTechnicaLinkContentParser.class,
                     url);
    }
}
