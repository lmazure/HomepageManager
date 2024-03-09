package fr.mazure.homepagemanager.data.linkchecker.spectrum.test;

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
import fr.mazure.homepagemanager.data.linkchecker.spectrum.SpectrumLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of ArsTechnicaLinkContentParser
 */
public class SpectrumLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://spectrum.ieee.org/intel-20a|In 2024, Intel Hopes to Leapfrog Its Chipmaking Competitors "
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SpectrumLinkContentParser parser = new SpectrumLinkContentParser(url, data);
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
        "https://spectrum.ieee.org/intel-20a|The chipmaker is betting on new transistors and power-delivery tech"
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SpectrumLinkContentParser parser = new SpectrumLinkContentParser(url, data);
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
        "https://spectrum.ieee.org/intel-20a|2023-12-18"
    }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SpectrumLinkContentParser parser = new SpectrumLinkContentParser(url, data);
                               try {
                                   Assertions.assertTrue(parser.getDate().isPresent());
                                   Assertions.assertEquals(expectedDate, parser.getDate().get().toString());
                                } catch (final ContentParserException e) {
                                    Assertions.fail("getDate threw " + e.getMessage());
                                }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @ParameterizedTest
    @CsvSource(value = {
        "https://spectrum.ieee.org/intel-20a|Gwendolyn||Rak|",
        "https://spectrum.ieee.org/self-driving-cars-2662494269|Mary|L.|Cummings|Missy",
    }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        final AuthorData expectedAuthor = new AuthorData(Optional.empty(),
                                                         Optional.of(expectedFirstName),
                                                         Optional.ofNullable(expectedMiddleName),
                                                         Optional.of(expectedLastName),
                                                         Optional.empty(),
                                                         Optional.ofNullable(expectedGivenName));
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SpectrumLinkContentParser parser = new SpectrumLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(1, parser.getSureAuthors().size());
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
    @CsvSource(value = {
            "https://spectrum.ieee.org/winner-multicore-made-simple|Brian||Santo|Sally||Adee"
    }, delimiter = '|')
    void test2Authors(final String url,
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
                               final SpectrumLinkContentParser parser = new SpectrumLinkContentParser(url, data);
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
