package data.internet.test;



import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import data.internet.SiteData;
import data.internet.SiteDataRetriever;

class SiteDataRetrieverTest {

    @Test
    void basicHttpRequest() {
        
        final SiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(buildURL("http://example.com"),
                           (SiteData d) -> {
                               consumerHasBeenCalled.set(true);
                               assertData(d);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    @Test
    void basicHttpsRequest() {
        
        final SiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(buildURL("https://example.com"),
                           (SiteData d) -> {
                               consumerHasBeenCalled.set(true);
                               assertData(d);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private void assertData(final SiteData d) {
        Assertions.assertEquals(200, d.getHttpCode());                               
        Assertions.assertTrue(d.getHeaders().keySet().contains("Content-Type"));
        Assertions.assertEquals(1, d.getHeaders().get("Content-Type").size());
        Assertions.assertEquals("text/html; charset=UTF-8", d.getHeaders().get("Content-Type").get(0));
        Assertions.assertTrue(d.getHeaders().keySet().contains("Cache-Control"));
        Assertions.assertEquals(1, d.getHeaders().get("Cache-Control").size());
        Assertions.assertEquals("max-age=604800", d.getHeaders().get("Cache-Control").get(0));
        String data;
        try {
            data = Files.readString(d.getDataFile().toPath());
            Assertions.assertNotEquals(-1, data.indexOf("This domain is for use in illustrative examples in documents."));
        } catch (@SuppressWarnings("unused") final IOException e) {
            Assertions.fail("failure to read data file");
        }        
    }
    
    private SiteDataRetriever buildDataSiteRetriever() {
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\SiteDataRetrieverTest");
        return new SiteDataRetriever(cachePath);
    }
    
    private URL buildURL(final String str) {
        try {
            return new URL(str);
        } catch (@SuppressWarnings("unused") final MalformedURLException e) {
            Assertions.fail("failed to convert " + str + " to URL");
            return null;
        }
    }
}