package fr.mazure.homepagemanager.data.linkchecker.baeldung.test;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of BaeldungLinkContentParser
 */
class BaeldungLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @Test
    void testTitle() {
        final String url = "https://www.baeldung.com/crawler4j";
        final String expectedTitle = "A Guide to Crawler4j";
        checkTitle(BaeldungLinkContentParser.class, url, expectedTitle);
    }

    @Test
    void testNoSubtitle() {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.baeldung.com/crawler4j";
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data, retriever);
                               Assertions.assertFalse(parser.getSubtitle().isPresent());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.baeldung.com/crawler4j,2024-01-11",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                          (final FullFetchedLinkData d) -> {
                              Assertions.assertTrue(d.dataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                              final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data, retriever);
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
        "https://www.baeldung.com/crawler4j,Amy,DeGregorio",
        "https://www.baeldung.com/ops/git-configure-credentials,Michael,Pratt",
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
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data, retriever);
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
        "https://www.baeldung.com/java-9-reactive-streams",
        })
    void testNoAuthor(final String url) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                          (final FullFetchedLinkData d) -> {
                              Assertions.assertTrue(d.dataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                              final BaeldungLinkContentParser parser = new BaeldungLinkContentParser(url, data, retriever);
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
