package fr.mazure.homepagemanager.data.linkchecker.chromium.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.chromium.ChromiumBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of ChromiumBlogLinkContentParser
 */
class ChromiumBlogLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html|Tabbed Browsing in Google Chrome",
        "https://blog.chromium.org/2020/04/keeping-spam-off-chrome-web-store.html|Keeping spam off the Chrome Web Store",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(ChromiumBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2019/05/improving-privacy-and-security-on-web.html|Improving privacy and security on the web",
    }, delimiter = '|')
    void testTrimmedTitle(final String url,
                          final String expectedTitle) {
        checkTitle(ChromiumBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2010/05/security-in-depth-html5s-sandbox.html|Security in Depth: HTML5’s @sandbox",
        "https://blog.chromium.org/2020/08/chromeosdev-blueprint-to-build-world.html|ChromeOS.dev — A blueprint to build world-class apps and games for Chrome OS",
        "https://blog.chromium.org/2018/09/the-capable-web-10-year-retrospective.html|The ‘Capable Web’: A 10 Year Retrospective",
        "https://blog.chromium.org/2010/09/web-graphics-past-present-and-future.html|Web Graphics – Past, Present and Future",
        }, delimiter = '|')
    void testTitleWithSpecialCharacter(final String url,
                                       final String expectedTitle) {
        checkTitle(ChromiumBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html|2009-01-06",
        "https://blog.chromium.org/2020/04/keeping-spam-off-chrome-web-store.html|2020-04-29",
    }, delimiter = '|')
    void testPublishDate(final String url,
                         final String expectedPublicationDate) {
        checkDate(ChromiumBlogLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2021/04/dont-copy-that-surface.html",
        "https://blog.chromium.org/2018/09/10-years-of-chrome-devtools.html",
    }, delimiter = '|')
    void testNoAuthor(final String url) {
        check0Author(ChromiumBlogLinkContentParser.class,
                     url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2010/08/chromium-graphics-overhaul.html|Vangelis||Kokkevis",
        "https://blog.chromium.org/2011/10/heading-in-right-direction-with-webgl.html|Jennifer||Maurer",
        "https://blog.chromium.org/2021/03/mitigating-side-channel-attacks.html|Mike||West",
        "https://blog.chromium.org/2011/06/new-chromium-security-features-june.html|Chris||Evans",
        "https://blog.chromium.org/2020/07/using-chrome-to-generate-more.html|Dominic||Mazzoni",
        "https://blog.chromium.org/2012/08/lossless-and-transparency-modes-in-webp.html|Jyrki||Alakuijala",
        "https://blog.chromium.org/2011/01/more-about-chrome-html-video-codec.html|Mike||Jazayeri",
        "https://blog.chromium.org/2011/04/protecting-users-from-malicious.html|Moheeb|Abu|Rajab",
        "https://blog.chromium.org/2011/07/using-cross-domain-images-in-webgl-and.html|Eric||Bidelman",
        "https://blog.chromium.org/2012/02/more-secure-extensions-by-default.html|Adam||Barth",
        "https://blog.chromium.org/2017/04/scroll-anchoring-for-web-developers.html|Steve||Kobes",
        "https://blog.chromium.org/2017/05/goodbye-pnacl-hello-webassembly.html|Brad||Nelson",
        "https://blog.chromium.org/2018/09/10-years-of-speed-in-chrome_11.html|Addy||Osmani",
        "https://blog.chromium.org/2018/09/how-we-designed-chrome-10-years-ago.html|Mustafa||Kurtuldu",
        "https://blog.chromium.org/2018/09/the-capable-web-10-year-retrospective.html|Paul||Kinlan",
        "https://blog.chromium.org/2018/10/trustworthy-chrome-extensions-by-default.html|James||Wagner",
        "https://blog.chromium.org/2018/11/our-commitment-to-more-capable-web.html|Pete||LePage",
        "https://blog.chromium.org/2022/09/speeding-up-chrome-on-android-startup.html|Calder||Kitagawa",
        "https://blog.chromium.org/2021/05/adjusted-timeline-for-sharedarraybuffers.html|Lutz||Vahl",
        "https://blog.chromium.org/2020/10/progress-on-privacy-sandbox-and.html|Justin||Schuh",
        "https://blog.chromium.org/2020/09/changing-chrome-on-ios-user-agent-for.html|Gauthier||Ambard",
        "https://blog.chromium.org/2020/09/a-safer-and-more-private-browsing.html|Kenji||Baheux",
        //"https://blog.chromium.org/2020/08/protecting-google-chrome-users-from.html|Shweta||Panditrao", //TODO <span>s are a complete mess!
        "https://blog.chromium.org/2020/06/improving-chromiums-browser.html|Stephen||McGruer",
        "https://blog.chromium.org/2020/05/protecting-chrome-users-from-abusive.html|PJ||McLachlan",
    }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(ChromiumBlogLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2010/09/unleashing-gpu-acceleration-on-web.html|James||Robinson|Gregg||Tavares",
        "https://blog.chromium.org/2018/11/chrome-dev-summit-2018-building-faster.html|Ben||Galbraith|Dion||Almaer",
        //"https://blog.chromium.org/2020/05/celebrating-10-years-of-webm-and-webrtc.html|Matt||Frost|Niklas||Blum", //TODO <span>s are a complete mess!
        "https://blog.chromium.org/2019/04/data-saver-is-now-lite-mode.html|Ben||Greenstein|Nancy||Gao",
        "https://blog.chromium.org/2012/05/tale-of-two-pwnies-part-1.html|Jorge|Lucangeli|Obes|Justin||Schuh",
        "https://blog.chromium.org/2011/02/amping-up-chromes-background-feature.html|Andrew||Wilson|Michael||Mahemoff",
        "https://blog.chromium.org/2012/04/fuzzing-for-security.html|Abhishek||Arya|Cris||Neckar",
        "https://blog.chromium.org/2015/03/new-javascript-techniques-for-rapid.html|Marja||Hölttä|Daniel||Vogelheim",
        "https://blog.chromium.org/2019/03/chrome-lite-pages-for-faster-leaner.html|Ben||Greenstein|Nancy||Gao",
        "https://blog.chromium.org/2019/05/taking-action-on-deceptive-installation.html|Swagateeka||Panigrahy|Benjamin||Ackerman",
        //"https://blog.chromium.org/2023/04/more-ways-were-making-chrome-faster.html|Thomas||Nattestad|Andrew||Grieve", //TODO <span>s are a complete mess!
        "https://blog.chromium.org/2020/11/transparent-privacy-practices.html|Alexandre||Blondin|Mark|M.|Jaycox",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(ChromiumBlogLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
                      // author 2
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://blog.chromium.org/2015/04/a-quic-update-on-googles-experimental.html|Alyssa||Wilk|Ryan||Hamilton|Ian||Swett",
            "https://blog.chromium.org/2019/10/automatically-lazy-loading-offscreen.html|Addy||Osmani|Scott||Little|Raj||T",
            "https://blog.chromium.org/2019/11/moving-towards-faster-web.html|Addy||Osmani|Ben||Greenstein|Bryan||McQuade",
            //"https://blog.chromium.org/2020/08/helping-people-spot-spoofs-url.html|Emily||Stark|Eric||Mill|Shweta||Panditrao", //TODO <span>s are a complete mess!
    }, delimiter = '|')
    void test3Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2,
                      final String expectedFirstName3,
                      final String expectedMiddleName3,
                      final String expectedLastName3) {
        check3Authors(ChromiumBlogLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
                      // author 2
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      null,
                      // author 3
                      null,
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://blog.chromium.org/2012/06/tale-of-two-pwnies-part-2.html|Ken||Buchanan|Chris||Evans|Charlie||Reis|Tom||Sepez",
    }, delimiter = '|')
    void test4Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2,
                      final String expectedFirstName3,
                      final String expectedMiddleName3,
                      final String expectedLastName3,
                      final String expectedFirstName4,
                      final String expectedMiddleName4,
                      final String expectedLastName4) {
        check4Authors(ChromiumBlogLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      null,
                      null,
                      // author 2
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      null,
                      null,
                      // author 3
                      null,
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      null,
                      null,
                      // author 4
                      null,
                      expectedFirstName4,
                      expectedMiddleName4,
                      expectedLastName4,
                      null,
                      null);
    }
}
