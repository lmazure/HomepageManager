package fr.mazure.homepagemanager.data.linkchecker.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.utils.DateTimeHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
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
                        Assertions.assertEquals(expectedTitle, p.getTitle());
                    });
    }

    protected static void checkSubtitle(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedSubtitle) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertEquals(expectedSubtitle, p.getSubtitle().get());
                    });
    }

    protected static void checkNoSubtitle(final Class<? extends LinkDataExtractor> clazz,
                                          final String url) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertFalse(p.getSubtitle().isPresent());
                    });
    }

    protected static void checkLanguage(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedLanguage) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                         Assertions.assertEquals(Locale.of(expectedLanguage), p.getLanguage());
                    });
    }

    protected static void checkCreationDate(final Class<? extends LinkDataExtractor> clazz,
                                            final String url,
                                            final String expectedDate) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertTrue(p.getCreationDate().isPresent());
                        Assertions.assertEquals(expectedDate, p.getCreationDate().get().toString());
                    });
    }

    protected static void checkPublicationDate(final Class<? extends LinkDataExtractor> clazz,
                                               final String url,
                                               final String expectedDate) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertTrue(p.getPublicationDate().isPresent());
                        Assertions.assertEquals(expectedDate, p.getPublicationDate().get().toString());
                    });
    }

    protected static void checkDuration(final Class<? extends LinkDataExtractor> clazz,
                                        final String url,
                                        final String expectedDuration) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertTrue(p.getCreationDate().isPresent());
                        Assertions.assertEquals(DateTimeHelper.roundDuration(Duration.parse(expectedDuration)), DateTimeHelper.roundDuration(p.getDuration().get()));
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

    protected static void check4Authors(final Class<? extends LinkDataExtractor> clazz,
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
                                        final String expectedGivenName3,
                                        final String expectedNamePrefix4,
                                        final String expectedFirstName4,
                                        final String expectedMiddleName4,
                                        final String expectedLastName4,
                                        final String expectedNameSuffix4,
                                        final String expectedGivenName4) {

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
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix4),
                                           Optional.ofNullable(expectedFirstName4),
                                           Optional.ofNullable(expectedMiddleName4),
                                           Optional.ofNullable(expectedLastName4),
                                           Optional.ofNullable(expectedNameSuffix4),
                                           Optional.ofNullable(expectedGivenName4)));
        checkAuthors(clazz, url, expectedAuthors);
    }

    protected static void check5Authors(final Class<? extends LinkDataExtractor> clazz,
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
                                        final String expectedGivenName3,
                                        final String expectedNamePrefix4,
                                        final String expectedFirstName4,
                                        final String expectedMiddleName4,
                                        final String expectedLastName4,
                                        final String expectedNameSuffix4,
                                        final String expectedGivenName4,
                                        final String expectedNamePrefix5,
                                        final String expectedFirstName5,
                                        final String expectedMiddleName5,
                                        final String expectedLastName5,
                                        final String expectedNameSuffix5,
                                        final String expectedGivenName5) {

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
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix4),
                                           Optional.ofNullable(expectedFirstName4),
                                           Optional.ofNullable(expectedMiddleName4),
                                           Optional.ofNullable(expectedLastName4),
                                           Optional.ofNullable(expectedNameSuffix4),
                                           Optional.ofNullable(expectedGivenName4)));
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix5),
                                           Optional.ofNullable(expectedFirstName5),
                                           Optional.ofNullable(expectedMiddleName5),
                                           Optional.ofNullable(expectedLastName5),
                                           Optional.ofNullable(expectedNameSuffix5),
                                           Optional.ofNullable(expectedGivenName5)));
        checkAuthors(clazz, url, expectedAuthors);
    }


    protected static void check6Authors(final Class<? extends LinkDataExtractor> clazz,
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
                                        final String expectedGivenName3,
                                        final String expectedNamePrefix4,
                                        final String expectedFirstName4,
                                        final String expectedMiddleName4,
                                        final String expectedLastName4,
                                        final String expectedNameSuffix4,
                                        final String expectedGivenName4,
                                        final String expectedNamePrefix5,
                                        final String expectedFirstName5,
                                        final String expectedMiddleName5,
                                        final String expectedLastName5,
                                        final String expectedNameSuffix5,
                                        final String expectedGivenName5,
                                        final String expectedNamePrefix6,
                                        final String expectedFirstName6,
                                        final String expectedMiddleName6,
                                        final String expectedLastName6,
                                        final String expectedNameSuffix6,
                                        final String expectedGivenName6) {

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
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix4),
                                           Optional.ofNullable(expectedFirstName4),
                                           Optional.ofNullable(expectedMiddleName4),
                                           Optional.ofNullable(expectedLastName4),
                                           Optional.ofNullable(expectedNameSuffix4),
                                           Optional.ofNullable(expectedGivenName4)));
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix5),
                                           Optional.ofNullable(expectedFirstName5),
                                           Optional.ofNullable(expectedMiddleName5),
                                           Optional.ofNullable(expectedLastName5),
                                           Optional.ofNullable(expectedNameSuffix5),
                                           Optional.ofNullable(expectedGivenName5)));
        expectedAuthors.add(new AuthorData(Optional.ofNullable(expectedNamePrefix6),
                                           Optional.ofNullable(expectedFirstName6),
                                           Optional.ofNullable(expectedMiddleName6),
                                           Optional.ofNullable(expectedLastName6),
                                           Optional.ofNullable(expectedNameSuffix6),
                                           Optional.ofNullable(expectedGivenName6)));
        checkAuthors(clazz, url, expectedAuthors);
    }
    private static void checkAuthors(final Class<? extends LinkDataExtractor> clazz,
                                     final String url,
                                     final List<AuthorData> expectedAuthors) {
        perform(clazz,
                url,
                (final LinkDataExtractor p) ->
                    {
                        Assertions.assertEquals(expectedAuthors, p.getSureAuthors());
                    });
    }

    protected static void perform(final Class<? extends LinkDataExtractor> clazz,
                                  final String url,
                                  final Consumer<LinkDataExtractor> assertor) {
        HttpHelper.throttle(url);
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
                           true);
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
        } catch (final InvocationTargetException e) {
            Assertions.fail("Error while invoking the constructor " + e.getCause());
            return null;
        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException e) {
            Assertions.fail("Error in reflexion code " + e);
            return null;
        }
    }
}
