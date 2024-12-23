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
            "https://developer.ibm.com/tutorials/wa-build-deploy-web-app-sailsjs-2-bluemix",
        })
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
            "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/",
        })
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
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/£An introduction to the java.util.stream library",
        }, delimiter = '£')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(IbmLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/|Run functional-style queries on collections and other data sets",
            // the next article has a space at the end of the subtitle
            "https://developer.ibm.com/articles/j-java-streams-3-brian-goetz/|Understand java.util.stream internals",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(IbmLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/,2016-05-09",
        })
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(IbmLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://developer.ibm.com/articles/j-java8idioms3/,Venkat,,Subramaniam",
        })
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
