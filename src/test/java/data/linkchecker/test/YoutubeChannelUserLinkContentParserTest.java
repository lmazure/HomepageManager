package data.linkchecker.test;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentParser;
import utils.HtmlHelper;

public class YoutubeChannelUserLinkContentParserTest {

    @Test
    void testErrorMessagePresent() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/channel/UCwBn4dgV3kxzvcCKN3TbQOQ"),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            Assertions.assertTrue(parser.getErrorMessage().isPresent());
                            Assertions.assertEquals("This channel does not exist.", parser.getErrorMessage().get());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testErrorMessageAbsent() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL("https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA"),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            Assertions.assertTrue(parser.getErrorMessage().isEmpty());
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA",
            "https://www.youtube.com/channel/UClq42foiSgl7sSpLupnugGA",
            "https://www.youtube.com/channel/UCZYTClx2T1of7BRZ86-8fow",
            "https://www.youtube.com/user/Vsauce2",
            "https://www.youtube.com/user/Vsauce3"
                           })
    void testEnglish(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            try {
                                Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage().get());
                            } catch (final ContentParserException e) {
                                Assertions.fail("getLanguage threw " + e.getMessage());
                            }
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/channel/UCjsHDXUU3BjBCG7OaCbNDyQ",
            "https://www.youtube.com/user/TheWandida"
                           })
    void testFrench(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                            Assertions.assertTrue(d.getDataFile().isPresent());
                            final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            try {
                                Assertions.assertEquals(Locale.FRENCH, parser.getLanguage().get());
                            } catch (final ContentParserException e) {
                                Assertions.fail("getLanguage threw " + e.getMessage());
                            }
                            consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
