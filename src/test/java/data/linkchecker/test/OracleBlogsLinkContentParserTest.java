package data.linkchecker.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.oracleblogs.OracleBlogsLinkContentParser;
import utils.FileHelper;
import utils.StringHelper;
import utils.xmlparsing.AuthorData;

public class OracleBlogsLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue,Quiz yourself: Break and continue in Java’s for loops",
        "https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update,Opening Up Java EE - An Update",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used,12 recipes for using the Optional class as it’s meant to be used",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se,Faster and Easier Use and Redistribution of Java SE"
        })
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(data, StringHelper.convertStringToUrl(url));
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue|Sometimes you have to simulate JVM behavior using pencil and paper.",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used|Follow these dozen best practices to protect your applications against ugly null pointer exceptions—and make your code more readable and concise.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(data, StringHelper.convertStringToUrl(url));
                               try {
                                   Assertions.assertTrue(parser.getSubtitle().isPresent());
                                   Assertions.assertEquals(expectedSubtitle, parser.getSubtitle().get());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se"
        })
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(data, StringHelper.convertStringToUrl(url));
                               try {
                                   Assertions.assertFalse(parser.getSubtitle().isPresent());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
    
    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue,2021-10-05",
        "https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update,2017-09-12",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used,2020-06-22",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se,2017-09-06"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(data, StringHelper.convertStringToUrl(url));
              try {
                  Assertions.assertEquals(expectedDate, parser.getDate().toString());
               } catch (final ContentParserException e) {
                   Assertions.fail("getDate threw " + e.getMessage());
               }
              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update,David,Delabassee",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used,Mohamed,Taman",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se,Donald,Smith"
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.of(expectedFirstName),
                                                         Optional.empty(),
                                                         Optional.of(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(data, StringHelper.convertStringToUrl(url));
              try {
                  Assertions.assertEquals(expectedAuthor, parser.getAuthor().get(0));
               } catch (final ContentParserException e) {
                   Assertions.fail("getAuthor threw " + e.getMessage());
               }
              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue,Mikalai,Zaikin,Simon,Roberts"
        })
    void testTwoAuthors(final String url,
                        final String expectedFirstName1,
                        final String expectedLastName1,
                        final String expectedFirstName2,
                        final String expectedLastName2) {
        final AuthorData expectedAuthor1 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName1),
                                                          Optional.empty(),
                                                          Optional.of(expectedLastName1),
                                                          Optional.empty(),
                                                          Optional.empty());
        final AuthorData expectedAuthor2 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName2),
                                                          Optional.empty(),
                                                          Optional.of(expectedLastName2),
                                                          Optional.empty(),
                                                          Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(data, StringHelper.convertStringToUrl(url));
              try {
                  Assertions.assertEquals(expectedAuthor1, parser.getAuthor().get(0));
                  Assertions.assertEquals(expectedAuthor2, parser.getAuthor().get(1));
               } catch (final ContentParserException e) {
                   Assertions.fail("getAuthor threw " + e.getMessage());
               }
              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}