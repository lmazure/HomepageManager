package fr.mazure.homepagemanager.data.dataretriever;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Facade class for retrieving site data
 */
public class SiteDataRetriever {

    private final AsynchronousSiteDataRetriever _retriever;

    /**
     * @param path directory where the persistence files should be written
     */
    public SiteDataRetriever(final Path path) {
        final SiteDataPersister persister = new SiteDataPersister(path);
        _retriever = new AsynchronousSiteDataRetriever(persister);
    }

    /**
     * @param url URL of the link to retrieve
     * @param consumer consumer of the site data
     * @param doNotUseCookies if true, cookies will not be recorded and resend while following redirections
     */
    public void retrieve(final String url,
                         final Consumer<FullFetchedLinkData> consumer,
                         final boolean doNotUseCookies) {

        _retriever.retrieve(url, consumer, doNotUseCookies);
    }
}
