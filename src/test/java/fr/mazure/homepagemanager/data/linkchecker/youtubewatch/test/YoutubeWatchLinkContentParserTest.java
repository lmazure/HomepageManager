package fr.mazure.homepagemanager.data.linkchecker.youtubewatch.test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.data.linkchecker.youtubewatch.YoutubeWatchLinkContentParser;

/**
 * Tests of YoutubeWatchLinkContentParser
 */
class YoutubeWatchLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=_kGqkxQo-Tw",
            "https://www.youtube.com/watch?v=z34XhE5oRwo",
                            })
    void testPlayabilityStatusOk(final String url) {
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertTrue(parser.isPlayable());
                        } catch (final ContentParserException e) {
                            Assertions.fail("isPlayable threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=EvknN89JoWo",
                            })
    void testPlayabilitySensibleVideo(final String url) {
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertTrue(parser.isPlayable());
                        } catch (final ContentParserException e) {
                            Assertions.fail("isPlayable threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @Test
    void testPlayabilityStatusUnplayable() {
        final String url = "https://www.youtube.com/watch?v=77nb34DB6Gs";
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertFalse(parser.isPlayable());
                        } catch (final ContentParserException e) {
                            Assertions.fail("isPlayable threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @Test
    void testPrivate() {
        final String url = "https://www.youtube.com/watch?v=xcV4bfEiucs";
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertTrue(parser.isPrivate());
                        } catch (final ContentParserException e) {
                            Assertions.fail("isPrivate threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
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
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertEquals(expectedChannel, parser.getChannel());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getChannel threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.youtube.com/watch?v=_kGqkxQo-Tw|Alain Aspect - Le photon onde ou particule ? L’étrangeté quantique mise en lumière",
            "https://www.youtube.com/watch?v=C2Tw0BeZb8Q|Scott Schiller: Web Audio - HTML5 + Flash (in a tree)",
            "https://www.youtube.com/watch?v=EcPPjZVB2vA|L'INCROYABLE HISTOIRE DE LA CONJECTURE DE FERMAT CMH#14",
            "https://www.youtube.com/watch?v=hcACC8LXokU|FLIP, L'émission quotidienne - Les Escapes Games",
              }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(YoutubeWatchLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleWithAmpersand() {
        final String url = "https://www.youtube.com/watch?v=ytuHV2e4c4Q";
        final String expectedTitle = "Win a SMALL fortune with counting cards-the math of blackjack & Co.";
        checkTitle(YoutubeWatchLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleWithBackslashQuote() {
        final String url = "https://www.youtube.com/watch?v=aeF-0y9HP9A";
        final String expectedTitle = "Googling the Googlers\\' DNA: A Demonstration of the 23andMe Personal Genome S...";
        checkTitle(YoutubeWatchLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testDescription() {
        final String url = "https://www.youtube.com/watch?v=_kGqkxQo-Tw";
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertEquals("Conférence organisée par les Amis de l'IHES le 23 mai 2019", parser.getDescription());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getDescription threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @Test
    void testDescriptionWithNewline() {
        final String url = "https://www.youtube.com/watch?v=y7FVLPvw1-I";
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertEquals("""
                             Ce soir, on joue ensemble autour de quelques énigmes mathématiques.

                             La chaîne Myriogon : https://www.youtube.com/channel/UCvYEpQbJ81n2pjrQrKUrRog/""", parser.getDescription());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getDescription threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @Test
    void testDescriptionWithDoubleQuote() {
        final String url = "https://www.youtube.com/watch?v=cJOSvvdy27I";
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertEquals("""
                             Watch Metallica perform "Master of Puppets" live on the Howard Stern Show.

                             Metallica's new album "Hardwired… to Self-Destruct" is available on Nov. 18.

                             Want to know what's going on with Howard Stern in the future?

                             Follow us on Twitter: http://bit.ly/1RzxGPD
                             On Facebook: http://on.fb.me/1JELtz3
                             On Instagram: https://goo.gl/VsWTND

                             For more great content from the Howard Stern Show visit our official website: http://www.HowardStern.com

                             Hear more Howard Stern by signing up for a free SiriusXM trial: https://goo.gl/uNL0Du""", parser.getDescription());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getDescription threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.youtube.com/watch?v=_kGqkxQo-Tw|2019-05-27",
        "https://www.youtube.com/watch?v=-hxeDjAxvJ8|2023-06-22",
    }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkDate(YoutubeWatchLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.youtube.com/watch?v=_kGqkxQo-Tw|2019-05-27|2019-05-27|2019-05-27||",
        "https://www.youtube.com/watch?v=-hxeDjAxvJ8|2023-06-22|2023-06-22|2023-06-22||",
        "https://www.youtube.com/watch?v=6LXw2beprGI|2023-11-29|2023-11-30|2023-11-30|2023-11-29|2023-11-30",
    }, delimiter = '|')
    void testAllDates(final String url,
                      final String expectedDate,
                      final String expectedPublishDate,
                      final String expectedUploadDate,
                      final String expectedStartBroadcastDate,
                      final String expectedEndBroadcastDate) {
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertEquals(expectedDate, parser.getDate().get().toString());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getDate threw " + e.getMessage());
                        }
                        try {
                            Assertions.assertEquals(expectedPublishDate, parser.getPublishDateInternal().toString());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getPublishDateInternal threw " + e.getMessage());
                        }
                        try {
                            Assertions.assertEquals(expectedUploadDate, parser.getUploadDateInternal().toString());
                        } catch (final ContentParserException e) {
                            Assertions.fail("geyUploadDateInternal threw " + e.getMessage());
                        }
                        try {
                            Assertions.assertEquals(Optional.ofNullable(expectedStartBroadcastDate), parser.getStartBroadcastDateInternal().map(LocalDate::toString));
                        } catch (final ContentParserException e) {
                            Assertions.fail("getStartBroadcastDateInternal threw " + e.getMessage());
                        }
                        try {
                            Assertions.assertEquals(Optional.ofNullable(expectedEndBroadcastDate), parser.getEndBroadcastDateInternal().map(LocalDate::toString));
                        } catch (final ContentParserException e) {
                            Assertions.fail("getEndBroadcastDateInternal threw " + e.getMessage());
                        }
                    });
    }

    @SuppressWarnings("static-method")
    @Test
    void testDurations() {
        final String url = "https://www.youtube.com/watch?v=_kGqkxQo-Tw";
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
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
                    });
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/watch?v=8idr1WZ1A7Q",
            "https://www.youtube.com/watch?v=hMBJSMaI_uE",
            "https://www.youtube.com/watch?v=MhTGX_ou1EM",
            "https://www.youtube.com/watch?v=sPQViNNOAkw",
            "https://www.youtube.com/watch?v=X63MWZIN3gM"
                           })
    void testEnglish(final String url) {
        checkLanguage(YoutubeWatchLinkContentParser.class, url, "en");
    }

    @SuppressWarnings("static-method")
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
        checkLanguage(YoutubeWatchLinkContentParser.class, url, "fr");
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
           "https://www.youtube.com/watch?v=_kGqkxQo-Tw,fr",
           "https://www.youtube.com/watch?v=-0ErpE8tQbw,de",
           "https://www.youtube.com/watch?v=-JcoFa5ieyA,vi",
           "https://www.youtube.com/watch?v=5NqbqTS9Ve0,hi",
           "https://www.youtube.com/watch?v=aeF-0y9HP9A,en",
           "https://www.youtube.com/watch?v=atKDrGedg_w,fr",
           "https://www.youtube.com/watch?v=d_bHo4nE_tE,nl",
           "https://www.youtube.com/watch?v=dQXVn7pFsVI,pt",
           "https://www.youtube.com/watch?v=HEfHFsfGXjs,nl",
           "https://www.youtube.com/watch?v=laty3vXKRek,ko",
           "https://www.youtube.com/watch?v=ohU1tEwxOSE,fr",
           "https://www.youtube.com/watch?v=oJTwQvgfgMM,de",
           "https://www.youtube.com/watch?v=QAU9psRDPZg,de",
           "https://www.youtube.com/watch?v=thT-RSEBxo8,vi",
           "https://www.youtube.com/watch?v=ytuHV2e4c4Q,en",
           })
    void testSubtitlesLanguage(final String url,
                               final String expectedLanguage) {
        perform(YoutubeWatchLinkContentParser.class,
                url,
                (final LinkDataExtractor p) ->
                    {
                        final YoutubeWatchLinkContentParser parser = (YoutubeWatchLinkContentParser)p;
                        try {
                            Assertions.assertEquals(Locale.forLanguageTag(expectedLanguage), parser.getSubtitlesLanguage().get());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getSubtitlesLanguage threw " + e.getMessage());
                        }
                    });
    }
}
