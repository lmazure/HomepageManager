package data.internet.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;

import data.internet.SiteData;

public class TestHelper {

    static public void assertData(final SiteData data) {
        Assertions.assertEquals(200, data.getHttpCode());                               
        Assertions.assertTrue(data.getHeaders().keySet().contains("Content-Type"));
        Assertions.assertEquals(1, data.getHeaders().get("Content-Type").size());
        Assertions.assertEquals("text/html; charset=UTF-8", data.getHeaders().get("Content-Type").get(0));
        Assertions.assertTrue(data.getHeaders().keySet().contains("Cache-Control"));
        Assertions.assertEquals(1, data.getHeaders().get("Cache-Control").size());
        Assertions.assertEquals("max-age=604800", data.getHeaders().get("Cache-Control").get(0));
        try {
            final String d = Files.readString(data.getDataFile().toPath());
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
