package data.linkchecker.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.LinkContentParser;
import utils.FileHelper;

public class LinkContentParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
             "http://safsdev.sourceforge.net/FRAMESDataDrivenTestAutomationFrameworks.htm",
             "https://viterbi-web.usc.edu/~meshkati/tefall99/toki.html",
             "https://www.cultdeadcow.com",
             "https://www.wired.com/1998/04/es-lists/"
            })
    void testEnglishArticle(String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
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
             "https://www.marmiton.org/",
             "http://www.wazabi-online.com/"
            })
    void testFrenchArticle(String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertEquals(Locale.FRENCH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testEnglishMedium() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa"),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testFrenchMedium() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://medium.com/france/praha-8e7086a6c1fe"),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertEquals(Locale.FRENCH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    @Disabled
    void testEnglishTwitter() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://twitter.com/3blue1brown"),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    @Disabled
    void testFrenchTwitter() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://twitter.com/mickaellaunay"),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final LinkContentParser parser = new LinkContentParser(data);
                            Assertions.assertEquals(Locale.FRENCH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\test\\LinkContentParserTest");
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SynchronousSiteDataRetriever(new SiteDataPersister(cachePath));
    }
}
