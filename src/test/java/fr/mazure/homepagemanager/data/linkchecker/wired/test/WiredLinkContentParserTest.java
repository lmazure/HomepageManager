package fr.mazure.homepagemanager.data.linkchecker.wired.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.data.linkchecker.wired.WiredLinkContentParser;

/**
 * Tests of WiredLinkContentParser
 */
class WiredLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.wired.com/story/india-deadly-combination-heat-humidity/|India Isn’t Ready for a Deadly Combination of Heat and Humidity",
        "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/|Answer to a 150-Year-Old Math Conundrum Brings More Mystery",
        // the next article has HTML in its title
        "https://www.wired.com/story/mirai-botnet-minecraft-scam-brought-down-the-internet/|How a Dorm Room Minecraft Scam Brought Down the Internet",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(WiredLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.wired.com/story/india-deadly-combination-heat-humidity/|The country’s recent heat wave has seen “wet-bulb” temperatures rise to potentially fatal levels—but plans to handle the crisis are still in their infancy.",
            "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/|A 150-year-old conundrum about how to group people has been solved, but many puzzles remain.",
            // the next article has a space at the end of the subtitle
            "https://www.wired.com/story/trickbot-botnet-uefi-firmware/|The hackers behind TrickBot have begun probing victim PCs for vulnerable firmware, which would let them persist on devices undetected.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(WiredLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.wired.com/2005/10/a-real-remedy-for-phishers/",
            // the next article has a subtitle equal to ""
            "https://www.wired.com/2007/09/ff-allen/"
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(WiredLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.wired.com/1997/12/science-2/",
            "https://www.wired.com/1997/10/genome/",
            "https://www.wired.com/1999/01/amish/"
        }, delimiter = '|')
    void testSubtitleWhichIsAnExtractOfTheArticleIsIgnored(final String url) {
        checkNoSubtitle(WiredLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.wired.com/story/india-deadly-combination-heat-humidity/|2022-06-09",
        "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/|2015-06-20",
        "https://www.wired.com/story/bitcoin-seizure-record-doj-crypto-tracing-monero/|2022-02-09"
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(WiredLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.wired.com/story/india-deadly-combination-heat-humidity/|Kamala|Thiagarajan",
        "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/|Erica|Klarreich",
        // in the next article, the author name is postfixed with ", Ars Technica"
        "https://www.wired.com/story/new-facebook-bug-exposes-millions-of-email-addresses/|Dan|Goodin"
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName) {
        check1Author(WiredLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     null,
                     expectedLastName,
                     null,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // author WIRED Staff
        "https://www.wired.com/2006/01/anonymity-wont-kill-the-internet/",
        // author WIRED Ideas
        "https://www.wired.com/story/large-language-model-phishing-scams/"
        }, delimiter = '|')
    void testNoAuthor(final String url) {
        check0Author(WiredLinkContentParser.class, url);
    }
}
