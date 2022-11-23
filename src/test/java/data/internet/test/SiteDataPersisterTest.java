package data.internet.test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.SiteData;
import data.internet.SiteDataDTO;
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
                                                                                    "header4", List.of("val41", "val42", "val43"),
                                                                                    "header5", List.of("val51")));
    private static Optional<String> error = Optional.of("error");

    private static String url2 = "http://example2.com";
    private static Optional<Integer> httpCode2 = Optional.of(Integer.valueOf(201));
    private static Optional<Map<String, List<String>>> headers2 = Optional.of(Map.of("header1_2", List.of("val11_2"),
                                                                                     "header2_2", List.of("val21_2", "val22_2"),
                                                                                     "header3_2", List.of(),
                                                                                     "header4_2", List.of("val31_2", "val32_2", "val33_2"),
                                                                                     "header5_2", List.of("val51_2")));
    private static Optional<String> error2 = Optional.of("error2");

    private static String url3 = "http://example3.com";
    private static Optional<Integer> httpCode3 = Optional.of(Integer.valueOf(202));
    private static Optional<Map<String, List<String>>> headers3 = Optional.of(Map.of("header1_3", List.of(),
                                                                                     "header2_3", List.of("val21_3", "val22_3"),
                                                                                     "header3_3", List.of("val31_3", "val32_3", "val33_3", "val34_3", "val35_3", "val36_3", "val37_3", "val38_3", "val39_3", "val3a_3", "val3b_3"),
                                                                                     "header4_3", List.of("val41_3", "val42_3", "val43_3"),
                                                                                     "header5_3", List.of()));
    private static Optional<String> error3 = Optional.of("error3");

    @Test
    void allPresent() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto = new SiteDataDTO(url, httpCode, headers, error, null);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());
        Assertions.assertNull(effectiveData.previousRedirection());
    }

    @Test
    void allHttpCodeEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto = new SiteDataDTO(url, Optional.empty(), headers, error, null);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(Optional.empty(), effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());
        Assertions.assertNull(effectiveData.previousRedirection());
    }

    @Test
    void allHeadersEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto = new SiteDataDTO(url, httpCode, Optional.empty(), error, null);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(Optional.empty(), effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());
        Assertions.assertNull(effectiveData.previousRedirection());
    }

    @Test
    void allErrorEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto = new SiteDataDTO(url, httpCode, headers, Optional.empty(), null);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(Optional.empty(), effectiveData.error());
        Assertions.assertNull(effectiveData.previousRedirection());
    }

    @Test
    void allEmpty() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto = new SiteDataDTO(url, Optional.empty(), Optional.empty(), Optional.empty(), null);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(Optional.empty(), effectiveData.httpCode());
        Assertions.assertEquals(Optional.empty(), effectiveData.headers());
        Assertions.assertTrue(effectiveData.error().isEmpty());
        Assertions.assertNull(effectiveData.previousRedirection());
    }

    @Test
    void oneRedirection() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto2 = new SiteDataDTO(url2, httpCode2, headers2, error2, null);
        final SiteDataDTO dto = new SiteDataDTO(url, httpCode, headers, error, dto2);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());

        Assertions.assertEquals(url2, effectiveData.previousRedirection().url());
        Assertions.assertEquals(httpCode2, effectiveData.previousRedirection().httpCode());
        Assertions.assertEquals(headers2, effectiveData.previousRedirection().headers());
        Assertions.assertEquals(error2, effectiveData.previousRedirection().error());
        Assertions.assertNull(effectiveData.previousRedirection().previousRedirection());
    }


    @Test
    void twoRedirections() {

        final SiteDataPersister persister = buildSiteDataPersister();
        final SiteDataDTO dto3 = new SiteDataDTO(url3, httpCode3, headers3, error3, null);
        final SiteDataDTO dto2 = new SiteDataDTO(url2, httpCode2, headers2, error2, dto3);
        final SiteDataDTO dto = new SiteDataDTO(url, httpCode, headers, error, dto2);
        persister.persist(dto, Optional.empty(), now);
        final SiteData effectiveData = persister.retrieve(url, now);

        Assertions.assertEquals(url, effectiveData.url());
        Assertions.assertEquals(httpCode, effectiveData.httpCode());
        Assertions.assertEquals(headers, effectiveData.headers());
        Assertions.assertEquals(error, effectiveData.error());

        Assertions.assertEquals(url2, effectiveData.previousRedirection().url());
        Assertions.assertEquals(httpCode2, effectiveData.previousRedirection().httpCode());
        Assertions.assertEquals(headers2, effectiveData.previousRedirection().headers());
        Assertions.assertEquals(error2, effectiveData.previousRedirection().error());

        Assertions.assertEquals(url3, effectiveData.previousRedirection().previousRedirection().url());
        Assertions.assertEquals(httpCode3, effectiveData.previousRedirection().previousRedirection().httpCode());
        Assertions.assertEquals(headers3, effectiveData.previousRedirection().previousRedirection().headers());
        Assertions.assertEquals(error3, effectiveData.previousRedirection().previousRedirection().error());
        Assertions.assertNull(effectiveData.previousRedirection().previousRedirection().previousRedirection());
    }

    private SiteDataPersister buildSiteDataPersister() {
        final Path cachePath = TestHelper.getTestDatapath(getClass());
        FileHelper.deleteDirectory(cachePath.toFile());
        return new SiteDataPersister(cachePath);
    }
}
