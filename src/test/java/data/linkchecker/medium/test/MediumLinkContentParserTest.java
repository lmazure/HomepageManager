package data.linkchecker.medium.test;

import java.net.URL;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.medium.MediumLinkContentParser;
import utils.HtmlHelper;
import utils.StringHelper;
import utils.xmlparsing.AuthorData;

public class MediumLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://medium.com/@kentbeck_7670/sipping-the-big-gulp-a7c50549c393,Kent,Beck,",
        "https://medium.com/@FibreTigre/mon-emploi-du-temps-2019-b4a44c2efa46,,,FibreTigre",
        "https://medium.com/@sendilkumarn/safevarargs-variable-arguments-in-java-b9fdd5d996bb,,,sendilkumarn"
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.ofNullable(expectedFirstName),
                                                         Optional.empty(),
                                                         Optional.ofNullable(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.ofNullable(expectedGivenName));
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = HtmlHelper.slurpFile(d.getDataFile().get());
              final MediumLinkContentParser parser = new MediumLinkContentParser(data, StringHelper.convertStringToUrl(url));
              try {
                  Assertions.assertEquals(expectedAuthor, parser.getAuthors().get(0));
               } catch (final ContentParserException e) {
                   Assertions.fail("getAuthor threw " + e.getMessage());
               }
              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testShortTitle() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL("https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa");
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
                               try {
                                   Assertions.assertEquals("SB Changes", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@kentbeck_7670/productive-compliments-giving-receiving-connecting-dda58570d96b|Productive Compliments: Giving, Receiving, Connecting",
        "https://medium.com/@kentbeck_7670/sipping-the-big-gulp-a7c50549c393|Sipping the Big Gulp: 2 Ways to Narrow an Interface"
        }, delimiter = '|')
    void testLongTitle(final String urlString,
                       final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL(urlString);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
    void testTitleWithAmpersandAndLink() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL("https://medium.com/@tdeniffel/tcr-test-commit-revert-a-test-alternative-to-tdd-6e6b03c22bec");
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL("https://medium.com/@kentbeck_7670/monolith-services-theory-practice-617e4546a879");
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL("https://medium.com/@kentbeck_7670/fast-slow-in-3x-explore-expand-extract-6d4c94a7539");
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
    void testTitleWithHairSpace(final String urlString,
                                final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL(urlString);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL("https://medium.com/javascript-scene/how-to-build-a-high-velocity-development-team-4b2360d34021");
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL("https://medium.com/netflix-techblog/a-microscope-on-microservices-923b906103f4");
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
    void testUnmodifiedBlogPublishDate(final String urlString,
                                       final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL(urlString);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
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
    void testModifiedBlogPublishDate(final String urlString,
                                     final String expectedPublicationDate,
                                     @SuppressWarnings("unused") final String expectedModificationDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final URL url = TestHelper.buildURL(urlString);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final MediumLinkContentParser parser = new MediumLinkContentParser(data, url);
                               try {
                                   Assertions.assertEquals(expectedPublicationDate, parser.getPublicationDate().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getPublicationDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
