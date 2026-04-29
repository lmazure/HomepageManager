package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;
import fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser.YoutubeChannelUserLinkContentParser;

/**
 * Tests of YoutubeChannelUserLinkContentParser
 */
class YoutubeChannelUserLinkContentParserTest extends LinkDataExtractorTestBase {


    @SuppressWarnings("static-method")
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/channel/UC6nSFpj9HTCZ5t-N3Rm3-HA",
            "https://www.youtube.com/channel/UClq42foiSgl7sSpLupnugGA",
            "https://www.youtube.com/channel/UCZYTClx2T1of7BRZ86-8fow",
            "https://www.youtube.com/user/Vsauce2",
            "https://www.youtube.com/user/Vsauce3",
       })
    void testEnglish(final String url) {
        checkLanguage(YoutubeChannelUserLinkContentParser.class, url, "en");
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @ValueSource(strings = {
            "https://www.youtube.com/channel/UCjsHDXUU3BjBCG7OaCbNDyQ",
            "https://www.youtube.com/user/TheWandida",
       })
    void testFrench(final String url) {
        checkLanguage(YoutubeChannelUserLinkContentParser.class, url, "fr");
    }
}
