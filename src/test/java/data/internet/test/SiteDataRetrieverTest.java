package data.internet.test;



import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import data.internet.SiteData;
import data.internet.SiteDataRetrieval;
import data.internet.SiteDataRetriever;

class SiteDataRetrieverTest {

    @Test
    void test() {
        
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp\\SiteDataRetrieverTest");
        final SiteDataRetriever retriever = new SiteDataRetriever(cachePath);
        final SiteDataRetrieval retrieval = retriever.retrieve(buildURL("http://example.com"));
        SiteData data = null;
        try {
            data = retrieval.getUpToDateData().get();
        } catch (@SuppressWarnings("unused") InterruptedException | ExecutionException e) {
            Assertions.fail();
        }
        Assertions.assertNotNull(data);
        assert(data != null);
        Assertions.assertEquals(200, data.getHttpCode());
        Assertions.assertEquals(200, data.getHttpCode());
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