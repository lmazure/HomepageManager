package data.linkchecker;

import java.time.temporal.TemporalAccessor;
import java.util.List;
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

    public abstract Optional<TemporalAccessor> getDate() throws ContentParserException;

    public abstract List<AuthorData> getSureAuthors() throws ContentParserException;

    public abstract List<AuthorData> getProbableAuthors() throws ContentParserException;

    public abstract List<AuthorData> getPossibleAuthors() throws ContentParserException;

    public abstract List<ExtractedLinkData> getLinks() throws ContentParserException;
}
