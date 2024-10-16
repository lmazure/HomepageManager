package fr.mazure.homepagemanager.data.linkchecker.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of LinkContentParserUtils class
 */
class LinkContentParserUtilsTest {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "Tharakarama Reddy Yernapalli Sreenivasulu||Tharakarama|Reddy|Yernapalli Sreenivasulu||",
        "Cameron R. Wolfe, PhD||Cameron|R.|Wolfe|PhD|",
        }, delimiter = '|')
    void testAuthorNameParsing(final String str,
                               final String expectedNamePrefix,
                               final String expectedFirstName,
                               final String expectedMiddleName,
                               final String expectedLastName,
                               final String expectedNameSuffix,
                               final String expectedGivenName) throws ContentParserException {
        final AuthorData expectedAuthor = new AuthorData(Optional.ofNullable(expectedNamePrefix),
                                                         Optional.ofNullable(expectedFirstName),
                                                         Optional.ofNullable(expectedMiddleName),
                                                         Optional.ofNullable(expectedLastName),
                                                         Optional.ofNullable(expectedNameSuffix),
                                                         Optional.ofNullable(expectedGivenName));

        final AuthorData author = LinkContentParserUtils.parseAuthorName(str);
        Assertions.assertEquals(expectedAuthor, author);
    }
}
