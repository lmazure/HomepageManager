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
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue|Quiz yourself: Break and continue in Java’s for loops",
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue|Quiz yourself: Break and continue in Java’s for loops",
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update|Opening Up Java EE - An Update",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used|12 recipes for using the Optional class as it’s meant to be used",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|Faster and Easier Use and Redistribution of Java SE",
        "https://blogs.oracle.com/java/post/the-arrival-of-java-20|The Arrival of Java 20",
        "https://blogs.oracle.com/java/post/javaone-is-back|JavaOne is Back!",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github|Everything you need to know about OpenJDK’s move to Git and GitHub",
        "https://blogs.oracle.com/javamagazine/understanding-the-jdks-new-superfast-garbage-collectors|Understanding the JDK’s New Superfast Garbage Collectors", // the title finishes with a space
        "https://blogs.oracle.com/cloud-infrastructure/post/oracle-code-assist-ai-companion-boost-velocity|Oracle Code Assist: AI companion to boost developer velocity",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(OracleBlogsLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
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
        "https://blogs.oracle.com/javamagazine/finance-quant-forex-java16|Java still rocks the finance industry. Here’s why Java 16 makes it even better.",
        "https://blogs.oracle.com/javamagazine/java-is-criminally-underhyped|Recent computer science graduate Jackson Roberts never took a single class in Java. That’s just wrong, he says.",
        // the following article has a newline in the subtitle
        "https://blogs.oracle.com/javamagazine/the-largest-survey-ever-of-java-developers|What 10,500 Java developers tell us about their projects, their tools, and themselves",
        // the following article contains <code> in the subtitle
        "https://blogs.oracle.com/javamagazine/java-enhancedfor-loop-statement|The enhanced for statement operates at a higher level of abstraction than the traditional simple for statement.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(OracleBlogsLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se",
        // the following article contains <h2> which is not a subtitle
        "https://blogs.oracle.com/javamagazine/post/the-top-25-greatest-java-apps-ever-written"
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(OracleBlogsLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue|2021-10-05",
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue|2021-10-05",
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update|2017-09-12",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used|2020-06-22",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|2017-09-06",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github|2021-05-15"
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(OracleBlogsLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // the next article is broken
        //"https://blogs.oracle.com/theaquarium/post/opening-up-java-ee-an-update|David||Delabassee",
        "https://blogs.oracle.com/javamagazine/post/12-recipes-for-using-the-optional-class-as-its-meant-to-be-used|Mohamed||Taman",
        "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se|Donald||Smith",
        "https://blogs.oracle.com/javamagazine/post/everything-you-need-to-know-about-openjdks-move-to-git-and-github|Ian||Darwin",
        "https://blogs.oracle.com/cloud-infrastructure/post/oracle-code-assist-ai-companion-boost-velocity|Aanand||Krishnan",
        "https://blogs.oracle.com/javamagazine/post/curly-braces-java-recursion-tail-call-optimization|Eric|J.|Bruno"
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
        "https://blogs.oracle.com/javamagazine/java-for-loop-break-continue|Mikalai|Zaikin|Simon|Roberts",
        "https://blogs.oracle.com/javamagazine/post/java-for-loop-break-continue|Mikalai|Zaikin|Simon|Roberts"
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
