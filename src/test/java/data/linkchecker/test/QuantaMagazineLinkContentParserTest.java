package data.linkchecker.test;

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
import data.linkchecker.QuantaMagazineLinkContentParser;
import utils.FileHelper;
import utils.xmlparsing.AuthorData;

public class QuantaMagazineLinkContentParserTest {

    @Test
    void testTitleWithPostfix() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("Universal Method to Sort Complex Information Found", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithoutPostfix() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.quantamagazine.org/mathematician-solves-computer-science-conjecture-in-two-pages-20190725/"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("Decades-Old Computer Science Conjecture Solved in Two Pages", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testSubtitle() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("The nearest neighbor problem asks where a new point fits into an existing data set. A few researchers set out to prove that there was no universal way to solve it. Instead, they found such a way.", parser.getSubtitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testSubtitleContainingQuotes() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.quantamagazine.org/mathematician-solves-computer-science-conjecture-in-two-pages-20190725/"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("The “sensitivity” conjecture stumped many top computer scientists, yet the new proof is so simple that one researcher summed it up in a single tweet.", parser.getSubtitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testSubtitleContainingHtml() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.quantamagazine.org/yitang-zhang-proves-landmark-theorem-in-distribution-of-prime-numbers-20130519/"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
                               try {
                                   Assertions.assertEquals("A virtually unknown researcher has made a great advance in one of mathematics’ oldest problems, the twin primes conjecture.", parser.getSubtitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/a-proof-about-where-symmetries-cant-exist-20181023/|In a major mathematical achievement, a small team of researchers has proven Zimmer’s conjecture.",
        "https://www.quantamagazine.org/how-godels-incompleteness-theorems-work-20200714|His incompleteness theorems destroyed the search for a mathematical theory of everything. Nearly a century later, we’re still coming to grips with the consequences.",
        "https://www.quantamagazine.org/the-useless-perspective-that-transformed-mathematics-20200609/|Representation theory was initially dismissed. Today, it’s central to much of mathematics.",
        }, delimiter = '|')
    void testSubtitleFinishingWithSpace(final String url,
                                        final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
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
        "https://www.quantamagazine.org/new-algorithm-solves-cake-cutting-problem-20161006/,2016-10-06",
        "https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/,2018-08-13"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
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
        "https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/,Kevin,Hartnett"
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
              try {
                  Assertions.assertEquals(expectedAuthor, parser.getAuthor());
               } catch (final ContentParserException e) {
                   Assertions.fail("getAuthor threw " + e.getMessage());
               }
              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.quantamagazine.org/barbara-liskov-is-the-architect-of-modern-algorithms-20191120/,Susan,D'Agostino"
        })
    void testAuthorWithEncodedCharacter(final String url,
                                        final String expectedFirstName,
                                        final String expectedLastName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.of(expectedFirstName),
                                                         Optional.empty(),
                                                         Optional.of(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(this.getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
          (final Boolean b, final SiteData d) -> {
              Assertions.assertTrue(d.getDataFile().isPresent());
              final String data = FileHelper.slurpFile(d.getDataFile().get());
              final QuantaMagazineLinkContentParser parser = new QuantaMagazineLinkContentParser(data);
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
