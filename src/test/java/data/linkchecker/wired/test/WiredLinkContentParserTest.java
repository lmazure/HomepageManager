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
        // the next article has HTML in its title
        "https://www.wired.com/story/mirai-botnet-minecraft-scam-brought-down-the-internet/|How a Dorm Room Minecraft Scam Brought Down the Internet",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
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
            // the next article has a space at the end of the subtitle
            "https://www.wired.com/story/trickbot-botnet-uefi-firmware/|The hackers behind TrickBot have begun probing victim PCs for vulnerable firmware, which would let them persist on devices undetected."
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
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
            // the next article has a subtitle equal to ""
            "https://www.wired.com/2007/09/ff-allen/"
        })
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
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
    @CsvSource(value = {
            "https://www.wired.com/1997/12/science-2/",
            "https://www.wired.com/1997/10/genome/",
            "https://www.wired.com/1999/01/amish/"
        })
    void testSubtitleWhichIsAnExtractOfTheArticleIsIgnored(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
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
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedDate, parser.getDateInternal().toString());
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
        // in the next article, the author name is postfixed with ", Ars Technica"
        "https://www.wired.com/story/new-facebook-bug-exposes-millions-of-email-addresses/,Dan,Goodin"
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
                              final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
                              try {
                                  Assertions.assertEquals(1, parser.getAuthors().size());
                                  Assertions.assertEquals(expectedAuthor, parser.getAuthors().get(0));
                              } catch (final ContentParserException e) {
                                  Assertions.fail("getAuthors threw " + e.getMessage());
                              }
                              consumerHasBeenCalled.set(true);
                          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.wired.com/2006/01/anonymity-wont-kill-the-internet/",
        })
    void testNoAuthor(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                          (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFile().isPresent());
                              final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                              final WiredLinkContentParser parser = new WiredLinkContentParser(url, data);
                              try {
                                  Assertions.assertEquals(0, parser.getAuthors().size());
                              } catch (final ContentParserException e) {
                                  Assertions.fail("getAuthors threw " + e.getMessage());
                              }
                              consumerHasBeenCalled.set(true);
                          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
