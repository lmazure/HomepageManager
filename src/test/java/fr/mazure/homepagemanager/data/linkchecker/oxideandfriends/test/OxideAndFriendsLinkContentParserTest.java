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
        "https://oxide-and-friends.transistor.fm/episodes/querying-metrics-with-oxql|Querying Metrics with OxQL",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(OxideAndFriendsLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide-and-friends.transistor.fm/episodes/querying-metrics-with-oxql",
    }, delimiter = '|')
    void testSubtitle(final String url) {
        checkNoSubtitle(OxideAndFriendsLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide-and-friends.transistor.fm/episodes/querying-metrics-with-oxql|2024-09-30",
    }, delimiter = '|')
    void testCreationDate(final String url,
                          final String expectedDate) {
        checkCreationDate(OxideAndFriendsLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide-and-friends.transistor.fm/episodes/querying-metrics-with-oxql|2024-10-02",
    }, delimiter = '|')
    void testPublicationDate(final String url,
                             final String expectedDate) {
        checkPublicationDate(OxideAndFriendsLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide-and-friends.transistor.fm/episodes/querying-metrics-with-oxql|PT1H35M14S",
        // the new episode is less than 1 hour long
        "https://oxide-and-friends.transistor.fm/episodes/software-engineering-past-present-and-future-with-grady-booch|PT54M52S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(OxideAndFriendsLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide-and-friends.transistor.fm/episodes/querying-metrics-with-oxql|Ben||Naecker|Bryan||Cantrill|Adam||Leventhal",
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
        check3Authors(OxideAndFriendsLinkContentParser.class,
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
        "https://oxide-and-friends.transistor.fm/episodes/technical-blogging|Bryan||Cantrill|Adam||Leventhal|Cynthia||Dunlop|Tim||Bray|Piotr||Sarna|Will||Snow",
    }, delimiter = '|')
    void test6Authors(final String url,
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
                      final String expectedLastName4,
                      final String expectedFirstName5,
                      final String expectedMiddleName5,
                      final String expectedLastName5,
                      final String expectedFirstName6,
                      final String expectedMiddleName6,
                      final String expectedLastName6) {
        check6Authors(OxideAndFriendsLinkContentParser.class,
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
                      null,
                      // author 4
                      null,
                      expectedFirstName4,
                      expectedMiddleName4,
                      expectedLastName4,
                      null,
                      null,
                      // author 5
                      null,
                      expectedFirstName5,
                      expectedMiddleName5,
                      expectedLastName5,
                      null,
                      null,
                      // author 6
                      null,
                      expectedFirstName6,
                      expectedMiddleName6,
                      expectedLastName6,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://oxide-and-friends.transistor.fm/episodes/pragmatic-llm-usage-with-nicholas-carlini|Bryan||Cantrill||Nicholas||Carlini||Adam||Leventhal||Mike||Cafarella|||||p5commmit",
    }, delimiter = '|')
    void checkThatUnknownVoicesAreIgnored(final String url,
                                          final String expectedFirstName1,
                                          final String expectedMiddleName1,
                                          final String expectedLastName1,
                                          final String expectedGivenName1,
                                          final String expectedFirstName2,
                                          final String expectedMiddleName2,
                                          final String expectedLastName2,
                                          final String expectedGivenName2,
                                          final String expectedFirstName3,
                                          final String expectedMiddleName3,
                                          final String expectedLastName3,
                                          final String expectedGivenName3,
                                          final String expectedFirstName4,
                                          final String expectedMiddleName4,
                                          final String expectedLastName4,
                                          final String expectedGivenName4,
                                          final String expectedFirstName5,
                                          final String expectedMiddleName5,
                                          final String expectedLastName5,
                                          final String expectedGivenName5) {
        check5Authors(OxideAndFriendsLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      expectedGivenName1,
                      // author 2
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      expectedGivenName2,
                      // author 3
                      null,
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      null,
                      expectedGivenName3,
                      // author 4
                      null,
                      expectedFirstName4,
                      expectedMiddleName4,
                      expectedLastName4,
                      null,
                      expectedGivenName4,
                      // author 5
                      null,
                      expectedFirstName5,
                      expectedMiddleName5,
                      expectedLastName5,
                      null,
                      expectedGivenName5);
    }
}
