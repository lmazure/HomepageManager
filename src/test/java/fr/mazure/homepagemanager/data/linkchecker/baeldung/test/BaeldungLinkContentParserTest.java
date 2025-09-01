package fr.mazure.homepagemanager.data.linkchecker.baeldung.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.baeldung.BaeldungLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of BaeldungLinkContentParser
 */
class BaeldungLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @Test
    void testTitle() {
        final String url = "https://www.baeldung.com/crawler4j";
        final String expectedTitle = "A Guide to Crawler4j";
        checkTitle(BaeldungLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @Test
    void testNoSubtitle() {
        final String url = "https://www.baeldung.com/crawler4j";
        checkNoSubtitle(BaeldungLinkContentParser.class, url);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.baeldung.com/crawler4j|2019-06-07",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(BaeldungLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.baeldung.com/crawler4j|Amy|DeGregorio",
        "https://www.baeldung.com/ops/git-configure-credentials|Michael|Pratt"
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedLastName) {
        check1Author(BaeldungLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     null,
                     expectedLastName,
                     null,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.baeldung.com/java-9-reactive-streams"
        }, delimiter = '|')
    void testNoAuthor(final String url) {
        check0Author(BaeldungLinkContentParser.class, url);
    }
}
