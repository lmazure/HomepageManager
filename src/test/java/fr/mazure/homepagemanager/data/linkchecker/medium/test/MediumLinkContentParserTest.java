package fr.mazure.homepagemanager.data.linkchecker.medium.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.medium.MediumLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of MediumLinkContentParser
 */
class MediumLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@kentbeck_7670/sipping-the-big-gulp-a7c50549c393|Kent|Beck|",
        "https://medium.com/@FibreTigre/mon-emploi-du-temps-2019-b4a44c2efa46|||FibreTigre",
        "https://sendilkumarn.medium.com/safevarargs-variable-arguments-in-java-b9fdd5d996bb|||sendilkumarn",
        // the next blog is from "Anuj shah (Exploring Neurons)"
        "https://medium.com/@anuj_shah/through-the-eyes-of-gabor-filter-17d1fdb3ac97|Anuj|Shah|",
        "https://medium.com/rahasak/build-rag-application-using-a-llm-running-on-local-computer-with-gpt4all-and-langchain-13b4b8851db8|||(λx.x)eranga",
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        check1Author(MediumLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     null,
                     expectedLastName,
                     null,
                     expectedGivenName);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://netflixtechblog.com/a-microscope-on-microservices-923b906103f4|Coburn|Watson|Scott|Emmons|Brendan|Gregg",
        }, delimiter = '|')
    void testNetflix3Authors(final String url,
                             final String expectedFirstName1,
                             final String expectedLastName1,
                             final String expectedFirstName2,
                             final String expectedLastName2,
                             final String expectedFirstName3,
                             final String expectedLastName3) {
        check3Authors(MediumLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      null,
                      expectedLastName1,
                      null,
                      null,
                      // author 2
                      null,
                      expectedFirstName2,
                      null,
                      expectedLastName2,
                      null,
                      null,
                      // author 3
                      null,
                      expectedFirstName3,
                      null,
                      expectedLastName3,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa|SB Changes",
        "https://medium.com/@kentbeck_7670/productive-compliments-giving-receiving-connecting-dda58570d96b|Productive Compliments: Giving, Receiving, Connecting",
        "https://medium.com/@kentbeck_7670/sipping-the-big-gulp-a7c50549c393|Sipping the Big Gulp: 2 Ways to Narrow an Interface",
        "https://medium.com/swlh/microservices-architecture-what-is-saga-pattern-and-how-important-is-it-55f56cfedd6b|[Microservices Architecture] What is SAGA Pattern and How important is it?",
        // the next articles seem to be old ones one where the paragraph IDs where not 4 hexadecimal numbers
        "https://medium.com/@docjamesw/the-anti-meeting-culture-c209bab5a16d|The Anti-Meeting Culture",
        "https://medium.com/@docjamesw/work-hard-youll-get-there-eventually-d4f4fc704820|Work Hard You’ll Get There Eventually",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // articles with a long subtitle that is truncated in the JSON payload
        "https://medium.com/devops-with-valentine/send-gitlab-ci-reports-artifacts-via-e-mail-86bc96e66511|Most users migrating from Jenkins to GitLab CI are looking for a way to send emails with the reports when a test fails. While Gitlab CI can notify you that a job has failed (or was successful) it will not attach any files or reports to that email.",
        // the next articles seem to be old ones one where the paragraph IDs where not 4 hexadecimal numbers
        "https://medium.com/@docjamesw/the-anti-meeting-culture-c209bab5a16d|Kill wasteful meetings before they kill you",
        "https://medium.com/@docjamesw/work-hard-youll-get-there-eventually-d4f4fc704820|(Hint: No You Won’t)",
        "https://medium.com/rahasak/build-rag-application-using-a-llm-running-on-local-computer-with-gpt4all-and-langchain-13b4b8851db8|Privacy-preserving LLM without GPU",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(MediumLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@bpnorlander/stop-writing-code-comments-28fef5272752",
        "https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa",
        "https://medium.com/@tdeniffel/tcr-test-commit-revert-a-test-alternative-to-tdd-6e6b03c22bec",
        "https://medium.com/@kentbeck_7670/limbo-scaling-software-collaboration-afd4f00db4b",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(MediumLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleWithAmpersandAndLink() {
        final String url = "https://medium.com/@tdeniffel/tcr-test-commit-revert-a-test-alternative-to-tdd-6e6b03c22bec";
        final String expectedTitle = "TCR (test && commit || revert). How to use? Alternative to TDD?";
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleWithGreater() {
        final String url = "https://medium.com/@kentbeck_7670/monolith-services-theory-practice-617e4546a879";
        final String expectedTitle = "Monolith -> Services: Theory & Practice";
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleWithSlash() {
        final String url = "https://medium.com/@kentbeck_7670/fast-slow-in-3x-explore-expand-extract-6d4c94a7539";
        final String expectedTitle = "Fast/Slow in 3X: Explore/Expand/Extract";
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // the hair space is in the JSON payload, but not in the HTML
        "https://medium.com/@kentbeck_7670/curiosity-as-a-service-literally-1f4f6309fae5|Curiosity as a Service — Literally",
        "https://medium.com/@specktackle/selenium-and-webdriverio-a-historical-overview-6f8fbf94b418|Selenium and WebdriverIO — A Historical Overview",
        }, delimiter = '|')
    void testTitleWithHairSpace(final String url,
                                final String expectedTitle) {
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleWithMultiline() {
        final String url = "https://medium.com/javascript-scene/how-to-build-a-high-velocity-development-team-4b2360d34021";
        final String expectedTitle = "How to Build a High Velocity Development Team";
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testTitleForNetflix() {
        final String url = "https://netflixtechblog.com/a-microscope-on-microservices-923b906103f4";
        final String expectedTitle = "A Microscope on Microservices";
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@kentbeck_7670/a-years-worth-c1cbc3085e9d|2019-06-08",
        "https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa|2019-05-21",
        "https://medium.com/@kentbeck_7670/buy-effort-sell-value-7a625345ad24|2019-05-10",
        "https://medium.com/@kentbeck_7670/curiosity-as-a-service-literally-1f4f6309fae5|2019-07-04",
        "https://medium.com/@kentbeck_7670/what-i-do-at-gusto-an-incentives-explanation-c7b4f79483ae|2020-05-02",
        "https://medium.com/@kentbeck_7670/software-design-is-human-relationships-part-3-of-3-changers-changers-20eeac7846e0|2019-07-18",
        }, delimiter = '|')
    void testUnmodifiedBlogPublishDate(final String url,
                                       final String expectedDate) {
        checkCreationDate(MediumLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@kentbeck_7670/sipping-the-big-gulp-a7c50549c393|2019-05-11|2019-05-21",
        "https://medium.com/97-things/optional-is-a-law-breaking-monad-but-a-good-type-7667eb821081|2019-07-18|2020-05-14",
        }, delimiter = '|')
    void testModifiedBlogPublishDate(final String url,
                                     final String expectedPublicationDate,
                                     @SuppressWarnings("unused") final String expectedModificationDate) {
        checkCreationDate(MediumLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://medium.com/@kentbeck_7670/bs-changes-e574bc396aaa|en",
        "https://medium.com/france/praha-8e7086a6c1fe|fr",
        "https://medium.com/@FibreTigre/mon-emploi-du-temps-2019-b4a44c2efa46|fr",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(MediumLinkContentParser.class, url, expectedLanguage);

    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://uxdesign.cc/the-dark-yellow-problem-in-design-system-color-palettes-a0db1eedc99d|The “dark yellow problem” in design system color palettes",
        "https://netflixtechblog.com/a-microscope-on-microservices-923b906103f4|A Microscope on Microservices",
        "https://blog.sparksuite.com/7-ways-to-speed-up-gitlab-ci-cd-times-29f60aab69f9|7 ways to speed up your GitLab CI/CD times",
        "https://levelup.gitconnected.com/git-worktrees-the-best-git-feature-youve-never-heard-of-9cd21df67baf|Git Worktrees: The Best Git Feature You’ve Never Heard Of",
        }, delimiter = '|')
    void testRedirectMechanism(final String url,
                               final String expectedTitle) {
        checkTitle(MediumLinkContentParser.class, url, expectedTitle);
    }
}
