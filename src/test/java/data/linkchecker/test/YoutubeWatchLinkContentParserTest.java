package data.linkchecker.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
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
import data.linkchecker.YoutubeWatchLinkContentParser;
import utils.FileHelper;

class YoutubeWatchLinkContentParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=_kGqkxQo-Tw",
            "https://www.youtube.com/watch?v=z34XhE5oRwo"
                            })
    void testPlayabilityStatusOk(final String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertTrue(parser.isPlayable());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testPlayabilityStatusUnplayable() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=dM_JivN3HvI"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertFalse(parser.isPlayable());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitle() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=_kGqkxQo-Tw"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("Alain Aspect - Le photon onde ou particule ? L’étrangeté quantique mise en lumière", parser.getTitle());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    @Disabled // I need to find another video with a control character, this one has been fixed
    void testTitleWithControlCharacterString() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=y7FVLPvw1-I"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("Spéciale \u0090Énigmes - Myriogon #7", parser.getTitle());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithAmpersand() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=ytuHV2e4c4Q"),
                           (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFile().isPresent());
                              final String data = FileHelper.slurpFile(d.getDataFile().get());
                              final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                              Assertions.assertEquals("Win a SMALL fortune with counting cards-the math of blackjack & Co.", parser.getTitle());
                              consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithBackslashQuote() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=aeF-0y9HP9A"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("Googling the Googlers\\' DNA: A Demonstration of the 23andMe Personal Genome S...", parser.getTitle());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDescription() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=_kGqkxQo-Tw"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("Conférence organisée par les Amis de l'IHES le 23 mai 2019", parser.getDescription());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDescriptionWithNewline() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=y7FVLPvw1-I"),
                              (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("Ce soir, on joue ensemble autour de quelques énigmes mathématiques.\n" +
                                                       "\n" +
                                                       "La chaîne Myriogon : https://www.youtube.com/channel/UCvYEpQbJ81n2pjrQrKUrRog/", parser.getDescription());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDescriptionWithDoubleQuote() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=cJOSvvdy27I"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("Watch Metallica perform \"Master of Puppets\" live on the Howard Stern Show.\n" +
                                                       "\n" +
                                                       "Metallica's new album \"Hardwired… to Self-Destruct\" is available on Nov. 18.\n" +
                                                       "\n" +
                                                       "Want to know what's going on with Howard Stern in the future?\n" +
                                                       "\n" +
                                                       "Follow us on Twitter: http://bit.ly/1RzxGPD\n" +
                                                       "On Facebook: http://on.fb.me/1JELtz3\n" +
                                                       "On Instagram: https://goo.gl/VsWTND\n" +
                                                       "\n" +
                                                       "For more great content from the Howard Stern Show visit our official website: http://www.HowardStern.com\n" +
                                                       "\n" +
                                                       "Hear more Howard Stern by signing up for a free SiriusXM trial: https://goo.gl/uNL0Du", parser.getDescription());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDate() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=_kGqkxQo-Tw"),
                              (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals(LocalDate.of(2019, 5, 27), parser.getUploadDate());
                               Assertions.assertEquals(LocalDate.of(2019, 5, 27), parser.getPublishDate());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDuration() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=_kGqkxQo-Tw"),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals(Duration.ofMillis(5602280), parser.getMinDuration());
                               Assertions.assertEquals(Duration.ofMillis(5602348), parser.getMaxDuration());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=8idr1WZ1A7Q",
            "https://www.youtube.com/watch?v=hMBJSMaI_uE",
            "https://www.youtube.com/watch?v=MhTGX_ou1EM",
            "https://www.youtube.com/watch?v=sPQViNNOAkw",
            "https://www.youtube.com/watch?v=X63MWZIN3gM"
                           })
    void testEnglish(final String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage().get());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=16GlrK-bxaI",
            "https://www.youtube.com/watch?v=3QqR3AQe-SU",
            "https://www.youtube.com/watch?v=6s_zXFmWM6g",
            "https://www.youtube.com/watch?v=AT89_nM0nes",
            "https://www.youtube.com/watch?v=atKDrGedg_w",
            "https://www.youtube.com/watch?v=ccB09XRdGjo",
            // description bilingue "https://www.youtube.com/watch?v=FGjWkQePk20",
            "https://www.youtube.com/watch?v=fxTxU0Echq8",
            // description bilingue "https://www.youtube.com/watch?v=hGbrH8DGzRg",
            // description bilingue "https://www.youtube.com/watch?v=iJfJPXI5EZQ",
            // description bilingue "https://www.youtube.com/watch?v=k0-5T_oDt1E",
            "https://www.youtube.com/watch?v=kiv32_P_T3k",
            "https://www.youtube.com/watch?v=lkdnOuzHdFE",
            // vidéo bilingue "https://www.youtube.com/watch?v=nhDpozSK0uw",
            "https://www.youtube.com/watch?v=ohU1tEwxOSE"
                           })
    void testFrench(final String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals(Locale.FRENCH, parser.getLanguage().get());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=-0ErpE8tQbw",
            "https://www.youtube.com/watch?v=8idr1WZ1A7Q",
            "https://www.youtube.com/watch?v=aeF-0y9HP9A",
            "https://www.youtube.com/watch?v=ytuHV2e4c4Q"
                           })
    void testEnglishSubtitles(final String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals(Locale.ENGLISH, parser.getSubtitlesLanguage().get());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=_kGqkxQo-Tw",
            "https://www.youtube.com/watch?v=atKDrGedg_w",
            "https://www.youtube.com/watch?v=ohU1tEwxOSE"
                           })
    void testFrenchSubtitles(final String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals(Locale.FRENCH, parser.getSubtitlesLanguage().get());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    //@Disabled // TBD I need to find such examples
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=CfRSVPhzN5M"
                           })
    void testNoSubtitles(final String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertTrue(parser.getSubtitlesLanguage().isEmpty());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\test\\YoutubeWatchLinkContentParserTest");
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SynchronousSiteDataRetriever(new SiteDataPersister(cachePath));
    }
}
