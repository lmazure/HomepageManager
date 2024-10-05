package fr.mazure.homepagemanager.data.linkchecker.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Base class for the LinkDataExtractor tests
 */
public class LinkDataExtractorTestBase {

    protected static void checkTitle(final Class<? extends LinkDataExtractor> clazz,
                                     final String url,
                                     final String expectedTitle) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(clazz);
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                           (final Boolean b, final FullFetchedLinkData d) -> {
                               Assertions.assertTrue(d.dataFileSection().isPresent());
                               final String data = HtmlHelper.slurpFile(d.dataFileSection().get());
                               try {
                                   final LinkDataExtractor parser = construct(clazz, url, data);
                                   Assertions.assertEquals(expectedTitle, parser.getTitle());
                               } catch (final ContentParserException e) {
                                   Assertions.fail("getTitle threw " + e.getMessage());
                               }
                               consumerHasBeenCalled.set(true);
                           },
                           false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private static LinkDataExtractor construct(final Class<? extends LinkDataExtractor> clazz,
                                               final String url,
                                               final String data) {
        try {
            final Constructor<LinkDataExtractor> constructor = (Constructor<LinkDataExtractor>)clazz.getConstructor(String.class, String.class);
            return constructor.newInstance(url, data);
        } catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            Assertions.fail("Error in reflexion code " + e.getMessage());
            return null;
        }
    }
}
