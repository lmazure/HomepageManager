package fr.mazure.homepagemanager.data.linkchecker.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Base class for the LinkDataExtractor tests
 */
public class LinkDataExtractorTestBase {

    protected static void checkTitle(final Class<? extends LinkDataExtractor> clazz,
                                     final String url,
                                     final String expectedTitle) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        try {
                            Assertions.assertEquals(expectedTitle, p.getTitle());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getTitle threw " + e.getMessage());
                        }
                    });
    }

    protected static void checkSubtitle(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedSubtitle) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        try {
                            Assertions.assertEquals(expectedSubtitle, p.getSubtitle().get());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getSubtitle threw " + e.getMessage());
                        }
                    });
    }

    protected static void checkNoSubtitle(final Class<? extends LinkDataExtractor> clazz,
                                          final String url) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        try {
                            Assertions.assertFalse(p.getSubtitle().isPresent());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getSubtitle threw " + e.getMessage());
                        }
                    });
    }

    protected static void checkDate(final Class<? extends LinkDataExtractor> clazz,
                                    final String url,
                                    final String expectedDate) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        try {
                            Assertions.assertTrue(p.getDate().isPresent());
                            Assertions.assertEquals(expectedDate, p.getDate().get().toString());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getDate threw " + e.getMessage());
                        }
                    });
    }

    protected static void checkDuration(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedDuration) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        try {
                            Assertions.assertTrue(p.getDate().isPresent());
                            Assertions.assertEquals(DateTimeHelper.roundDuration(Duration.parse(expectedDuration)), DateTimeHelper.roundDuration(p.getDuration().get()));
                        } catch (final ContentParserException e) {
                            Assertions.fail("getDuration threw " + e.getMessage());
                        }
                    });
    }

    protected static void check0Author(final Class<? extends LinkDataExtractor> clazz,
                                       final String url) {
        final List<AuthorData> expectedAuthors = new ArrayList<>();
        checkAuthors(clazz, url, expectedAuthors);
    }

    protected static void check1Author(final Class<? extends LinkDataExtractor> clazz,
                                       final String url,
                                       final String expectedNamePrefix,
                                       final String expectedFirstName,
                                       final String expectedMiddleName,
                                       final String expectedLastName,
                                       final String expectedNameSuffix,
                                       final String expectedGivenName) {
        final List<AuthorData> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix),
                                           Optional.ofNullable(expectedFirstName),
                                           Optional.ofNullable(expectedMiddleName),
                                           Optional.ofNullable(expectedLastName),
                                           Optional.ofNullable(expectedNameSuffix),
                                           Optional.ofNullable(expectedGivenName)));
        checkAuthors(clazz, url, expectedAuthors);
   }

    protected static void check2Authors(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedNamePrefix1,
                                        final String expectedFirstName1,
                                        final String expectedMiddleName1,
                                        final String expectedLastName1,
                                        final String expectedNameSuffix1,
                                        final String expectedGivenName1,
                                        final String expectedNamePrefix2,
                                        final String expectedFirstName2,
                                        final String expectedMiddleName2,
                                        final String expectedLastName2,
                                        final String expectedNameSuffix2,
                                        final String expectedGivenName2) {
        final List<AuthorData> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix1),
                                           Optional.ofNullable(expectedFirstName1),
                                           Optional.ofNullable(expectedMiddleName1),
                                           Optional.ofNullable(expectedLastName1),
                                           Optional.ofNullable(expectedNameSuffix1),
                                           Optional.ofNullable(expectedGivenName1)));
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix2),
                                           Optional.ofNullable(expectedFirstName2),
                                           Optional.ofNullable(expectedMiddleName2),
                                           Optional.ofNullable(expectedLastName2),
                                           Optional.ofNullable(expectedNameSuffix2),
                                           Optional.ofNullable(expectedGivenName2)));
        checkAuthors(clazz, url, expectedAuthors);
    }

    protected static void check3Authors(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedNamePrefix1,
                                        final String expectedFirstName1,
                                        final String expectedMiddleName1,
                                        final String expectedLastName1,
                                        final String expectedNameSuffix1,
                                        final String expectedGivenName1,
                                        final String expectedNamePrefix2,
                                        final String expectedFirstName2,
                                        final String expectedMiddleName2,
                                        final String expectedLastName2,
                                        final String expectedGivenName2,
                                        final String expectedNameSuffix2,
                                        final String expectedNamePrefix3,
                                        final String expectedFirstName3,
                                        final String expectedMiddleName3,
                                        final String expectedLastName3,
                                        final String expectedNameSuffix3,
                                        final String expectedGivenName3) {
        final List<AuthorData> expectedAuthors = new ArrayList<>();
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix1),
                                           Optional.ofNullable(expectedFirstName1),
                                           Optional.ofNullable(expectedMiddleName1),
                                           Optional.ofNullable(expectedLastName1),
                                           Optional.ofNullable(expectedNameSuffix1),
                                           Optional.ofNullable(expectedGivenName1)));
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix2),
                                           Optional.ofNullable(expectedFirstName2),
                                           Optional.ofNullable(expectedMiddleName2),
                                           Optional.ofNullable(expectedLastName2),
                                           Optional.ofNullable(expectedNameSuffix2),
                                           Optional.ofNullable(expectedGivenName2)));
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix3),
                                           Optional.ofNullable(expectedFirstName3),
                                           Optional.ofNullable(expectedMiddleName3),
                                           Optional.ofNullable(expectedLastName3),
                                           Optional.ofNullable(expectedNameSuffix3),
                                           Optional.ofNullable(expectedGivenName3)));
        checkAuthors(clazz, url, expectedAuthors);
    }

    private static void checkAuthors(final Class<? extends LinkDataExtractor> clazz,
                                     final String url,
                                     final List<AuthorData> expectedAuthors) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        try {
                            Assertions.assertEquals(expectedAuthors, p.getSureAuthors());
                        } catch (final ContentParserException e) {
                            Assertions.fail("getSureAuthors threw " + e.getMessage());
                        }
                    });
    }

    protected static void perform(final Class<? extends LinkDataExtractor> clazz,
                                  final String url,
                                  final Consumer<LinkDataExtractor> assertor) {
        final CachedSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(clazz);
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               final LinkDataExtractor parser = construct(clazz, url, data, retriever);
                               assertor.accept(parser);
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private static LinkDataExtractor construct(final Class<? extends LinkDataExtractor> clazz,
                                               final String url,
                                               final String data,
                                               final CachedSiteDataRetriever retriever) {
        try {
            @SuppressWarnings("unchecked")
            final Constructor<LinkDataExtractor> constructor = (Constructor<LinkDataExtractor>)clazz.getConstructor(String.class, String.class, CachedSiteDataRetriever.class);
            return constructor.newInstance(url, data, retriever);
        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Assertions.fail("Error in reflexion code " + e.getMessage());
            return null;
        }
    }
}
