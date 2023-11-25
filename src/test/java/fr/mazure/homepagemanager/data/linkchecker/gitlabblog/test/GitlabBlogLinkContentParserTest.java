package fr.mazure.homepagemanager.data.linkchecker.gitlabblog.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.gitlabblog.GitlabBlogLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of GitlabBlogLinkContentParser
 *
 */
public class GitlabBlogLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/|Understand how your teams adopt DevOps with DevOps reports",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/|Write a stageless CI/CD pipeline using GitLab 14.2",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/|Top ten GitLab hacks for all stages of the DevOps Platform",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/|Get started with GitLab's Helm Package Registry",
        "https://about.gitlab.com/blog/2023/08/10/learning-rust-with-a-little-help-from-ai-code-suggestions-getting-started/|Learning Rust with a little help from AI",
        "https://about.gitlab.com/blog/2023/08/28/sha256-support-in-gitaly/|GitLab Gitaly project now supports the SHA-256 hashing algorithm",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
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
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/"
        })
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               Assertions.assertFalse(parser.getSubtitle().isPresent());
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2020/11/11/gitlab-for-agile-portfolio-planning-project-management/,2020-11-11",
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/,2021-12-15",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/,2021-08-24",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/,2021-10-19",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/,2021-10-18",
        "https://about.gitlab.com/blog/2023/07/25/rail-m-is-an-imperfectly-good-start-for-ai-model-licenses/,2023-07-25",
        "https://about.gitlab.com/blog/2023/08/28/sha256-support-in-gitaly/,2023-08-28",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               try {
                                   TestHelper.assertDate(expectedDate, parser.getDate());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2023/09/28/unmasking-password-attacks-at-gitlab/",
        })
    void testNoAuthors(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(0, parser.getSureAuthors().size());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/,Orit,,Golowinski",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/,Dov,,Hershkovitch",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/,Michael,,Friedrich",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/,Philip,,Welz",
        "https://about.gitlab.com/blog/2023/08/28/sha256-support-in-gitaly/,John,,Cai",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.of(expectedFirstName),
                                                         Optional.ofNullable(expectedMiddleName),
                                                         Optional.of(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(1, parser.getSureAuthors().size());
                                   Assertions.assertEquals(expectedAuthor, parser.getSureAuthors().get(0));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getSureAuthors threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2021/09/23/best-practices-customer-feature-request/,Christina,,Hupy,Neil,,McCorrison",
        "https://about.gitlab.com/blog/2021/09/29/why-we-spent-the-last-month-eliminating-postgresql-subtransactions/,Stan,,Hu,Grzegorz,,Bizon",
        })
    void testTwoAuthors(final String url,
                        final String expectedFirstName1,
                        final String expectedMiddleName1,
                        final String expectedLastName1,
                        final String expectedFirstName2,
                        final String expectedMiddleName2,
                        final String expectedLastName2) {
        final AuthorData expectedAuthor1 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName1),
                                                          Optional.ofNullable(expectedMiddleName1),
                                                          Optional.of(expectedLastName1),
                                                          Optional.empty(),
                                                          Optional.empty());
        final AuthorData expectedAuthor2 = new AuthorData(Optional.empty(),
                                                          Optional.of(expectedFirstName2),
                                                          Optional.ofNullable(expectedMiddleName2),
                                                          Optional.of(expectedLastName2),
                                                          Optional.empty(),
                                                          Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                          (final Boolean b, final FullFetchedLinkData d) -> {
                              Assertions.assertTrue(d.dataFileSection().isPresent());
                              final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                              final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                              try {
                                  Assertions.assertEquals(2, parser.getSureAuthors().size());
                                  Assertions.assertEquals(expectedAuthor1, parser.getSureAuthors().get(0));
                                  Assertions.assertEquals(expectedAuthor2, parser.getSureAuthors().get(1));
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSureAuthors threw " + e.getMessage());
                               }
                              consumerHasBeenCalled.set(true);
                          },
                          false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
