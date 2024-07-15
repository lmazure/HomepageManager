package fr.mazure.homepagemanager.data.linkchecker.githubblog.test;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.githubblog.GithubBlogLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of GithubBlogLinkContentParser
 */
public class GithubBlogLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://github.blog/2022-10-03-highlights-from-git-2-38/|Highlights from Git 2.38",
        // the next articles have a title with an encoded character
        "https://github.blog/2022-08-29-gits-database-internals-i-packed-object-store/|Git’s database internals I: packed object store",
        "https://github.blog/2022-09-02-gits-database-internals-v-scalability/|Git’s database internals V: scalability",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(url, data);
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
        "https://github.blog/2022-10-03-highlights-from-git-2-38/|Another new release of Git is here! Take a look at some of our highlights on what's new in Git 2.38.",
        // the following articles have a different subtitle in the JSON payload and the HTML content
        "https://github.blog/2022-01-24-highlights-from-git-2-35/|The open source Git project just released Git 2.35. Here's GitHub's look at some of the most interesting features and changes introduced since last time.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedSubtitle, parser.getSubtitle().get());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        // the following articles have a subtitle which is, in fact, the beginning of the article
        "https://github.blog/2015-12-15-move-fast/",
        "https://github.blog/2020-12-21-get-up-to-speed-with-partial-clone-and-shallow-clone/",
        })
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertTrue(parser.getSubtitle().isEmpty());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,2022-10-03",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(expectedDate, parser.getPublicationDate().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getPublicationDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

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
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(Collections.singletonList(expectedAuthor), parser.getSureAuthors());
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
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(url, data);
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
    }}
