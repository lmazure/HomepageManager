package data.linkchecker;

import java.nio.file.Path;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import data.internet.SiteData;
import data.internet.SiteDataPersister;
import data.internet.SynchronousSiteDataRetriever;
import utils.HtmlHelper;
import utils.xmlparsing.AuthorData;

public abstract class LinkDataExtractor {

    private final String _url;
    private String _content;

    public LinkDataExtractor(final String url,
                             final Path cacheDirectory) {
        _url = url;
        final SiteDataPersister persister = new SiteDataPersister(cacheDirectory);
        final SynchronousSiteDataRetriever retriever = new SynchronousSiteDataRetriever(persister);
        retriever.retrieve(url, this::handleLinkData);
    }

    private void handleLinkData(@SuppressWarnings("unused") final Boolean isDataFresh,
                                final SiteData siteData) {
        if (siteData.getDataFile().isPresent()) {
            _content = HtmlHelper.slurpFile(siteData.getDataFile().get());
        }
    }

    protected String getUrl() {
        return _url;
    }

    protected String getContent() {
        return _content;
    }

    public abstract Optional<TemporalAccessor> getDate() throws ContentParserException;

    public abstract List<AuthorData> getSureAuthors() throws ContentParserException;

    public abstract List<AuthorData> getProbableAuthors() throws ContentParserException;

    public abstract List<AuthorData> getPossibleAuthors() throws ContentParserException;

    public abstract List<ExtractedLinkData> getLinks() throws ContentParserException;
}
