package fr.mazure.homepagemanager.data.linkchecker.test;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of LinkContentParser
 */
public class LinkContentParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://usa.kaspersky.com/about/press-releases/2016_kaspersky-lab-study-proves-smartphones-distract-workers-and-decrease-productivity",
            "https://safsdev.sourceforge.net/FRAMESDataDrivenTestAutomationFrameworks.htm",
            "https://viterbi-web.usc.edu/~meshkati/tefall99/toki.html",
            "https://www.cultdeadcow.com",
            "https://www.meteor.com/",
            "https://www.wired.com/1998/04/es-lists/"
            })
    void testLanguageForEnglishArticle(String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
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
            "http://astronogeek.fr/",
            "https://spoutlink.com/",
            "https://www.france24.com/fr/20200419-covid-19-le-fl%C3%A9au-de-trop-pour-les-peuples-indig%C3%A8nes-au-br%C3%A9sil",
            "https://www.france24.com/fr/am%C3%A9riques/20200527-%C3%A9mission-sp%C3%A9ciale-le-br%C3%A9sil-%C3%A9picentre-du-covid-19-en-am%C3%A9rique-latine",
            "https://www.lavoixdunord.fr/752544/article/2020-05-13/coronavirus-les-foyers-de-morts-mysterieuses-se-multiplient-au-nigeria",
            "https://www.lemonde.fr/blog/vidberg/2013/07/20/une-banale-histoire-de-proces-sur-internet/",
            "https://www.marmiton.org/"
            })
    void testLanguageForFrenchArticle(String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
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
            "http://www.openafs.org/"
            })
    void testLanguageForArticleWithNoText(String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa",
                           (final Boolean b, final FullFetchedLinkData d) -> {
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://medium.com/france/praha-8e7086a6c1fe",
                           (final Boolean b, final FullFetchedLinkData d) -> {
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
