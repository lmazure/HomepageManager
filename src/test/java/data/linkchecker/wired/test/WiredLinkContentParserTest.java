package data.linkchecker.wired.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.wired.WiredLinkContentParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class WiredLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://www.wired.com/story/india-deadly-combination-heat-humidity/|India Isn’t Ready for a Deadly Combination of Heat and Humidity",
        "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/|Answer to a 150-Year-Old Math Conundrum Brings More Mystery",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(data);
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
            "https://www.wired.com/story/india-deadly-combination-heat-humidity/|The country’s recent heat wave has seen “wet-bulb” temperatures rise to potentially fatal levels—but plans to handle the crisis are still in their infancy.",
            "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/|A 150-year-old conundrum about how to group people has been solved, but many puzzles remain.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(data);
                               try {
                                   Assertions.assertTrue(parser.getSubtitle().isPresent());
                                   Assertions.assertEquals(expectedSubtitle, parser.getSubtitle().get());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "https://www.wired.com/2005/10/a-real-remedy-for-phishers/",
        })
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(data);
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
        "https://www.wired.com/story/india-deadly-combination-heat-humidity/,2022-06-09",
        "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/,2015-06-20",
        "https://www.wired.com/story/bitcoin-seizure-record-doj-crypto-tracing-monero/,2022-02-09",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(data);
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
        "https://www.wired.com/story/india-deadly-combination-heat-humidity/,Kamala,Thiagarajan",
        "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/,Erica,Klarreich",
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
                              final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                              final WiredLinkContentParser parser = new WiredLinkContentParser(data);
                              try {
                                  Assertions.assertEquals(expectedAuthor, parser.getAuthors().get(0));
                              } catch (final ContentParserException e) {
                                  Assertions.fail("getAuthors threw " + e.getMessage());
                              }
                              consumerHasBeenCalled.set(true);
                          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
