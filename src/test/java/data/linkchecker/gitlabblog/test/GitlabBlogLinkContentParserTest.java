package data.linkchecker.gitlabblog.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.gitlabblog.GitlabBlogLinkContentParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class GitlabBlogLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/|Understand how your teams adopt DevOps with DevOps reports",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/|Write a stageless CI/CD pipeline using GitLab 14.2",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/|Top ten GitLab hacks for all stages of the DevOps Platform",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/|Get started with GitLab's Helm Package Registry"
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/,2021-12-15",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/,2021-08-24",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/,2021-10-19",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/,2021-10-18"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedDate, parser.getDateInternal().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2021/12/15/devops-adoption/,Orit,,Golowinski",
        "https://about.gitlab.com/blog/2021/08/24/stageless-pipelines/,Dov,,Hershkovitch",
        "https://about.gitlab.com/blog/2021/10/19/top-10-gitlab-hacks/,Michael,,Friedrich",
        "https://about.gitlab.com/blog/2021/10/18/improve-cd-workflows-helm-chart-registry/,Philip,,Welz"
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
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(1, parser.getAuthors().size());
                                   Assertions.assertEquals(expectedAuthor, parser.getAuthors().get(0));
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getAuthor threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://about.gitlab.com/blog/2021/09/23/best-practices-customer-feature-request/,Christina,,Hupy,Neil,,McCorrison",
        "https://about.gitlab.com/blog/2021/09/29/why-we-spent-the-last-month-eliminating-postgresql-subtransactions/,Stan,,Hu,Grzegorz,,Bizon"
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
                          (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFile().isPresent());
                              final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                              final GitlabBlogLinkContentParser parser = new GitlabBlogLinkContentParser(url, data);
                              try {
                                  Assertions.assertEquals(2, parser.getAuthors().size());
                                  Assertions.assertEquals(expectedAuthor1, parser.getAuthors().get(0));
                                  Assertions.assertEquals(expectedAuthor2, parser.getAuthors().get(1));
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getAuthor threw " + e.getMessage());
                               }
                              consumerHasBeenCalled.set(true);
          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
