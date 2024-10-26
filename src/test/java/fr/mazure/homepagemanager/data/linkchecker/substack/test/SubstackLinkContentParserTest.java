package fr.mazure.homepagemanager.data.linkchecker.substack.test;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.substack.SubstackLinkContentParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 *  Tests of SubstackLinkContentParser class
 */
class SubstackLinkContentParserTest {

    @ParameterizedTest
    @CsvSource(value = {
        "https://magazine.sebastianraschka.com/p/research-papers-in-january-2024|Model Merging, Mixtures of Experts, and Towards Smaller LLMs",
        "https://magazine.sebastianraschka.com/p/ahead-of-ai-12-llm-businesses|LLM Business and Busyness: Recent Company Investments and AI Adoption, New Small Openly Available LLMs, and LoRA Research",
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|Data exfiltration from Writer.com with indirect prompt injection",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|\"Grokking\" : les modèles d'IA sont-ils capables de piger ?",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|Eventual Business Consistency",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
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
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|Authors: PromptArmor and Kai Greshake",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|Ce phénomène étonnant, découvert récemment, pourrait changer notre compréhension de l'apprentissage et de la cognition dans les réseaux de neurones...",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|Executive Summary of Bi-temporality",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
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
        "https://magazine.sebastianraschka.com/p/llm-training-rlhf-and-its-alternatives",
        "https://magazine.sebastianraschka.com/p/understanding-and-coding-self-attention",
        "https://magazine.sebastianraschka.com/p/understanding-encoder-and-decoder",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
                               try {
                                   Assertions.assertFalse(parser.getSubtitle().isPresent());
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
        "https://magazine.sebastianraschka.com/p/research-papers-in-january-2024|Sebastian||Raschka",
        "https://magazine.sebastianraschka.com/p/understanding-and-coding-self-attention|Sebastian||Raschka",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|David||Louapre",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|Kent||Beck",
        }, delimiter = '|')
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
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
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
    @CsvSource(value = {
            "https://frontierai.substack.com/p/you-cant-build-a-moat-with-ai|Vikram||Sreekanti|Joseph|E.|Gonzalez",
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
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
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

    /* this is impossible to make this work, since the Greshake does not appear in the author names
    @Test
    void testComplexAuthor() {
        final AuthorData expectedAuthor1 = new AuthorData(Optional.empty(),
                                                          Optional.empty(),
                                                          Optional.empty(),
                                                          Optional.empty(),
                                                          Optional.empty(),
                                                          Optional.of("PromptArmor"));
        final AuthorData expectedAuthor2 = new AuthorData(Optional.empty(),
                                                          Optional.of("Kai"),
                                                          Optional.empty(),
                                                          Optional.of("Greshake"),
                                                          Optional.empty(),
                                                          Optional.empty());
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://promptarmor.substack.com/p/data-exfiltration-from-writercom";
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
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
    */

    @ParameterizedTest
    @CsvSource(value = {
        "https://magazine.sebastianraschka.com/p/research-papers-in-january-2024|2024-02-03",
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|2023-12-15",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|2023-09-11",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|2023-08-04",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
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
        "https://magazine.sebastianraschka.com/p/research-papers-in-january-2024|en",
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|en",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|fr",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data);
                               try {
                                   Assertions.assertEquals(Locale.of(expectedLanguage), parser.getLanguage());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getLanguage threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}

