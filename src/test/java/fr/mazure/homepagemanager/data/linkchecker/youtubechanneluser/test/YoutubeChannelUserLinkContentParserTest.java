package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser.test;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of YoutubeChannelUserLinkContentParser
 *
 */
public class YoutubeChannelUserLinkContentParserTest {

    @Test
    void testErrorMessagePresent() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://www.youtube.com/channel/UCwBn4dgV3kxzvcCKN3TbQOQ",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            Assertions.assertTrue(parser.getErrorMessage().isPresent());
                            Assertions.assertEquals("This channel does not exist.", parser.getErrorMessage().get());
                            consumerHasBeenCalled.set(true);
                           },
                           true);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void testErrorMessageAbsent() {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve("https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA",
                           (final Boolean b, final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            Assertions.assertTrue(parser.getErrorMessage().isEmpty());
                            consumerHasBeenCalled.set(true);
                           },
                           true);
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
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            try {
                                Assertions.assertEquals(Locale.ENGLISH, parser.getLanguage().get());
                            } catch (final ContentParserException e) {
                                Assertions.fail("getLanguage threw " + e.getMessage());
                            }
                            consumerHasBeenCalled.set(true);
                           },
                           true);
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
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                            Assertions.assertTrue(d.dataFileSection().isPresent());
                            final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                            final YoutubeChannelUserLinkContentParser parser = new YoutubeChannelUserLinkContentParser(data);
                            try {
                                Assertions.assertEquals(Locale.FRENCH, parser.getLanguage().get());
                            } catch (final ContentParserException e) {
                                Assertions.fail("getLanguage threw " + e.getMessage());
                            }
                            consumerHasBeenCalled.set(true);
                           },
                           true);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
