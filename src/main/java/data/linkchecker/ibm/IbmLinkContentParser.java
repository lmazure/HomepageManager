package data.linkchecker.ibm;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.internet.NotGzipException;
import data.internet.SynchronousSiteDataRetriever;
import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentParserUtils;
import utils.xmlparsing.AuthorData;

/**
*
*/
public class IbmLinkContentParser {

    private final String _title;
    private final String _subtitle;
    private final LocalDate _publicationDate;
    private final List<AuthorData> _authors;
    private final ContentParserException _exception;
    private final ContentParserException _authorException;
    private final SynchronousSiteDataRetriever _retriever;
    private final boolean _articleIsLost;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     */
    public IbmLinkContentParser(final String data,
                                final String url) {

        _retriever = new SynchronousSiteDataRetriever(null);

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
                _authors.add(LinkContentParserUtils.getAuthor(name));
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
    public String getSubtitle() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _subtitle;
    }

    /**
     * @return creation date, empty if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public LocalDate getDate() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        return _publicationDate;
    }

    /**
     * @return authors, empty list if there is none
     * @throws ContentParserException Failure to extract the information
     */
    public List<AuthorData> getAuthors() throws ContentParserException {
        if (_exception != null) {
            throw _exception;
        }
        if (_authorException != null) {
            throw _authorException;
        }
        return _authors;
    }

    private String getStructureJson(final String url) throws IOException, NotGzipException {
        final String urlJsonStructure = url.replaceFirst("//developer.ibm.com/articles/", "//developer.ibm.com/middleware/v1/contents/articles/")
                                           .replaceFirst("//developer.ibm.com/tutorials/", "//developer.ibm.com/middleware/v1/contents/tutorials/")
                                           .replaceFirst("/$", "");
        return _retriever.getGzippedContent(urlJsonStructure, false);
    }
}
