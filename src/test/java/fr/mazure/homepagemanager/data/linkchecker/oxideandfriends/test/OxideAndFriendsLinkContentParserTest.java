package fr.mazure.homepagemanager.data.linkchecker.oxideandfriends.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.oxideandfriends.OxideAndFriendsLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of OxideAndFriendsLinkContentParser
 */
class OxideAndFriendsLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide.computer/podcasts/oxide-and-friends/2113598|Querying Metrics with OxQL",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(OxideAndFriendsLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide.computer/podcasts/oxide-and-friends/2113598",
    }, delimiter = '|')
    void testSubtitle(final String url) {
        checkNoSubtitle(OxideAndFriendsLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide.computer/podcasts/oxide-and-friends/2113598|2024-10-02",
    }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkDate(OxideAndFriendsLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide.computer/podcasts/oxide-and-friends/2113598|PT1H35M14S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(OxideAndFriendsLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide.computer/podcasts/oxide-and-friends/2113598|Bryan||Cantrill|Adam||Leventhal",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(OxideAndFriendsLinkContentParser.class,
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
}
