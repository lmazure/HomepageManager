package fr.mazure.homepagemanager.data.linkchecker.test;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of LinkContentParser
 */
class LinkContentParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.kaspersky.com/about/press-releases/all-work-and-no-play-it-security-employees-spend-six-hours-of-working-week-on-hobbies",
            "https://viterbi-web.usc.edu/~meshkati/tefall99/toki.html",
            "https://www.cultdeadcow.com",
            "https://www.meteor.com/",
            "https://www.wired.com/1998/04/es-lists/",
            })
    void testLanguageForEnglishArticle(final String url) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertTrue(parser.getLanguage().isPresent());
                            Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage().get());
                            consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.lemonde.fr/blog/vidberg/2013/07/20/une-banale-histoire-de-proces-sur-internet/",
            "https://www.marmiton.org/",
            })
    void testLanguageForFrenchArticle(final String url) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertTrue(parser.getLanguage().isPresent());
                            Assertions.assertEquals(Locale.FRENCH, parser.getLanguage().get());
                            consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.govinfo.gov/app/collection/cfr",
            "http://www.openafs.org/",
            })
    void testLanguageForArticleWithNoText(final String url) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertTrue(parser.getLanguage().isEmpty());
                            consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testLanguageForEnglishMedium() {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa",
                           (final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertTrue(parser.getLanguage().isPresent());
                            Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage().get());
                            consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testLanguageForFrenchMedium() {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://medium.com/france/praha-8e7086a6c1fe",
                           (final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertTrue(parser.getLanguage().isPresent());
                            Assertions.assertEquals(Locale.FRENCH, parser.getLanguage().get());
                            consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
