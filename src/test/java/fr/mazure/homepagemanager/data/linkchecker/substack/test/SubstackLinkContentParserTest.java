package fr.mazure.homepagemanager.data.linkchecker.substack.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.substack.SubstackLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 *  Tests of SubstackLinkContentParser class
 */
class SubstackLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
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
        checkTitle(SubstackLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|Authors: PromptArmor and Kai Greshake",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|Ce phénomène étonnant, découvert récemment, pourrait changer notre compréhension de l'apprentissage et de la cognition dans les réseaux de neurones...",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|Executive Summary of Bi-temporality",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(SubstackLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://magazine.sebastianraschka.com/p/llm-training-rlhf-and-its-alternatives",
        "https://magazine.sebastianraschka.com/p/understanding-and-coding-self-attention",
        "https://magazine.sebastianraschka.com/p/understanding-encoder-and-decoder",
        }, delimiter = '|')
    void testNoSubtitle(final String url) {
        checkNoSubtitle(SubstackLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
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
        check1Author(SubstackLinkContentParser.class,
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
            "https://frontierai.substack.com/p/you-cant-build-a-moat-with-ai|Vikram||Sreekanti|Joseph|E.|Gonzalez",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(SubstackLinkContentParser.class,
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
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        final String url = "https://promptarmor.substack.com/p/data-exfiltration-from-writercom";
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final SubstackLinkContentParser parser = new SubstackLinkContentParser(url, data, retriever);
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

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://magazine.sebastianraschka.com/p/research-papers-in-january-2024|2024-02-03",
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|2023-12-15",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|2023-09-11",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|2023-08-04",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(SubstackLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://magazine.sebastianraschka.com/p/research-papers-in-january-2024|en",
        "https://promptarmor.substack.com/p/data-exfiltration-from-writercom|en",
        "https://scienceetonnante.substack.com/p/grokking-les-modeles-dia-sont-ils|fr",
        "https://tidyfirst.substack.com/p/eventual-business-consistency|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(SubstackLinkContentParser.class, url, expectedLanguage);
    }
}

