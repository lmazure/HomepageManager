package data.linkchecker.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.MediumLinkContentParser;
import utils.FileHelper;

public class MediumLinkContentParserTest {

    @Test
    void testShortTitle() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("SB Changes", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testLongTitle() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/@kentbeck_7670/productive-compliments-giving-receiving-connecting-dda58570d96b"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("Productive Compliments: Giving, Receiving, Connecting", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithAmpersandAndLink() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/@tdeniffel/tcr-test-commit-revert-a-test-alternative-to-tdd-6e6b03c22bec"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("TCR (test && commit || revert). How to use? Alternative to TDD?", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithGreater() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/@kentbeck_7670/monolith-services-theory-practice-617e4546a879"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("Monolith -> Services: Theory & Practice", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithSlash() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/@kentbeck_7670/fast-slow-in-3x-explore-expand-extract-6d4c94a7539"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("Fast/Slow in 3X: Explore/Expand/Extract", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://medium.com/@kentbeck_7670/curiosity-as-a-service-literally-1f4f6309fae5,Curiosity as a Service\u200A—\u200ALiterally",
        "https://medium.com/@specktackle/selenium-and-webdriverio-a-historical-overview-6f8fbf94b418,Selenium and WebdriverIO\u200A—\u200AA Historical Overview"
        })
    void testTitleWithHairSpace(final String url,
                                final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithMultiline() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/javascript-scene/how-to-build-a-high-velocity-development-team-4b2360d34021"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("How to Build a High Velocity Development Team", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleForNetflix() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/netflix-techblog/a-microscope-on-microservices-923b906103f4"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("A Microscope on Microservices", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://medium.com/@kentbeck_7670/a-years-worth-c1cbc3085e9d,2019-06-08",
        "https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa,2019-05-21",
        "https://medium.com/@kentbeck_7670/buy-effort-sell-value-7a625345ad24,2019-05-10",
        "https://medium.com/@kentbeck_7670/curiosity-as-a-service-literally-1f4f6309fae5,2019-07-04",
        "https://medium.com/@kentbeck_7670/what-i-do-at-gusto-an-incentives-explanation-c7b4f79483ae,2020-05-02",
        "https://medium.com/@kentbeck_7670/software-design-is-human-relationships-part-3-of-3-changers-changers-20eeac7846e0,2019-07-18"
        })
    void testUnmodifiedBlogPublishDate(final String url,
                                       final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedDate, parser.getPublicationDate().toString());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getPublicationDate threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://medium.com/@kentbeck_7670/sipping-the-big-gulp-a7c50549c393,2019-05-11,2019-05-21",
        "https://medium.com/97-things/optional-is-a-law-breaking-monad-but-a-good-type-7667eb821081,2019-07-18,2020-05-14"
        })
    void testModifiedBlogPublishDate(final String url,
                                     final String expectedPublicationDate,
                                     final String expectedModificationDate) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedPublicationDate, parser.getPublicationDate().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getPublicationDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\test\\MediumLinkContentParserTest");
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SynchronousSiteDataRetriever(new SiteDataPersister(cachePath));
    }
}
