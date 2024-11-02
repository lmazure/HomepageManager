package fr.mazure.homepagemanager.data.linkchecker.spectrum.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.spectrum.SpectrumLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of SpectrumLinkContentParser
 */
class SpectrumLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://spectrum.ieee.org/intel-20a|In 2024, Intel Hopes to Leapfrog Its Chipmaking Competitors "
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(SpectrumLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://spectrum.ieee.org/intel-20a|The chipmaker is betting on new transistors and power-delivery tech"
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(SpectrumLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://spectrum.ieee.org/intel-20a|2023-12-18"
    }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
		checkDate(SpectrumLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://spectrum.ieee.org/intel-20a|Gwendolyn||Rak|",
        "https://spectrum.ieee.org/self-driving-cars-2662494269|Mary|L.|Cummings|Missy",
        "https://spectrum.ieee.org/the-end-of-gold-farming|Margo||Anderson|"
    }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName,
                    final String expectedGivenName) {
        check1Author(SpectrumLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     expectedGivenName);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
            "https://spectrum.ieee.org/winner-multicore-made-simple|Brian||Santo|Sally||Adee",
            "https://spectrum.ieee.org/everything-you-need-to-know-about-5g|Amy||Nordrum|Kristen||Clark",
    }, delimiter = '|')
    void test2Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2) {
        check2Authors(SpectrumLinkContentParser.class,
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
