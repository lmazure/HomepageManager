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
    private final boolean _articleIsLost;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public IbmLinkContentParser(final String url,
                                final String data,
                                final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);
        String json = null;
        try {
            json = getStructureJson(url);
        } catch (final NotGzipException _) {
            _articleIsLost = true;
            _title = null;
            _subtitle = null;
            _publicationDate = null;
            _authors = null;
            return;
        } catch (final IOException e) {
            throw new ContentParserException("failed to get JSON data for " + url, e);
        }

        _articleIsLost = false;

        try {
            final JSONObject payload = new JSONObject(json);
            final JSONObject results = payload.getJSONArray("results").getJSONObject(0);
            _title = results.getString("title");
            _subtitle = results.getString("subtitle").trim();
            _publicationDate = LocalDateTime.parse(results.getString("publish_date")).toLocalDate();
            final JSONArray contributors = results.getJSONArray("contributors");
            _authors = new ArrayList<>(contributors.length());
            for (int i = 0; i < contributors.length(); i++) {
                final String contributor = contributors.getJSONObject(i).getString("name");
                _authors.add(LinkContentParserUtils.parseAuthorName(contributor));
            }
        } catch (final JSONException e) {
            throw new ContentParserException("failed to parse JSON data for " + url + ". The JSON payload is \"" + json + "\"", e);
        }
    }

    /**
     * @return true if IBM lost the article
     */
    public boolean articleIsLost() {
        return _articleIsLost;
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public Optional<String> getSubtitle() {
        return Optional.of(_subtitle);
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        return Optional.of(_publicationDate);
    }


    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
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
