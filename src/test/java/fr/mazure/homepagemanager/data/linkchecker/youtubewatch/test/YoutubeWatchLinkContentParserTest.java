package fr.mazure.homepagemanager.data.linkchecker.youtubewatch.test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.internet.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of YoutubeWatchLinkContentParser
 */
public class YoutubeWatchLinkContentParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=_kGqkxQo-Tw",
            "https://www.youtube.com/watch?v=z34XhE5oRwo"
                            })
    void testPlayabilityStatusOk(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               Assertions.assertTrue(parser.isPlayable());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testPlayabilityStatusUnplayable() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=dM_JivN3HvI";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               Assertions.assertFalse(parser.isPlayable());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
            "https://www.youtube.com/watch?v=4AuV93LOPcE,Mathologer",
            "https://www.youtube.com/watch?v=zqTRdSUhgQw,Heu?reka",
            "https://www.youtube.com/watch?v=KT18KJouHWg,Veritasium",
            "https://www.youtube.com/watch?v=8JFyTubj30o,DeepSkyVideos",
            "https://www.youtube.com/watch?v=LJ4W1g-6JiY,Sabine Hossenfelder",
              })
    void testChannel(final String url,
                     final String expectedChannel) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals(expectedChannel, parser.getChannel());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getChannel threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitle() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=_kGqkxQo-Tw";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals("Alain Aspect - Le photon onde ou particule ? L’étrangeté quantique mise en lumière", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    @Disabled("I need to find another video with a control character, this one has been fixed")
    void testTitleWithControlCharacterString() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=y7FVLPvw1-I";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals("Spéciale \u0090Énigmes - Myriogon #7", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithAmpersand() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=ytuHV2e4c4Q";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                              Assertions.assertTrue(d.dataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                              final YoutubeWatchLinkContentParser parser = buildParser(data,url);
                              try {
                                  Assertions.assertEquals("Win a SMALL fortune with counting cards-the math of blackjack & Co.", parser.getTitle());
                              } catch (final ContentParserException e) {
                                  Assertions.fail("getTitle threw " + e.getMessage());
                              }
                              consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testTitleWithBackslashQuote() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=aeF-0y9HP9A";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals("Googling the Googlers\\' DNA: A Demonstration of the 23andMe Personal Genome S...", parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDescription() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=_kGqkxQo-Tw";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals("Conférence organisée par les Amis de l'IHES le 23 mai 2019", parser.getDescription());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getDescription threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDescriptionWithNewline() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=y7FVLPvw1-I";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals("Ce soir, on joue ensemble autour de quelques énigmes mathématiques.\n" +
                                           "\n" +
                                           "La chaîne Myriogon : https://www.youtube.com/channel/UCvYEpQbJ81n2pjrQrKUrRog/", parser.getDescription());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getDescription threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDescriptionWithDoubleQuote() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=cJOSvvdy27I";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
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
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getDescription threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDate() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=_kGqkxQo-Tw";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals(LocalDate.of(2019, 5, 27), parser.getUploadDateInternal());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getUploadDate threw " + e.getMessage());
                               }
                               try {
                                   Assertions.assertEquals(LocalDate.of(2019, 5, 27), parser.getPublishDateInternal());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getPublishDate threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testDuration() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.youtube.com/watch?v=_kGqkxQo-Tw";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals(5602, parser.getMinDuration().get(ChronoUnit.SECONDS));
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getMinDuration threw " + e.getMessage());
                               }
                               try {
                                   Assertions.assertEquals(5602, parser.getMaxDuration().get(ChronoUnit.SECONDS));
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getMaxDuration threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getLanguage threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
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
            "https://www.youtube.com/watch?v=ohU1tEwxOSE",
            // the following video has no description
            "https://www.youtube.com/watch?v=x4rj4MfNkys"
                           })
    void testFrench(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               try {
                                   Assertions.assertEquals(Locale.FRENCH, parser.getLanguage());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getLanguage threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
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
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               Assertions.assertEquals(Locale.ENGLISH, parser.getSubtitlesLanguage().get());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=_kGqkxQo-Tw",
            "https://www.youtube.com/watch?v=atKDrGedg_w",
            "https://www.youtube.com/watch?v=ohU1tEwxOSE"
                           })
    void testFrenchSubtitles(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               Assertions.assertEquals(Locale.FRENCH, parser.getSubtitlesLanguage().get());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=CfRSVPhzN5M"
                           })
    @Disabled("I need to find such examples")
    void testNoSubtitles(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final YoutubeWatchLinkContentParser parser = buildParser(data, url);
                               Assertions.assertTrue(parser.getSubtitlesLanguage().isEmpty());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void incorrectContentGeneratesException() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://www.google.com";
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               try {
                                   @SuppressWarnings("unused")
                                   final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(url, data);
                                   Assertions.fail("new YoutubeWatchLinkContentParser has not thrown an exception");
                               } catch (@SuppressWarnings("unused") final ContentParserException e) {
                                   // do nothing
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private static YoutubeWatchLinkContentParser buildParser(final String data,
                                                             final String url) {

        try {
            final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(url, data);
            return parser;
        } catch (final ContentParserException e) {
            Assertions.fail("new YoutubeWatchLinkContentParser threw " + e.getMessage());
        }

        return null;
    }
}