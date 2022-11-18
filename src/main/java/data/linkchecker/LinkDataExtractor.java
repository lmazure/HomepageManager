package data.linkchecker;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.AuthorData;

public abstract class LinkDataExtractor {

    private final String _url;

    public LinkDataExtractor(final String url) {
        _url = url;
    }

    protected String getUrl() {
        return _url;
    }

    public abstract String getTitle() throws ContentParserException;

    public abstract Optional<String> getSubtitle() throws ContentParserException;

    public abstract Optional<TemporalAccessor> getDate() throws ContentParserException;

    public abstract List<AuthorData> getSureAuthors() throws ContentParserException;

    public abstract Locale getLanguage() throws ContentParserException;

    /**
     * @return the list of probable authors
     *   (they may be present and it is probable that they are effectively present)
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
    public List<AuthorData> getProbableAuthors() throws ContentParserException {
        return new ArrayList<>(0);
    }

    /**
     * @return the list of possible authors
     *   (they may be present but it is probable that they are not effectively present)
     * @throws ContentParserException
     */
    @SuppressWarnings("static-method")
    public List<AuthorData> getPossibleAuthors() throws ContentParserException {
        return new ArrayList<>(0);
    }

    public abstract List<ExtractedLinkData> getLinks() throws ContentParserException;
}
