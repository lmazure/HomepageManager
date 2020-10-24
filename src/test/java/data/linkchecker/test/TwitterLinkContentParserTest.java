package data.linkchecker.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.TwitterLinkContentParser;
import utils.FileHelper;

public class TwitterLinkContentParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://twitter.com/3blue1brown",
            "https://twitter.com/numberphile"
                           })
    void testEnglish(String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final TwitterLinkContentParser parser = new TwitterLinkContentParser(data);
                            Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://twitter.com/ElJj",
            "https://twitter.com/mickaellaunay"
                           })
    void testFrench(String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = FileHelper.slurpFile(d.getDataFile().get());
                            final TwitterLinkContentParser parser = new TwitterLinkContentParser(data);
                            Assertions.assertEquals(Locale.FRENCH, parser.getLanguage());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\test\\TwitterLinkContentParserTest");
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SynchronousSiteDataRetriever(new SiteDataPersister(cachePath));
    }
}
