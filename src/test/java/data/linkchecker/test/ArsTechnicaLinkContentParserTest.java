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
import data.linkchecker.ars_technica.ArsTechnicaLinkContentParser;
import utils.FileHelper;
import utils.xmlparsing.AuthorData;

public class ArsTechnicaLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,Consumer Reports shows Tesla Autopilot works with no one in the driver’s seat"
        })
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
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
        "https://arstechnica.com/cars/2021/04/consumer-reports-shows-tesla-autopilot-works-with-no-one-in-the-drivers-seat/,2021-04-22"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
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
        "https://arstechnica.com/information-technology/2021/09/travis-ci-flaw-exposed-secrets-for-thousands-of-open-source-projects/,Ax,,Sharma"
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final ArsTechnicaLinkContentParser parser = new ArsTechnicaLinkContentParser(data);
              try {
                  Assertions.assertEquals(expectedAuthor, parser.getAuthor());
               } catch (final ContentParserException e) {
                   Assertions.fail("getAuthor threw " + e.getMessage());
               }
              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
