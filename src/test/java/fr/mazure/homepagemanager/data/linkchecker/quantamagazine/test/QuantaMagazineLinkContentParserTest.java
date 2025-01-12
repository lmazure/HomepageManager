package fr.mazure.homepagemanager.data.linkchecker.quantamagazine.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.quantamagazine.QuantaMagazineLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of QuantaMagazineLinkContentParser
 */
class QuantaMagazineLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/mathematician-solves-computer-science-conjecture-in-two-pages-20190725/|Decades-Old Computer Science Conjecture Solved in Two Pages",
        "https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/|Universal Method to Sort Complex Information Found",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(QuantaMagazineLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.quantamagazine.org/tiny-language-models-thrive-with-gpt-4-as-a-teacher-20231005/|Tiny Language Models Come of Age"
        }, delimiter = '|')
    void testTitleThatHasChanged(final String url,
                                 final String expectedTitle) {
        checkTitle(QuantaMagazineLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.quantamagazine.org/how-do-machines-grok-data-20240412/|How Do Machines ‘Grok’ Data?"
        }, delimiter = '|')
    void testTitleWithEncodedCharacters(final String url,
                                        final String expectedTitle) {
        checkTitle(QuantaMagazineLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/|The nearest neighbor problem asks where a new point fits into an existing data set. A few researchers set out to prove that there was no universal way to solve it. Instead, they found such a way."
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(QuantaMagazineLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.quantamagazine.org/mathematician-solves-computer-science-conjecture-in-two-pages-20190725/|The “sensitivity” conjecture stumped many top computer scientists, yet the new proof is so simple that one researcher summed it up in a single tweet."
        }, delimiter = '|')
    void testSubtitleContainingQuotes(final String url,
                                      final String expectedSubtitle) {
        checkSubtitle(QuantaMagazineLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://www.quantamagazine.org/yitang-zhang-proves-landmark-theorem-in-distribution-of-prime-numbers-20130519/|A virtually unknown researcher has made a great advance in one of mathematics’ oldest problems, the twin primes conjecture."
        }, delimiter = '|')
    void testSubtitleContainingHtml(final String url,
                                    final String expectedSubtitle) {
        checkSubtitle(QuantaMagazineLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/a-proof-about-where-symmetries-cant-exist-20181023/|In a major mathematical achievement, a small team of researchers has proven Zimmer’s conjecture.",
        "https://www.quantamagazine.org/how-godels-incompleteness-theorems-work-20200714|His incompleteness theorems destroyed the search for a mathematical theory of everything. Nearly a century later, we’re still coming to grips with the consequences.",
        "https://www.quantamagazine.org/the-useless-perspective-that-transformed-mathematics-20200609/|Representation theory was initially dismissed. Today, it’s central to much of mathematics.",
        }, delimiter = '|')
    void testSubtitleFinishingWithSpace(final String url,
                                        final String expectedSubtitle) {
        checkSubtitle(QuantaMagazineLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/new-algorithm-solves-cake-cutting-problem-20161006/|2016-10-06",
        "https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/|2018-08-13"
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(QuantaMagazineLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/universal-method-to-sort-complex-information-found-20180813/|Kevin||Hartnett",
        "https://www.quantamagazine.org/long-covid-how-it-keeps-us-sick-20210701/|Tara|C.|Smith"
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName) {
        check1Author(QuantaMagazineLinkContentParser.class, url, null, expectedFirstName, expectedMiddleName, expectedLastName, null, null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/barbara-liskov-is-the-architect-of-modern-algorithms-20191120/|Susan||D’Agostino"
        }, delimiter = '|')
    void testAuthorWithEncodedCharacter(final String url,
                                        final String expectedFirstName,
                                        final String expectedMiddleName,
                                        final String expectedLastName) {
        check1Author(QuantaMagazineLinkContentParser.class, url, null, expectedFirstName, expectedMiddleName, expectedLastName, null, null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/the-multiverses-measure-problem-20141103/|Natalie||Wolchover|Peter||Byrne"
        }, delimiter = '|')
    void testTwoAuthors(final String url,
                        final String expectedFirstName1,
                        final String expectedMiddleName1,
                        final String expectedLastName1,
                        final String expectedFirstName2,
                        final String expectedMiddleName2,
                        final String expectedLastName2) {
        check2Authors(QuantaMagazineLinkContentParser.class,
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

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/how-did-altruism-evolve-20240215/|Janna||Levin|Stephanie||Preston",
        "https://www.quantamagazine.org/what-causes-giant-rogue-waves-20230614/|Steven||Strogatz|Ton||van den Bremer",
        "https://www.quantamagazine.org/what-is-quantum-field-theory-and-why-is-it-incomplete-20220810/|Steven||Strogatz|David||Tong",
        "https://www.quantamagazine.org/what-is-quantum-teleportation-20240314/|Janna||Levin|John||Preskill",
        "https://www.quantamagazine.org/what-is-the-nature-of-time-20240229/|Steven||Strogatz|Frank||Wilczek",
        "https://www.quantamagazine.org/will-better-superconductors-transform-the-world-20240509/|Janna||Levin|Siddharth|Shanker|Saxena"
        }, delimiter = '|')
    void testAuthorJoyOfWhy(final String url,
                            final String expectedHostFirstName,
                            final String expectedHostMiddleName,
                            final String expectedHostLastName,
                            final String expectedFirstName,
                            final String expectedMiddleName,
                            final String expectedLastName) {
        check2Authors(QuantaMagazineLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName,
                      expectedMiddleName,
                      expectedLastName,
                      null,
                      null,
                      // author 2
                      null,
                      expectedHostFirstName,
                      expectedHostMiddleName,
                      expectedHostLastName,
                      null,
                      null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.quantamagazine.org/why-do-we-get-old-and-can-aging-be-reversed-20220727/|Steven||Strogatz|Judith||Campisi|Dena||Dubal"
        }, delimiter = '|')
    void testTwoAuthorsJoyOfWhy(final String url,
                                final String expectedHostFirstName,
                                final String expectedHostMiddleName,
                                final String expectedHostLastName,
                                final String expectedFirstName1,
                                final String expectedMiddleName1,
                                final String expectedLastName1,
                                final String expectedFirstName2,
                                final String expectedMiddleName2,
                                final String expectedLastName2) {
        check3Authors(QuantaMagazineLinkContentParser.class,
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
                      null,
                      // author 3
                      null,
                      expectedHostFirstName,
                      expectedHostMiddleName,
                      expectedHostLastName,
                      null,
                      null);
    }
}
