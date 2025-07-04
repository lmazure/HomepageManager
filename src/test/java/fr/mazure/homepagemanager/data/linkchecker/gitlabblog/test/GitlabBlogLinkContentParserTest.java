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
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/|Understand how your teams adopt DevOps with DevOps reports",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/|Write a stageless CI/CD pipeline using GitLab 14.2",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/|Top ten GitLab hacks for all stages of the DevOps Platform",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/|Get started with GitLab's Helm Package Registry",
        "https://about.gitlab.com/blog/2023/08/10/learning-rust-with-a-little-help-from-ai-code-suggestions-getting-started/|Learning Rust with a little help from AI",
        "https://about.gitlab.com/blog/2023/08/28/sha256-support-in-gitaly/|GitLab Gitaly project now supports the SHA 256 hashing algorithm",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(GitlabBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/"
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(GitlabBlogLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/2020/11/11/gitlab-for-agile-portfolio-planning-project-management/|2020-11-11",
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/|2021-12-15",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/|2021-08-24",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/|2021-10-19",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/|2021-10-18",
        "https://about.gitlab.com/blog/2023/07/25/rail-m-is-an-imperfectly-good-start-for-ai-model-licenses/|2023-07-25",
        "https://about.gitlab.com/blog/2023/08/28/sha256-support-in-gitaly/|2023-08-28",
        "https://about.gitlab.com/blog/gitlab-release-process/|2015-12-17",
        "https://about.gitlab.com/blog/2024/12/05/gitlab-names-bill-staples-as-new-ceo/|2024-12-05",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(GitlabBlogLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/2023/09/28/unmasking-password-attacks-at-gitlab/"
        }, delimiter = '|')
    void testNoAuthors(final String url) {
        check0Author(GitlabBlogLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/|Orit||Golowinski",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/|Dov||Hershkovitch",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/|Michael||Friedrich",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/|Philip||Welz",
        "https://about.gitlab.com/blog/2023/08/28/sha256-support-in-gitaly/|John||Cai",
        "https://about.gitlab.com/blog/2024/02/14/new-report-on-ai-assisted-tools-points-to-rising-stakes-for-devsecops/|Rusty||Weston"
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
        "https://about.gitlab.com/blog/2021/09/23/best-practices-customer-feature-request/|Christina||Hupy|Neil||McCorrison",
        "https://about.gitlab.com/blog/2021/09/29/why-we-spent-the-last-month-eliminating-postgresql-subtransactions/|Grzegorz||Bizon|Stan||Hu",
        "https://about.gitlab.com/blog/2024/03/20/oxeye-joins-gitlab-to-advance-application-security-capabilities/|David||DeSanto|Dean||Agron"
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
