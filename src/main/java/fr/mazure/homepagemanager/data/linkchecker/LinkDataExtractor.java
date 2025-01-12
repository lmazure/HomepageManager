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
     * @throws ContentParserException Failure to extract the information
     */
    public abstract String getTitle() throws ContentParserException;

    /**
     * @return subtitle, empty if the is none
     * @throws ContentParserException Failure to extract the information
     */
    public abstract Optional<String> getSubtitle() throws ContentParserException;

    /**
     * @return creation date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public abstract Optional<TemporalAccessor> getCreationDate() throws ContentParserException;

    /**
     * @return publication date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public abstract Optional<TemporalAccessor> getPublicationDate() throws ContentParserException;

    /**
     * @return authors, empty list if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public abstract List<AuthorData> getSureAuthors() throws ContentParserException;

    /**
     * @return language
     * @throws ContentParserException Failure to extract the information
     */
    public abstract Locale getLanguage() throws ContentParserException;

    /**
     * @return the list of probable authors
     *   (they may be present and it is probable that they are effectively present)
     * @throws ContentParserException Failure to extract the information
     */
    @SuppressWarnings("static-method")
    public List<AuthorData> getProbableAuthors() throws ContentParserException {
        return Collections.emptyList();
    }

    /**
     * @return the list of possible authors
     *   (they may be present but it is probable that they are not effectively present)
     * @throws ContentParserException Failure to extract the information
     */
    @SuppressWarnings("static-method")
    public List<AuthorData> getPossibleAuthors() throws ContentParserException {
        return Collections.emptyList();
    }

    /**
     * @return the list of links
     * @throws ContentParserException Failure to extract the information
     */
    public abstract List<ExtractedLinkData> getLinks() throws ContentParserException;

    /**
     * @return the duration
     * @throws ContentParserException Failure to extract the information
     */
    @SuppressWarnings("static-method")
    public Optional<Duration> getDuration()  throws ContentParserException{
        return Optional.empty();
    }
}
