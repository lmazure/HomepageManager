package fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.stackoverflowblog.StackOverflowBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of StackOverflowBlogLinkContentParser class
 *
 */
class StackOverflowBlogLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://stackoverflow.blog/2023/01/24/ai-applications-open-new-security-vulnerabilities/,AI applications open new security vulnerabilities",
        })
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(StackOverflowBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://stackoverflow.blog/2023/01/24/ai-applications-open-new-security-vulnerabilities/,Your ML model and AI-as-a-service apps might open new attack surfaces. Here's how to mitigate them.",
        })
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(StackOverflowBlogLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://stackoverflow.blog/2023/01/24/ai-applications-open-new-security-vulnerabilities/,2023-01-24",
        })
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(StackOverflowBlogLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "https://stackoverflow.blog/2023/01/24/ai-applications-open-new-security-vulnerabilities/,Taimur,,Ijlal,",
        "https://stackoverflow.blog/2021/09/21/podcast-377-you-dont-need-a-math-phd-to-play-dwarf-fortress-just-to-code-it/,Ryan,,Donovan,",
        "https://stackoverflow.blog/2024/04/04/how-do-mixture-of-experts-layers-affect-transformer-models/,Cameron,R.,Wolfe,PhD",
        })
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName,
                    final String expectedNameSuffix) {
        check1Author(StackOverflowBlogLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     expectedNameSuffix,
                     null);
    }
}
