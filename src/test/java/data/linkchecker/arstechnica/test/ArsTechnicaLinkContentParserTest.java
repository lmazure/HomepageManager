package data.linkchecker.arstechnica.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.arstechnica.ArsTechnicaLinkContentParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class ArsTechnicaLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,Consumer Reports shows Tesla Autopilot works with no one in the driver’s seat"
        })
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(data);
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
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,Consumer Reports argues Tesla needs a better driver-monitoring system."
        })
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedSubtitle, parser.getSubtitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,2021-04-22",
        "https://arstechnica.com/science/2021/08/with-covid-cases-and-deaths-rising-more-unvaccinated-are-lining-up-for-shots/,2021-08-21"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(data);
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
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,Timothy,B.,Lee",
        "https://arstechnica.com/information-technology/2021/09/travis-ci-flaw-exposed-secrets-for-thousands-of-open-source-projects/,Ax,,Sharma",
        // the next article contains "-" and digits in the person URL
        "https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/,Tim,,De Chant",
        // the next article contains "_" and digits in the person URL
        "https://arstechnica.com/gaming/2022/08/crypto-driven-gpu-crash-makes-nvidia-miss-q2-projections-by-1-4-billion/,Andrew,,Cunningham"
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
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(data);
                               try {
                                   Assertions.assertTrue(parser.getAuthor().isPresent());
                                   Assertions.assertEquals(expectedAuthor, parser.getAuthor().get());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getAuthor threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/information-technology/2012/03/microsoft-announces-cloud-building-with-tfs-feature-packs-for-visual-studio/"
        })
    void testAuthorAbsence(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(data);
                               try {
                                   Assertions.assertFalse(parser.getAuthor().isPresent());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getAuthor threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
