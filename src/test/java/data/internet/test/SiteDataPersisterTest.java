package data.internet.test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import utils.FileHelper;

/**
 * Tests of SiteDataPersister
 *
 */
public class SiteDataPersisterTest {

    private static String url = "http://example.com";
    private static Instant now = Instant.now();
    private static Optional<Integer> httpCode = Optional.of(Integer.valueOf(200));
    private static Optional<Map<String, List<String>>> headers = Optional.of(Map.of("header1", List.of("val11"),
                                                                                    "header2", List.of("val21", "val22"),
                                                                                    "header3", List.of(),
                                                                                    "header4", List.of("val31", "val32", "val33"),
                                                                                    "header5", List.of("val51")));
    private static Optional<String> error = Optional.of("error");

    @Test
    void allPresent() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, httpCode, headers, Optional.empty(), error, now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());
    }

    @Test
    void allHttpCodeEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, Optional.empty(), headers, Optional.empty(), error, now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(Optional.empty(), effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());
    }

    @Test
    void allHeadersEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, httpCode, Optional.empty(), Optional.empty(), error, now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(Optional.empty(), effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());
    }

    @Test
    void allErrorEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, httpCode, headers, Optional.empty(), Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(Optional.empty(), effectiveData.error());
    }

    @Test
    void allEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(Optional.empty(), effectiveData.httpCode());
        Assertions.assertEquals(Optional.empty(), effectiveData.headers());
        Assertions.assertTrue(effectiveData.error().isEmpty());
    }

    private SiteDataPersister buildSiteDataPersister() {
        final Path cachePath = TestHelper.getTestDatapath(getClass());
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SiteDataPersister(cachePath);
    }
}
