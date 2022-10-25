package data.linkchecker.oracleblogs.test;

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
import utils.internet.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class OracleBlogsLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue|Quiz yourself: Break and continue in Java’s for loops",
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue|Quiz yourself: Break and continue in Java’s for loops",
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update|Opening Up Java EE - An Update",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used|12 recipes for using the Optional class as it’s meant to be used",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|Faster and Easier Use and Redistribution of Java SE",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github|Everything you need to know about OpenJDK’s move to Git and GitHub",
        "https://blogs.oracle.com/javamagazine/understanding-the-jdks-new-superfast-garbage-collectors|Understanding the JDK’s New Superfast Garbage Collectors", // the title finishes with a space
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                               Assertions.assertEquals(expectedTitle, parser.getTitle());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue|Sometimes you have to simulate JVM behavior using pencil and paper.",
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue|Sometimes you have to simulate JVM behavior using pencil and paper.",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used|Follow these dozen best practices to protect your applications against ugly null pointer exceptions—and make your code more readable and concise.",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github|The move from Mercurial to Git provided an opportunity to consolidate the source code repositories.",
        // the following articles has a space at the end of its subtitle
        "https://blogs.oracle.com/javamagazine/post/inside-java-13s-switch-expressions-and-reimplemented-socket-api|Incremental changes bring future benefits in this release.",
        "https://blogs.oracle.com/javamagazine/understanding-the-jdks-new-superfast-garbage-collectors|ZGC, Shenandoah, and improvements to G1 get developers closer than ever to pauseless Java.",
        // the following articles has a &rsquo; in the subtitle
        "https://blogs.oracle.com/javamagazine/post/java-project-amber-lambda-loom-panama-valhalla|Many JEPs are collected into named projects. Here’s the latest on the progress of these initiatives.",
        "https://blogs.oracle.com/javamagazine/finance-quant-forex-java16|Java still rocks the finance industry. Here’s why Java 16 makes it even better.",
        "https://blogs.oracle.com/javamagazine/java-is-criminally-underhyped|Recent computer science graduate Jackson Roberts never took a single class in Java. That’s just wrong, he says.",
        // the following article has a newline in the subtitle
        "https://blogs.oracle.com/javamagazine/the-largest-survey-ever-of-java-developers|What 10,500 Java developers tell us about their projects, their tools, and themselves",
        // the following article contains <code> in the subtitle
        "https://blogs.oracle.com/javamagazine/java-enhancedfor-loop-statement|The enhanced for statement operates at a higher level of abstraction than the traditional simple for statement."
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                               Assertions.assertTrue(parser.getSubtitle().isPresent());
                               Assertions.assertEquals(expectedSubtitle, parser.getSubtitle().get());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se",
        // the following article contains <h2> which is not a subtitle
        "https://blogs.oracle.com/javamagazine/post/the-top-25-greatest-java-apps-ever-written"
        })
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                               Assertions.assertFalse(parser.getSubtitle().isPresent());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue,2021-10-05",
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue,2021-10-05",
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update,2017-09-12",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used,2020-06-22",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se,2017-09-06",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github,2021-05-15",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                               try {
                                   TestHelper.assertDate(expectedDate, parser.getDate());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getDate threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update,David,Delabassee",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used,Mohamed,Taman",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se,Donald,Smith",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github,Ian,Darwin",
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
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(1, parser.getSureAuthors().size());
                                   Assertions.assertEquals(expectedAuthor, parser.getSureAuthors().get(0));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue,Mikalai,Zaikin,Simon,Roberts",
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
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(2, parser.getSureAuthors().size());
                                   Assertions.assertEquals(expectedAuthor1, parser.getSureAuthors().get(0));
                                   Assertions.assertEquals(expectedAuthor2, parser.getSureAuthors().get(1));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update,David,Delabassee",
        })
    void doesNotCrashOnCorruptedArticle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String expectedDate = "1970-01-01";
        retriever.retrieve(url,
                          (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFile().isPresent());
                              final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                              final OracleBlogsLinkContentParser parser = new OracleBlogsLinkContentParser(url, data);
                              Assertions.assertEquals("", parser.getTitle());
                              Assertions.assertTrue(parser.getSubtitle().isEmpty());
                              try {
                                  Assertions.assertEquals(0, parser.getSureAuthors().size());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSureAuthors threw " + e.getMessage());
                               }
                              try {
                                  TestHelper.assertDate(expectedDate, parser.getDate());
                              } catch (final ContentParserException e) {
                                  Assertions.fail("getDate threw " + e.getMessage());
                              }
                              consumerHasBeenCalled.set(true);
                          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
