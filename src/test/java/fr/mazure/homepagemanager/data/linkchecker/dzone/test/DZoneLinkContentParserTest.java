package fr.mazure.homepagemanager.data.linkchecker.dzone.test;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.dzone.DZoneLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of DZoneLinkContentParser
 */
class DZoneLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://dzone.com/articles/how-fix-memory-leaks-java,Veljko,Krunic",
        "https://dzone.com/articles/explore-annotations-in-java-8,Niamul,Sanjavi",
        "https://dzone.com/articles/clean-code-tips,Rajeev,Bera",
        "https://dzone.com/articles/java-collections-are-evolving,Trisha,Gee",
        "https://dzone.com/articles/functional-approach-to-string-manipulation-in-java,Sameer,Shukla",
        "https://dzone.com/articles/dependency-scope-applied,Maksim,Kren",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.ofNullable(expectedFirstName),
                                                         Optional.empty(),
                                                         Optional.ofNullable(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final DZoneLinkContentParser parser = new DZoneLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(1, parser.getSureAuthors().size());
                                   Assertions.assertEquals(expectedAuthor, parser.getSureAuthors().get(0));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java|How to Fix Memory Leaks in Java",
        "https://dzone.com/articles/explore-annotations-in-java-8|Explore Annotations in Java 8",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final DZoneLinkContentParser parser = new DZoneLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/explore-annotations-in-java-8|Explore the evolution of annotations in Java 8 and how they are being used today!",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final DZoneLinkContentParser parser = new DZoneLinkContentParser(url, data);
                               try {
                                   Assertions.assertTrue(parser.getSubtitle().isPresent());
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
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final DZoneLinkContentParser parser = new DZoneLinkContentParser(url, data);
                               try {
                                   Assertions.assertFalse(parser.getSubtitle().isPresent());
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
        "https://dzone.com/articles/how-fix-memory-leaks-java,2009-03-18",
        "https://dzone.com/articles/explore-annotations-in-java-8,2019-10-22",
        "https://dzone.com/articles/leveraging-lambda-expressions-for-lazy-evaluation,2018-07-28",
        })
    void testPublishDate(final String url,
                         final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final DZoneLinkContentParser parser = new DZoneLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedDate, parser.getPublicationDate().toString());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getPublicationDate threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
    @ParameterizedTest
    @CsvSource(value = {
        "https://dzone.com/articles/how-fix-memory-leaks-java|en",
        "https://dzone.com/articles/explore-annotations-in-java-8|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final DZoneLinkContentParser parser = new DZoneLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(Locale.of(expectedLanguage), parser.getLanguage());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getLanguage threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
