package data.internet.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import utils.FileHelper;

public class TestHelper {

    public static void assertData(final SiteData data) {
        Assertions.assertTrue(data.getHttpCode().isPresent());
        Assertions.assertEquals(200, data.getHttpCode().get());
        Assertions.assertTrue(data.getHeaders().isPresent());
        Assertions.assertTrue(data.getHeaders().get().keySet().contains("Content-Type"));
        Assertions.assertEquals(1, data.getHeaders().get().get("Content-Type").size());
        Assertions.assertEquals("text/html; charset=UTF-8", data.getHeaders().get().get("Content-Type").get(0));
        Assertions.assertTrue(data.getHeaders().get().keySet().contains("Cache-Control"));
        Assertions.assertEquals(1, data.getHeaders().get().get("Cache-Control").size());
        Assertions.assertEquals("max-age=604800", data.getHeaders().get().get("Cache-Control").get(0));
        Assertions.assertTrue(data.getDataFile().isPresent());
        Assertions.assertFalse(data.getError().isPresent());
        try {
            final String d = Files.readString(data.getDataFile().get().file().toPath());
            Assertions.assertNotEquals(-1, d.indexOf("This domain is for use in illustrative examples in documents."));
        } catch (final IOException e) {
            Assertions.fail("failure to read data file " + data.getDataFile().get() + " (" + e.getMessage() +")");
        }
    }

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

    public static SynchronousSiteDataRetriever buildDataSiteRetriever(final Class<?> clazz) {
        final Path cachePath = getTestDatapath(clazz);
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SynchronousSiteDataRetriever(new SiteDataPersister(cachePath));
    }

    public static Path getTestDatapath(final Class<?> clazz) {
        return Paths.get("H:\\Documents\\tmp\\hptmp\\test\\" + clazz.getSimpleName());
    }
}
