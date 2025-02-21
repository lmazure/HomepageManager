package fr.mazure.homepagemanager.data.linkchecker.huggingface.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.huggingface.HuggingFaceLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of HuggingFaceLinkContentParser
 */
class HuggingFaceLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @Test
    void testTitle() {
        final String url = "https://huggingface.co/blog/llama2";
        final String expectedTitle = "Llama 2 is here - get it on Hugging Face";
        checkTitle(HuggingFaceLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testNoSubtitle() {
        final String url = "https://huggingface.co/blog/llama2";
        checkNoSubtitle(HuggingFaceLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://huggingface.co/blog/llama2|2023-07-18"
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(HuggingFaceLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://huggingface.co/blog/llama2|Philipp||Schmid|Omar||Sanseviero|Pedro||Cuenca|Lewis||Tunstall"
        }, delimiter = '|')
    void test4Authors(final String url,
                     final String expectedFirstName1,
                     final String expectedMiddleName1,
                     final String expectedLastName1,
                     final String expectedFirstName2,
                     final String expectedMiddleName2,
                     final String expectedLastName2,
                     final String expectedFirstName3,
                     final String expectedMiddleName3,
                     final String expectedLastName3,
                     final String expectedFirstName4,
                     final String expectedMiddleName4,
                     final String expectedLastName4) {
        check4Authors(HuggingFaceLinkContentParser.class,
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
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      null,
                      null,
                      // author 4
                      null,
                      expectedFirstName4,
                      expectedMiddleName4,
                      expectedLastName4,
                      null,
                      null);
    }
}
