package data.internet.test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import data.internet.SiteData;
import data.internet.SiteData.Status;
import data.internet.SiteDataPersister;
import utils.FileHelper;

public class SiteDataPersisterTest {

    private static URL url = TestHelper.buildURL("http://example.com");
    private static Instant now = Instant.now();
    private static Status status = Status.SUCCESS;
    private static Optional<Integer> httpCode = Optional.of(Integer.valueOf(200));
    private static Optional<Map<String, List<String>>> headers = Optional.of(Map.of("header1", List.of("val11"),
                                                                                    "header2", List.of("val21", "val22"),
                                                                                    "header3", List.of(),
                                                                                    "header4", List.of("val31", "val32", "val33"),
                                                                                    "header5", List.of("val51")));
    private static Optional<String> error = Optional.of("error");

    @SuppressWarnings("static-method")
    @Test
    void allPresent() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, now, status, httpCode, headers, Optional.empty(), error);
        final SiteData data = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, data.getHttpCode());
        Assertions.assertEquals(status, data.getStatus());
        Assertions.assertEquals(headers, data.getHeaders());
        Assertions.assertEquals(error, data.getError());
    }

    @SuppressWarnings("static-method")
    @Test
    void allHttpCodeEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, now, status, Optional.empty(), headers, Optional.empty(), error);
        final SiteData data = persister.retrieve(url, now);

        Assertions.assertEquals(Optional.empty(), data.getHttpCode());
        Assertions.assertEquals(status, data.getStatus());
        Assertions.assertEquals(headers, data.getHeaders());
        Assertions.assertEquals(error, data.getError());
    }

    @SuppressWarnings("static-method")
    @Test
    void allHeadersEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, now, status, httpCode, Optional.empty(), Optional.empty(), error);
        final SiteData data = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, data.getHttpCode());
        Assertions.assertEquals(status, data.getStatus());
        Assertions.assertEquals(Optional.empty(), data.getHeaders());
        Assertions.assertEquals(error, data.getError());
    }

    @SuppressWarnings("static-method")
    @Test
    void allErrorEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, now, status, httpCode, headers, Optional.empty(), Optional.empty());
        final SiteData data = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, data.getHttpCode());
        Assertions.assertEquals(status, data.getStatus());
        Assertions.assertEquals(headers, data.getHeaders());
        Assertions.assertEquals(Optional.empty(), data.getError());
    }

    @SuppressWarnings("static-method")
    @Test
    void allEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, now, status, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        final SiteData data = persister.retrieve(url, now);

        Assertions.assertEquals(Optional.empty(), data.getHttpCode());
        Assertions.assertEquals(status, data.getStatus());
        Assertions.assertEquals(Optional.empty(), data.getHeaders());
        Assertions.assertEquals(Optional.empty(), data.getError());
    }

    @SuppressWarnings("static-method")
    @Test
    void allPresentAndStatusFailure() {

        final SiteDataPersister persister = buildSiteDataPersister();
        persister.persist(url, now, Status.FAILURE, httpCode, headers, Optional.empty(), error);
        final SiteData data = persister.retrieve(url, now);

        Assertions.assertEquals(httpCode, data.getHttpCode());
        Assertions.assertEquals(Status.FAILURE, data.getStatus());
        Assertions.assertEquals(headers, data.getHeaders());
        Assertions.assertEquals(error, data.getError());
    }

    private static SiteDataPersister buildSiteDataPersister() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\test\\SiteDataPersisterTest");
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SiteDataPersister(cachePath);
    }
}
