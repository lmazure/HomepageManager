package fr.mazure.homepagemanager.data.linkchecker.chromium.test;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.chromium.ChromiumBlogLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Tests of ChromiumBlogLinkContentParser
 */
class ChromiumBlogLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html,Tabbed Browsing in Google Chrome",
        "https://blog.chromium.org/2020/04/keeping-spam-off-chrome-web-store.html,Keeping spam off the Chrome Web Store",
        })
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ChromiumBlogLinkContentParser parser = new ChromiumBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blog.chromium.org/2019/05/improving-privacy-and-security-on-web.html,Improving privacy and security on the web",
        })
    void testTrimmedTitle(final String url,
                          final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ChromiumBlogLinkContentParser parser = new ChromiumBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.chromium.org/2010/05/security-in-depth-html5s-sandbox.html|Security in Depth: HTML5’s @sandbox",
        "https://blog.chromium.org/2020/08/chromeosdev-blueprint-to-build-world.html|ChromeOS.dev — A blueprint to build world-class apps and games for Chrome OS",
        "https://blog.chromium.org/2018/09/the-capable-web-10-year-retrospective.html|The ‘Capable Web’: A 10 Year Retrospective",
        "https://blog.chromium.org/2010/09/web-graphics-past-present-and-future.html|Web Graphics – Past, Present and Future",
        }, delimiter = '|')
    void testTitleWithSpecialCharacter(final String url,
                                       final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ChromiumBlogLinkContentParser parser = new ChromiumBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
    @ParameterizedTest
    @CsvSource({
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html,2009-01-06",
        "https://blog.chromium.org/2020/04/keeping-spam-off-chrome-web-store.html,2020-04-29",
        })
    void testPublishDate(final String url,
                         final String expectedPublicationDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final ChromiumBlogLinkContentParser parser = new ChromiumBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedPublicationDate, parser.getPublicationDate().toString());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getPublicationDate threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
