package fr.mazure.homepagemanager.data.linkchecker.ibm.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.ibm.IbmLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of IbmLinkContentParser
 */
class IbmLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://developer.ibm.com/articles/wa-sailsjs4/",
            "https://developer.ibm.com/tutorials/wa-build-deploy-web-app-sailsjs-2-bluemix"
        }, delimiter = '|')
    void testArticleIsLost(final String url) {
        perform(IbmLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertTrue(((IbmLinkContentParser)p).articleIsLost());
                    });
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/"
        }, delimiter = '|')
    void testArticleIsNotLost(final String url) {
        perform(IbmLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertFalse(((IbmLinkContentParser)p).articleIsLost());
                    });
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/|An introduction to the java.util.stream library"
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(IbmLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/|A concise explanation of how Java’s java.util.stream pacakage enables functional-style, declarative processing of collections and data sequences through pipelines of intermediate and terminal operations for clearer, more expressive, and potentially parallel code",
            // the next article has a space at the end of the subtitle
            "https://developer.ibm.com/articles/j-java-streams-3-brian-goetz/|Explore the internal mechanics of Java streams for optimized performance, parallel processing, and efficient data handling in modern Java applications",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(IbmLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/|2016-05-09"
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(IbmLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://developer.ibm.com/articles/j-java8idioms3/|Venkat||Subramaniam"
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(IbmLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     null);
    }
}
