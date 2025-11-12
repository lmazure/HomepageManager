package fr.mazure.homepagemanager.data.linkchecker.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentChecker;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkQuality;

/**
 * Tests of LinkContentParser
 */
class LinkContentCheckerTest {

    @ParameterizedTest
    @CsvSource(value = {
            "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html|en|Tabbed Browsing in Google Chrome",
            "https://www-archive.mozilla.org/docs/web-developer/xbdhtml/xbdhtml.html|en|Introduction to Cross-Browser, Cross-Platform, Backwardly Compatible JavaScript and Dynamic HTML",
            "http://www.business-esolutions.com/islm.htm|en|Project Lifecycle Models: How They Differ and When to Use Them",
            "https://martinfowler.com/bliki/UseCasesAndStories.html|en|What is the difference between a UseCase and XP's UserStory?",
            "http://hesketh.com/publications/inclusive_web_design_for_the_future/|en|Inclusive Web Design For the Future with Progressive Enhancement",
            "https://blog.cleancoder.com/uncle-bob/2014/06/17/IsTddDeadFinalThoughts.html|en|Is TDD Dead? Final Thoughts about Teams.",
            // use of &reg (i.e. no semicolon at the end) this is allowed, see https://stackoverflow.com/questions/15532252/why-is-reg-being-rendered-as-without-the-bounding-semicolon
            "https://www.rigacci.org/docs/biblio/online/CA-2000-02/CA-2000-02.html|en|CERT® Advisory CA-2000-02 Malicious HTML Tags Embedded in Client Web Requests",
            }, delimiter = '|')
    void testTitle(final String url,
                   final String locale,
                   final String expectedTitle) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[0], url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<>(), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertTrue(checks.isEmpty());
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
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html|en|Tabbed browsing in Google Chrome|Tabbed Browsing in Google Chrome",
        "https://www.eiffel.com/values/design-by-contract/introduction/|en|Building bug-free O-O software: An introduction to Design by Contract|Building bug-free O-O software: An Introduction to Design by Contract",
        }, delimiter = '|')
    void detectBadlyCasedTitle(final String url,
                               final String locale,
                               final String expectedTitle,
                               final String realTitle) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[0], url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<>(), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(1, checks.size());
                                   Assertions.assertEquals("Title \"" + expectedTitle +"\" does not appear in the page, this is a problem of casing, the real title is \"" + realTitle + "\"",
                                                           checks.get(0).getDescription());
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
            "https://www.lemondeinformatique.fr/actualites/lire-log4j-une-autre-vulnerabilite-corrigee-par-apache-85265.html|fr|Plus d'une semaine après la publication de la mise à jour 2.17 de la bibliothèque de journalisation Log4j d'Apache Logging, une faille CVE-2021-44832 l'affectant est comblée. La montée de version vers la 2.17.1 est à effectuer dès que possible.",
            "https://www.lemondeinformatique.fr/actualites/lire-le-document-d-ipo-de-gitlab-revele-un-avenir-ambitieux-84230.html|fr|La plateforme GitLab s'apprête à entrer en bourse, offrant ainsi aux équipes devops, sécurité, informatique dans les entreprises un moyen de collaborer au développement de logiciels.",
            }, delimiter = '|')
    void testSubtitle(final String url,
                      final String locale,
                      final String expectedSubtitle) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedSubtitle, new String[0], url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<>(), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertTrue(checks.isEmpty());
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
        "https://www.liberation.fr/checknews/2020/04/16/covid-19-les-personnes-gueries-sont-elles-immunisees_1785420|fr|Covid-19 : les personnes guéries sont-elles immunisées ?|Covid-19 : les personnes guéries sont-elles immunisées ?",
        }, delimiter = '|')
    void detectBadlySpacedTitle(final String url,
                                final String locale,
                                final String expectedTitle,
                                final String realTitle) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[0], url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<>(), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(1, checks.size());
                                   Assertions.assertEquals("Title \"" + expectedTitle +"\" does not appear in the page, this is a problem of space, the real title is \"" + realTitle + "\"",
                                                           checks.get(0).getDescription());
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
        "https://jml.io/your-code-sucks-and-i-hate-you/|en|Your code sucks, and I hate you|The Social Dynamics of Code Reviews|The Social Dynamics of Code Reviews",
        }, delimiter = '|')
    void detectBadlySpacedSubtitle(final String url,
                                   final String locale,
                                   final String expectedTitle,
                                   final String expectedSubtitle,
                                   final String realSubtitle) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(expectedTitle, new String[] { expectedSubtitle }, url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), new ArrayList<>(), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(1, checks.size());
                                   Assertions.assertEquals("Subtitle \"" + expectedSubtitle +"\" does not appear in the page, this is a problem of space, the real subtitle is \"" + realSubtitle + "\"",
                                                           checks.get(0).getDescription());
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
            "https://blog.google/technology/ai/google-gemini-ai/|en|Introducing Gemini: our largest and most capable AI model|Milad|Nasr",
            }, delimiter = '|')
    void testAuthor(final String url,
                    final String locale,
                    final String title,
                    final String firstName,
                    final String lastName) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(title, new String[0], url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final AuthorData author = new AuthorData(Optional.empty(), Optional.of(firstName), Optional.empty(), Optional.of(lastName), Optional.empty(), Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), Collections.singletonList(author), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(1, checks.size());
                                   Assertions.assertEquals("author \"" + lastName + "\" does not appear in the page",
                                                           checks.get(0).getDescription());
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
            // check proper management of ' and ’ (the two characters should be considered the same)
            "https://www.assemblyai.com/blog/emergent-abilities-of-large-language-models/|en|Emergent Abilities of Large Language Models|Ryan|O’Connor",
            }, delimiter = '|')
    void testAuthorPresent(final String url,
                           final String locale,
                           final String title,
                           final String firstName,
                           final String lastName) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final LinkData linkData = new LinkData(title, new String[0], url, null, null, new LinkFormat[] { LinkFormat.HTML }, new Locale[] { Locale.forLanguageTag(locale) }, LinkQuality.AVERAGE, Optional.empty(), null, Optional.empty());
        final AuthorData author = new AuthorData(Optional.empty(), Optional.of(firstName), Optional.empty(), Optional.of(lastName), Optional.empty(), Optional.empty());
        final ArticleData articleData = new ArticleData(Optional.empty(), Collections.singletonList(author), null, null);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final LinkContentChecker checker = new LinkContentChecker(url, linkData, Optional.of(articleData), d.dataFileSection().get(), retriever);
                               try {
                                   final List<LinkContentCheck> checks = checker.check();
                                   Assertions.assertEquals(0, checks.size());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
