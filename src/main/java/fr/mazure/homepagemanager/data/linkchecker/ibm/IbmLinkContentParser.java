package fr.mazure.homepagemanager.data.linkchecker.ibm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.NotGzipException;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for IBM articles
 */
public class IbmLinkContentParser extends LinkDataExtractor {

    private final String _title;
    private final String _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;
    private final ContentParserException _exception;
    private final ContentParserException _authorException;
    private final boolean _articleIsLost;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public IbmLinkContentParser(final String url,
                                final String data,
                                final CachedSiteDataRetriever retriever) {
        super(url, retriever);
        String json = null;
        try {
            json = getStructureJson(url);
        } catch (final IOException | NotGzipException e) {
            _articleIsLost = e instanceof NotGzipException;
            _exception = new ContentParserException("failed to get JSON data for " + url, e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }

        _articleIsLost = false;

        String title;
        String subtitle;
        LocalDate publicationDate;
        List<String> authorNames;

        try {
            final JSONObject payload = new JSONObject(json);
            final JSONObject results = payload.getJSONArray("results").getJSONObject(0);
            title = results.getString("title");
            subtitle = results.getString("subtitle").trim();
            publicationDate = LocalDateTime.parse(results.getString("publish_date")).toLocalDate();
            final JSONArray contributors = results.getJSONArray("contributors");
            authorNames = new ArrayList<>(contributors.length());
            for (int i = 0; i < contributors.length(); i++) {
                final String contributor = contributors.getJSONObject(i).getString("name");
                authorNames.add(contributor);
            }
        } catch (final JSONException e) {
            _exception = new ContentParserException("failed to parse JSON data for " + url + ". The JSON payload is \"" + json + "\"", e);
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            _authorException = null;
            return;
        }
        _title = title;
        _subtitle = subtitle;
        _publicationDate = publicationDate;
        _authors = new ArrayList<>(authorNames.size());
        _exception = null;
        ContentParserException authorException = null;
        for (final String name: authorNames) {
            try {
                _authors.add(LinkContentParserUtils.parseAuthorName(name));
            } catch (final ContentParserException e) {
                authorException = new ContentParserException("failed to parse author name", e);
            }
        }
        _authorException = authorException;
    }

    /**
     * @return true if IBM lost the article
     */
    public boolean articleIsLost() {
        return _articleIsLost;
    }

    /**
     * @return title
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public String getTitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _title;
    }

    /**
     * @return subtitle, empty if the is none
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return Optional.of(_subtitle);
    }

    /**
     * @return creation date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return Optional.of(_publicationDate);
    }

    /**
     * @return authors, empty list if there is none
     * @throws ContentParserException Failure to extract the information
     */
    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        if (_authorException != null) {
            throw _authorException;
        }
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        final ExtractedLinkData linkData = new ExtractedLinkData(getTitle(),
                                                                 new String[] { },
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { getLanguage() },
                                                                 Optional.empty(),
                                                                 Optional.empty());
        final List<ExtractedLinkData> list = new ArrayList<>(1);
        list.add(linkData);
        return list;
    }

    @Override
    public Locale getLanguage() {
        return Locale.ENGLISH;
    }

    private static String getStructureJson(final String url) throws IOException, NotGzipException {
        final String urlJsonStructure = url.replaceFirst("//developer.ibm.com/articles/", "//developer.ibm.com/middleware/v1/contents/articles/")
                                           .replaceFirst("//developer.ibm.com/tutorials/", "//developer.ibm.com/middleware/v1/contents/tutorials/")
                                           .replaceFirst("/$", "");
        return SynchronousSiteDataRetriever.getGzippedContent(urlJsonStructure, false);
    }
}
