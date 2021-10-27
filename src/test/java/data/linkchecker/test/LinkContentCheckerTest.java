package data.linkchecker.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentCheck;
import data.linkchecker.LinkContentChecker;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.LinkFormat;

public class LinkContentCheckerTest {

    @ParameterizedTest
    @CsvSource(value = {
            "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html|en|Tabbed Browsing in Google Chrome",
            "https://www-archive.mozilla.org/docs/web-developer/xbdhtml/xbdhtml.html|en|Introduction to Cross-Browser, Cross-Platform, Backwardly Compatible JavaScript and Dynamic HTML",
            "http://www.tcl.tk/doc/scripting.html|en|Scripting: Higher Level Programming for the 21st Century",
            "http://www.business-esolutions.com/islm.htm|en|Project Lifecycle Models: How They Differ and When to Use Them",
            "https://martinfowler.com/bliki/UseCasesAndStories.html|en|What is the difference between a UseCase and XP's UserStory?",
            "http://hesketh.com/publications/inclusive_web_design_for_the_future/|en|Inclusive Web Design For the Future with Progressive Enhancement",
            "https://blog.cleancoder.com/uncle-bob/2014/06/17/IsTddDeadFinalThoughts.html|en|Is TDD Dead? Final Thoughts about Teams."
            }, delimiter = '|')
    void testTitle(final String urlAsString,
                   final String locale,
                   final String expectedTitle) {
        final URL url = TestHelper.buildURL(urlAsString);
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[0], urlAsString, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, Optional.empty(), null);
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<AuthorData>(), null);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.getDataFile().get());
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertTrue(checks.isEmpty());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html,en,Tabbed browsing in Google Chrome,Tabbed Browsing in Google Chrome",
        "https://www.eiffel.com/values/design-by-contract/introduction/,en,Building bug-free O-O software: An introduction to Design by Contract,Building bug-free O-O software: An Introduction to Design by Contract"
        })
    void detectBadlyCasedTitle(final String urlAsString,
                               final String locale,
                               final String expectedTitle,
                               final String realTitle) {
        final URL url = TestHelper.buildURL(urlAsString);
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[0], urlAsString, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, Optional.empty(), null);
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<AuthorData>(), null);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.getDataFile().get());
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(1, checks.size());
                                   Assertions.assertEquals("title \"" + expectedTitle +"\" does not appear in the page, this is a problem of casing, the real title is \"" + realTitle + "\"",
                                                           checks.get(0).getDescription());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.liberation.fr/checknews/2020/04/16/covid-19-les-personnes-gueries-sont-elles-immunisees_1785420,fr,Covid-19 : les personnes guéries sont-elles immunisées ?,Covid-19 : les personnes guéries sont-elles immunisées ?"
        })
    void detectBadlySpacedTitle(final String urlAsString,
                                final String locale,
                                final String expectedTitle,
                                final String realTitle) {
        final URL url = TestHelper.buildURL(urlAsString);
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[0], urlAsString, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, Optional.empty(), null);
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<AuthorData>(), null);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.getDataFile().get());
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(1, checks.size());
                                   Assertions.assertEquals("title \"" + expectedTitle +"\" does not appear in the page, this is a problem of space, the real title is \"" + realTitle + "\"",
                                                           checks.get(0).getDescription());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
