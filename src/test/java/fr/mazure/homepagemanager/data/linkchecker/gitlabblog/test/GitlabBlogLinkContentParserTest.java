package fr.mazure.homepagemanager.data.linkchecker.gitlabblog.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of GitlabBlogLinkContentParser
 */
class GitlabBlogLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/making-room-for-whats-next-in-the-gitlab-ui/|Making room for what's next in the GitLab UI",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(GitlabBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/making-room-for-whats-next-in-the-gitlab-ui/|A look at what's changing in the GitLab UI, why, and what we're building toward.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(GitlabBlogLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/making-room-for-whats-next-in-the-gitlab-ui/|2026-08-26",
        // the next article has been updated
        "https://about.gitlab.com/blog/understanding-flows-multi-agent-workflows/|2026-01-14"
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(GitlabBlogLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/ai-is-reshaping-devsecops-attend-gitlab-transcend-to-see-whats-next/|Manav||Khurana"
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(GitlabBlogLinkContentParser.class,
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
        "https://about.gitlab.com/blog/gitlab-next-gen-scm/|Kranthi||Erusu|Jessica||Taylor"
        }, delimiter = '|')
    void testTwoAuthors(final String url,
                        final String expectedFirstName1,
                        final String expectedMiddleName1,
                        final String expectedLastName1,
                        final String expectedFirstName2,
                        final String expectedMiddleName2,
                        final String expectedLastName2) {
        check2Authors(GitlabBlogLinkContentParser.class,
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
