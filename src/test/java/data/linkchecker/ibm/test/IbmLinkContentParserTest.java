package data.linkchecker.ibm.test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.ibm.IbmLinkContentParser;
import utils.HtmlHelper;
import utils.StringHelper;
import utils.xmlparsing.AuthorData;

public class IbmLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/|An introduction to the java.util.stream library",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final IbmLinkContentParser parser = new IbmLinkContentParser(data, StringHelper.convertStringToUrl(url));
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
    @CsvSource(value = {
            "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/|Run functional-style queries on collections and other data sets",
            // the next article has a space at the end of the subtitle
            "https://developer.ibm.com/articles/j-java-streams-3-brian-goetz/|Understand java.util.stream internals"
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final IbmLinkContentParser parser = new IbmLinkContentParser(data, StringHelper.convertStringToUrl(url));
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
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/,2016-05-09",
        })
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                          (final Boolean b, final SiteData d) -> {
                              Assertions.assertTrue(d.getDataFile().isPresent());
                              final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                              final IbmLinkContentParser parser = new IbmLinkContentParser(data, StringHelper.convertStringToUrl(url));
                              try {
                                  Assertions.assertEquals(expectedDate, parser.getDate().toString());
                              } catch (final ContentParserException e) {
                                  Assertions.fail("getDate threw " + e.getMessage());
                              }
                              consumerHasBeenCalled.set(true);
                          });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource({
        "https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/,Brian,Goetz",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.of(expectedFirstName),
                                                         Optional.empty(),
                                                         Optional.of(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final String data = HtmlHelper.slurpFile(d.getDataFile().get());
                               final IbmLinkContentParser parser = new IbmLinkContentParser(data, StringHelper.convertStringToUrl(url));
                               try {
                                 Assertions.assertEquals(1, parser.getAuthors().size());
                                 Assertions.assertEquals(expectedAuthor, parser.getAuthors().get(0));
                             } catch (final ContentParserException e) {
                                 Assertions.fail("getAuthors threw " + e.getMessage());
                             }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
