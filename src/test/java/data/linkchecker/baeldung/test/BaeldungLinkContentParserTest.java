package data.linkchecker.baeldung.test;

import java.util.Collections;
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
import data.linkchecker.baeldung.BaeldungLinkContentParser;
import utils.internet.HtmlHelper;
import utils.xmlparsing.AuthorData;

/**
 * Tests of BaeldungLinkContentParser
 *
 */
public class BaeldungLinkContentParserTest {

    @Test
    void testTitle() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.baeldung.com/crawler4j";
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFileSection().get());
                               final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals("A Guide to Crawler4j", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.baeldung.com/crawler4j,2019-09-06"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                          (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.getDataFileSection().get());
                              final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data);
                              try {
                                  TestHelper.assertDate(expectedDate, parser.getDate());
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
        "https://www.baeldung.com/crawler4j,Amy,DeGregorio"
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
                               Assertions.assertTrue(d.getDataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFileSection().get());
                               final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data);
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
        "https://www.baeldung.com/java-9-reactive-streams"
        })
    void testNoAuthor(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                          (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.getDataFileSection().get());
                              final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data);
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
