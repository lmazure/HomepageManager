package fr.mazure.homepagemanager.data.linkchecker.githubblog.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of GithubBlogLinkContentParser
 */
class GithubBlogLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://github.blog/2022-10-03-highlights-from-git-2-38/|Highlights from Git 2.38",
        // the next articles have a title with an encoded character
        "https://github.blog/2022-08-29-gits-database-internals-i-packed-object-store/|Git’s database internals I: packed object store",
        "https://github.blog/2022-09-02-gits-database-internals-v-scalability/|Git’s database internals V: scalability",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(GithubBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://github.blog/2022-10-03-highlights-from-git-2-38/|Another new release of Git is here! Take a look at some of our highlights on what’s new in Git 2.38.",
        // the following articles have a different subtitle in the JSON payload and the HTML content
        "https://github.blog/2022-01-24-highlights-from-git-2-35/|The open source Git project just released Git 2.35. Here’s GitHub’s look at some of the most interesting features and changes introduced since last time.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(GithubBlogLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        // the following articles have a subtitle which is, in fact, the beginning of the article
        "https://github.blog/2015-12-15-move-fast/",
        "https://github.blog/2020-12-21-get-up-to-speed-with-partial-clone-and-shallow-clone/",
        })
    void testNoSubtitle(final String url) {
        checkNoSubtitle(GithubBlogLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,2022-10-03",
        })
    void testDate(final String url,
                  final String expectedDate) {
        checkDate(GithubBlogLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,Taylor,,Blau",
        // the next article has an author name with a particle
        "https://github.blog/2022-08-15-the-next-step-for-lgtm-com-github-code-scanning/,Bas,,van Schaik",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(GithubBlogLinkContentParser.class,
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
    @CsvSource({
        "https://github.blog/2023-09-12-codeql-team-uses-ai-to-power-vulnerability-detection-in-code/,Walker,,Chabbott,Florin,,Coada",
        "https://github.blog/2022-02-14-include-diagrams-markdown-files-mermaid/,Martin,,Woodward,Adam,,Biagianti",
        })
    void testTwoAuthors(final String url,
                        final String expectedFirstName1,
                        final String expectedMiddleName1,
                        final String expectedLastName1,
                        final String expectedFirstName2,
                        final String expectedMiddleName2,
                        final String expectedLastName2) {
        check2Authors(GithubBlogLinkContentParser.class,
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
}
