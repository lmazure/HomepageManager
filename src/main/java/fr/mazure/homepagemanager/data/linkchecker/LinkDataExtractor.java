package fr.mazure.homepagemanager.data.linkchecker;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Extract some information for a link
 */
public abstract class LinkDataExtractor {

    private final String _url;
    private final CachedSiteDataRetriever _retriever;

    /**
     * @param url URL of the link
     */
    protected LinkDataExtractor(final String url,
                                final CachedSiteDataRetriever retriever) {
        _url = url;
        _retriever = retriever;
    }

    protected String getUrl() {
        return _url;
    }

    protected CachedSiteDataRetriever getRetriever() {
        return _retriever;
    }

    /**
     * @return title
     */
    public abstract String getTitle();

    /**
     * @return subtitle, empty if the is none
      */
    public abstract Optional<String> getSubtitle();

    /**
     * @return creation date, empty if there is none
     */
    public abstract Optional<TemporalAccessor> getCreationDate();

    /**
     * @return publication date, empty if there is none
     */
    public abstract Optional<TemporalAccessor> getPublicationDate();

    /**
     * @return the list of sure authors
     */
    public abstract List<AuthorData> getSureAuthors();

    /**
     * @return language
     */
    public abstract Locale getLanguage();

    /**
     * @return the list of probable authors
     *   (they may be present and it is probable that they are effectively present)
     */
    @SuppressWarnings("static-method")
    public List<AuthorData> getProbableAuthors() {
        return Collections.emptyList();
    }

    /**
     * @return the list of possible authors
     *   (they may be present but it is probable that they are not effectively present)
     */
    @SuppressWarnings("static-method")
    public List<AuthorData> getPossibleAuthors() {
        return Collections.emptyList();
    }

    /**
     * @return the list of links
     */
    public abstract List<ExtractedLinkData> getLinks();

    /**
     * @return the duration
     */
    @SuppressWarnings("static-method")
    public Optional<Duration> getDuration() {
        return Optional.empty();
    }
}
