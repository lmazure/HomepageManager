package data.linkchecker.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
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

	@Test
	void testPlayabiltyStatusOk() {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/watch?v=_kGqkxQo-Tw"),
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
	void testPlayabiltyStatusUnplayable() {
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
	void testTitleWithDeviceControlString() {
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
	void testEnglish(String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("en", parser.getLanguage());
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"https://www.youtube.com/watch?v=16GlrK-bxaI",
			"https://www.youtube.com/watch?v=3QqR3AQe-SU",
			"https://www.youtube.com/watch?v=6s_zXFmWM6g",
			"https://www.youtube.com/watch?v=atKDrGedg_w",
			// bilingue "https://www.youtube.com/watch?v=FGjWkQePk20",
			"https://www.youtube.com/watch?v=fxTxU0Echq8",
			// bilingue "https://www.youtube.com/watch?v=iJfJPXI5EZQ",
            "https://www.youtube.com/watch?v=kiv32_P_T3k",
            "https://www.youtube.com/watch?v=lkdnOuzHdFE",
   			"https://www.youtube.com/watch?v=ohU1tEwxOSE"
                		   })
	void testFrench(String url) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = FileHelper.slurpFile(d.getDataFile().get());
                               final YoutubeWatchLinkContentParser parser = new YoutubeWatchLinkContentParser(data);
                               Assertions.assertEquals("fr", parser.getLanguage());
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
