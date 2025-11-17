package fr.mazure.homepagemanager.data.linkchecker.oracleblogs.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.oracleblogs.OracleBlogsLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of OracleBlogsLinkContentParser
 */
class OracleBlogsLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|Faster and Easier Use and Redistribution of Java SE",
        "https://blogs.oracle.com/java/post/the-arrival-of-java-20|The Arrival of Java 20",
        "https://blogs.oracle.com/java/post/javaone-is-back|JavaOne is Back!",
        "https://blogs.oracle.com/cloud-infrastructure/post/oracle-code-assist-ai-companion-boost-velocity|Oracle Code Assist: AI companion to boost developer velocity",
        "https://blogs.oracle.com/javamagazine/java-quiz-serialize-primitive-value/|Quiz yourself: Serializing a primitive with ObjectOutputStream",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(OracleBlogsLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://blogs.oracle.com/javamagazine/java-quiz-serialize-primitive-value/|Primitives? Objects? What should you do?",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(OracleBlogsLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se",
        "https://blogs.oracle.com/javamagazine/post/the-top-25-greatest-java-apps-ever-written"
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(OracleBlogsLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|2017-09-06",
        "https://blogs.oracle.com/javamagazine/java-quiz-serialize-primitive-value/|2023-09-25",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(OracleBlogsLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|Donald||Smith",
        "https://blogs.oracle.com/cloud-infrastructure/post/oracle-code-assist-ai-companion-boost-velocity|Aanand||Krishnan",
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(OracleBlogsLinkContentParser.class, url, null, expectedFirstName, expectedMiddleName, expectedLastName, null, null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/javamagazine/java-quiz-serialize-primitive-value/|Mikalai|Zaikin|Simon|Roberts"
        }, delimiter = '|')
    void testTwoAuthors(final String url,
                        final String expectedFirstName1,
                        final String expectedLastName1,
                        final String expectedFirstName2,
                        final String expectedLastName2) {
        check2Authors(OracleBlogsLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      null,
                      expectedLastName1,
                      null,
                      null,
                      // author 2
                      null,
                      expectedFirstName2,
                      null,
                      expectedLastName2,
                      null,
                      null);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update|David|Delabassee"
        }, delimiter = '|')
    void doesNotCrashOnCorruptedArticle(final String url) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String expectedDate = "1970-01-01";
        retriever.retrieve(url,
                          (final FullFetchedLinkData d) -> {
                              Assertions.assertTrue(d.dataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                              final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data, retriever);
                              Assertions.assertEquals("", parser.getTitle());
                              Assertions.assertTrue(parser.getSubtitle().isEmpty());
                              Assertions.assertEquals(0, parser.getSureAuthors().size());
                              TestHelper.assertDate(expectedDate, parser.getCreationDate());
                              consumerHasBeenCalled.set(true);
                          },
                          false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
