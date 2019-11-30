package data.internet.test;



import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import data.internet.SiteDataRetrieval;
import data.internet.SiteDataRetriever;

class SiteDataRetrieverTest {

    @Test
    void test() {
        
        final Path cachePath = Paths.get("H:\\Documents\\tmp\\hptmp");
        final SiteDataRetriever retriever = new SiteDataRetriever(cachePath);
        try {
            final SiteDataRetrieval retrieval = retriever.retrieve(new URL("http://example.com"));
        } catch (final MalformedURLException e) {
            Assertions.fail();
        }
        Assertions.assertTrue(true);
    }
    
}