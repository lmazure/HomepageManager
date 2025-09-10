package fr.mazure.homepagemanager.data.dataretriever.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SiteDataPersister;
import fr.mazure.homepagemanager.utils.FileHelper;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;

/**
 * Helper for the data retrieved tests
 */
public class TestHelper {

    /**
     * Assert that the data is the one expected from http://example.com
     *
     * @param data data
     */
    public static void assertExampleComData(final FullFetchedLinkData data) {
        Assertions.assertEquals(200, HttpHelper.getResponseCodeFromHeaders(data.headers().get()));
        Assertions.assertTrue(data.headers().isPresent());
        Assertions.assertTrue(data.headers().get().containsKey("Content-Type"));
        Assertions.assertEquals(1, data.headers().get().get("Content-Type").size());
        Assertions.assertEquals("text/html", data.headers().get().get("Content-Type").get(0));
        Assertions.assertTrue(data.headers().get().containsKey("Cache-Control"));
        Assertions.assertEquals(1, data.headers().get().get("Cache-Control").size());
        Assertions.assertTrue(data.headers().get().get("Cache-Control").get(0).matches("max-age=\\d{1,5}"));
        Assertions.assertTrue(data.dataFileSection().isPresent());
        Assertions.assertFalse(data.error().isPresent());
        try {
            final String d = Files.readString(data.dataFileSection().get().file().toPath());
            Assertions.assertNotEquals(-1, d.indexOf("This domain is for use in illustrative examples in documents."));
        } catch (final IOException e) {
            Assertions.fail("failure to read data file " + data.dataFileSection().get() + " (" + e.getMessage() +")");
        }
    }

    /**
     * Assert that the effective date is the expected one
     *
     * @param expectedDateAsString expected date
     * @param effectiveDate effective date
     */
    public static void assertDate(final String expectedDateAsString,
                                  final Optional<TemporalAccessor> effectiveDate) {
        Assertions.assertTrue(effectiveDate.isPresent());
        Assertions.assertTrue(effectiveDate.get().isSupported(ChronoField.YEAR));
        Assertions.assertTrue(effectiveDate.get().isSupported(ChronoField.MONTH_OF_YEAR));
        Assertions.assertTrue(effectiveDate.get().isSupported(ChronoField.DAY_OF_MONTH));
        final LocalDate d = LocalDate.of(effectiveDate.get().get(ChronoField.YEAR),
                                         effectiveDate.get().get(ChronoField.MONTH_OF_YEAR),
                                         effectiveDate.get().get(ChronoField.DAY_OF_MONTH));
        Assertions.assertEquals(expectedDateAsString, d.toString());
    }

    /**
     * @param clazz test class
     *
     * @return SynchronousSiteDataRetriever to be used for testing the class
     */
    public static CachedSiteDataRetriever buildDataSiteRetriever(final Class<?> clazz) {
        return new CachedSiteDataRetriever(buildSiteDataPersister(clazz));
    }

    /**
     * @param clazz test class
     *
     * @return SiteDataPersister to be used for testing the class
     */
    public static SiteDataPersister buildSiteDataPersister(final Class<?> clazz) {
        final Path cachePath = getTestDatapath(clazz);
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SiteDataPersister(cachePath);
    }

    /**
     * Return a directory to store the data of the unit tests defined in a test class
     * @param clazz Test class
     * @return Directory
     */
    public static Path getTestDatapath(final Class<?> clazz) {
        return Paths.get("G:\\Documents\\tmp\\hptmp\\test\\" + clazz.getSimpleName());  // TODO this should not be hard-coded
    }
}
