package fr.mazure.homepagemanager.data.linkchecker.dzone.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.dzone.DZoneLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of DZoneLinkContentParser
 */
class DZoneLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java|Veljko||Krunic",
        "https://dzone.com/articles/explore-annotations-in-java-8|Niamul||Sanjavi",
        "https://dzone.com/articles/clean-code-tips|Rajeev||Bera",
        "https://dzone.com/articles/java-collections-are-evolving|Trisha||Gee",
        "https://dzone.com/articles/functional-approach-to-string-manipulation-in-java|Sameer||Shukla",
        "https://dzone.com/articles/dependency-scope-applied|Maksim||Kren",
        "https://dzone.com/articles/model-compression-dl-model-efficiency|Inderjot|Singh|Saggu",
        "https://dzone.com/articles/hybrid-search-using-postgres-db|Suraj||Dharmapuram",
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(DZoneLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     null);
    }

    /*
    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // this article is dead
        // "https://dzone.com/articles/knowledge-graphs-the-secret-weapon-for-rag-apps|Pavan||Vemuri|Prince||Bose|Tharakarama|Reddy|Yernapalli Sreenivasulu"
        }, delimiter = '|')
    void testThreeAuthors(final String url,
                          final String expectedFirstName1,
                          final String expectedMiddleName1,
                          final String expectedLastName1,
                          final String expectedFirstName2,
                          final String expectedMiddleName2,
                          final String expectedLastName2,
                          final String expectedFirstName3,
                          final String expectedMiddleName3,
                          final String expectedLastName3) {
        check3Authors(DZoneLinkContentParser.class,
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
    */

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java|How to Fix Memory Leaks in Java",
        "https://dzone.com/articles/explore-annotations-in-java-8|Explore Annotations in Java 8",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(DZoneLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/explore-annotations-in-java-8|Explore the evolution of annotations in Java 8 and how they are being used today!",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(DZoneLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(DZoneLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java|2009-03-18",
        "https://dzone.com/articles/explore-annotations-in-java-8|2019-10-22",
        "https://dzone.com/articles/leveraging-lambda-expressions-for-lazy-evaluation|2018-07-28"
        }, delimiter = '|')
    void testPublishDate(final String url,
                         final String expectedDate) {
        checkCreationDate(DZoneLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java|en",
        "https://dzone.com/articles/explore-annotations-in-java-8|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(DZoneLinkContentParser.class, url, expectedLanguage);
    }
}
