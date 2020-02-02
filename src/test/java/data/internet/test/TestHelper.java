package data.internet.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;

import data.internet.SiteData;

public class TestHelper {

    static public void assertData(final SiteData data) {
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
            final String d = Files.readString(data.getDataFile().get().toPath());
            Assertions.assertNotEquals(-1, d.indexOf("This domain is for use in illustrative examples in documents."));
        } catch (@SuppressWarnings("unused") final IOException e) {
            Assertions.fail("failure to read data file");
        }        
    }

    static public URL buildURL(final String str) {
        try {
            return new URL(str);
        } catch (@SuppressWarnings("unused") final MalformedURLException e) {
            Assertions.fail("failed to convert " + str + " to URL");
            return null;
        }
    }
}
