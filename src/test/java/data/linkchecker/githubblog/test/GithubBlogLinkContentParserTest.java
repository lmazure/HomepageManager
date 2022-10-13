package data.linkchecker.githubblog.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.githubblog.GithubBlogLinkContentParser;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public class GithubBlogLinkContentParserTest {

    @ParameterizedTest
    @CsvSource({
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,Highlights from Git 2.38",
        // the next articles have a title with an ecoded character
        "https://github.blog/2022-08-29-gits-database-internals-i-packed-object-store/,Git’s database internals I: packed object store",
        "https://github.blog/2022-09-02-gits-database-internals-v-scalability/,Git’s database internals V: scalability"
        })
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(data);
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
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,Another new release of Git is here! Take a look at some of our highlights on what's new in Git 2.38."
        })
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedSubtitle, parser.getSubtitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getSubtitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,2022-10-03"
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedDate, parser.getPublicationDate().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getPublicationDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://github.blog/2022-10-03-highlights-from-git-2-38/,Taylor,,Blau ",
        // the next article has an authot name with a particle
        "https://github.blog/2022-08-15-the-next-step-for-lgtm-com-github-code-scanning/,Bas,,van Schaik"
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
                               final GithubBlogLinkContentParser parser = new GithubBlogLinkContentParser(data);
                               try {
                                   Assertions.assertEquals(expectedAuthor, parser.getAuthor());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getAuthor threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
