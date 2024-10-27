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
     * @param data
     */
    public static void assertData(final FullFetchedLinkData data) {
        Assertions.assertEquals(200, HttpHelper.getResponseCodeFromHeaders(data.headers().get()));
        Assertions.assertTrue(data.headers().isPresent());
        Assertions.assertTrue(data.headers().get().containsKey("Content-Type"));
        Assertions.assertEquals(1, data.headers().get().get("Content-Type").size());
        Assertions.assertEquals("text/html; charset=UTF-8", data.headers().get().get("Content-Type").get(0));
        Assertions.assertTrue(data.headers().get().containsKey("Cache-Control"));
        Assertions.assertEquals(1, data.headers().get().get("Cache-Control").size());
        Assertions.assertEquals("max-age=604800", data.headers().get().get("Cache-Control").get(0));
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
     * @param expectedDateAsString
     * @param date
     */
    public static void assertDate(final String expectedDateAsString,
                                  final Optional<TemporalAccessor> date) {
        Assertions.assertTrue(date.isPresent());
        Assertions.assertTrue(date.get().isSupported(ChronoField.YEAR));
        Assertions.assertTrue(date.get().isSupported(ChronoField.MONTH_OF_YEAR));
        Assertions.assertTrue(date.get().isSupported(ChronoField.DAY_OF_MONTH));
        final LocalDate d = LocalDate.of(date.get().get(ChronoField.YEAR),
                                         date.get().get(ChronoField.MONTH_OF_YEAR),
                                         date.get().get(ChronoField.DAY_OF_MONTH));
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
