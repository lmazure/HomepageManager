package fr.mazure.homepagemanager.data.linkchecker.arstechnica.test;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.arstechnica.ArsTechnicaLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of ArsTechnicaLinkContentParser
 */
class ArsTechnicaLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/|Consumer Reports shows Tesla Autopilot works with no one in the driver’s seat",
        "https://arstechnica.com/information-technology/2022/09/uber-was-hacked-to-its-core-purportedly-by-an-18-year-old-here-are-the-basics/|Uber was breached to its core, purportedly by an 18-year-old. Here’s what’s known",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(ArsTechnicaLinkContentParser.class, url, expectedTitle);
    }

    
    @ParameterizedTest
    @CsvSource(value = {
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/|Consumer Reports argues Tesla needs a better driver-monitoring system.",
        "https://arstechnica.com/science/2023/03/brightest-ever-gamma-ray-burst-the-boat-continues-to-puzzle-astronomers/|No evidence of associated supernova, and afterglow radio data contradicts current models."
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedSubtitle, parser.getSubtitle().get());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,2021-04-22",
        "https://arstechnica.com/science/2021/08/with-covid-cases-and-deaths-rising-more-unvaccinated-are-lining-up-for-shots/,2021-08-21",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(url, data);
                               try {
                                   Assertions.assertTrue(parser.getDate().isPresent());
                                   Assertions.assertEquals(expectedDate, parser.getDate().get().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,Timothy,B.,Lee",
        "https://arstechnica.com/information-technology/2021/09/travis-ci-flaw-exposed-secrets-for-thousands-of-open-source-projects/,Ax,,Sharma",
        "https://arstechnica.com/gaming/2023/01/dd-maker-still-wants-to-revoke-earlier-versions-of-open-gaming-license/,Kyle,,Orland",
        // the next article contains "-" and digits in the person URL
        "https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/,Tim,,De Chant",
        // the next article contains "_" and digits in the person URL
        "https://arstechnica.com/gaming/2022/08/crypto-driven-gpu-crash-makes-nvidia-miss-q2-projections-by-1-4-billion/,Andrew,,Cunningham",
        // the next article ends with wired.com
        "https://arstechnica.com/information-technology/2022/09/mystery-hackers-are-hyperjacking-targets-for-insidious-spying/,Andy,,Greenberg",
        // the next article ends with Financial Times
        "https://arstechnica.com/tech-policy/2022/12/twitter-rival-mastodon-rejects-funding-to-preserve-nonprofit-status/,Ian,,Johnston",
        // the next article ends with Inside Climate News
        "https://arstechnica.com/cars/2023/03/why-its-time-to-officially-get-over-your-ev-range-anxiety/,Dan,,Gearino",
        // the first name and last name are separated by a non breaking space
        "https://arstechnica.com/gadgets/2023/01/the-generative-ai-revolution-has-begun-how-did-we-get-here/,Haomiao,,Huang",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.of(expectedFirstName),
                                                         Optional.ofNullable(expectedMiddleName),
                                                         Optional.of(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(Collections.singletonList(expectedAuthor), parser.getSureAuthors());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/information-technology/2023/03/chinese-search-giant-launches-ai-chatbot-with-prerecorded-demo/,Ryan,,McMorrow,Qianer,,Liu",
        })
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        final AuthorData expectedAuthor1 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName1),
                                                          Optional.ofNullable(expectedMiddleName1),
                                                          Optional.of(expectedLastName1),
                                                          Optional.empty(),
                                                          Optional.empty());
        final AuthorData expectedAuthor2 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName2),
                                                          Optional.ofNullable(expectedMiddleName2),
                                                          Optional.of(expectedLastName2),
                                                          Optional.empty(),
                                                          Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(2, parser.getSureAuthors().size());
                                   Assertions.assertEquals(expectedAuthor1, parser.getSureAuthors().get(0));
                                   Assertions.assertEquals(expectedAuthor2, parser.getSureAuthors().get(1));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/tech-policy/2022/09/texts-show-roll-call-of-tech-figures-tried-to-help-elon-musk-in-twitter-deal/,Hannah,,Murphy,James,,Fontanella-Khan,Sujeet,,Indap",
        "https://arstechnica.com/science/2023/03/radio-interference-from-satellites-is-threatening-astronomy/,Christopher,Gordon,De Pree,Christopher,R.,Anderson,Mariya,,Zheleva",
        })
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
        final AuthorData expectedAuthor1 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName1),
                                                          Optional.ofNullable(expectedMiddleName1),
                                                          Optional.of(expectedLastName1),
                                                          Optional.empty(),
                                                          Optional.empty());
        final AuthorData expectedAuthor2 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName2),
                                                          Optional.ofNullable(expectedMiddleName2),
                                                          Optional.of(expectedLastName2),
                                                          Optional.empty(),
                                                          Optional.empty());
        final AuthorData expectedAuthor3 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName3),
                                                          Optional.ofNullable(expectedMiddleName3),
                                                          Optional.of(expectedLastName3),
                                                          Optional.empty(),
                                                          Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(3, parser.getSureAuthors().size());
                                   Assertions.assertEquals(expectedAuthor1, parser.getSureAuthors().get(0));
                                   Assertions.assertEquals(expectedAuthor2, parser.getSureAuthors().get(1));
                                   Assertions.assertEquals(expectedAuthor3, parser.getSureAuthors().get(2));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/information-technology/2012/03/microsoft-announces-cloud-building-with-tfs-feature-packs-for-visual-studio/",
        })
    void testAuthorAbsence(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(0, parser.getSureAuthors().size());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
