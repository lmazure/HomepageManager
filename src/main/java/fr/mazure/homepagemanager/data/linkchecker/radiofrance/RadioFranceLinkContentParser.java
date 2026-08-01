package fr.mazure.homepagemanager.data.linkchecker.radiofrance;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.SiteSlurper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentParserUtils;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 * Data extractor for Radio France podcasts
 */
public class RadioFranceLinkContentParser extends LinkDataExtractor {

    private static final String s_sourceName = "Radio France";

    private final String _title;
    private final String _subtitle;
    private final Optional<TemporalAccessor> _publicationDate;
    private final Optional<Duration> _duration;
    private final List<AuthorData> _authors;
    private final Locale _language;
    private final List<ExtractedLinkData> _links;

    private static final TextParser s_jsonParser
        = new TextParser("<script type=\"application/ld\\+json\">",
                         "</script>",
                         s_sourceName,
                         "JSON-LD");

    /**
     * Constructor
     * @param url URL of the link
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public RadioFranceLinkContentParser(final String url,
                                        final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        final SiteSlurper sluper = new SiteSlurper(getRetriever(), url);
        final String data = sluper.getContent();

        final String json = s_jsonParser.extract(data);
        final JSONObject payload;
        try {
            payload = new JSONObject(json);
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to parse JSON-LD in Radio France page", e);
        }

        final JSONArray graph;
        try {
            graph = payload.getJSONArray("@graph");
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to find @graph array in Radio France JSON-LD", e);
        }

        JSONObject newsArticle = null;
        JSONObject radioEpisode = null;
        for (int i = 0; i < graph.length(); i++) {
            try {
                final JSONObject item = graph.getJSONObject(i);
                final String type = item.getString("@type");
                if ("NewsArticle".equals(type)) {
                    newsArticle = item;
                } else if ("RadioEpisode".equals(type)) {
                    radioEpisode = item;
                }
            } catch (final JSONException e) {
                throw new ContentParserException("Failed to iterate @graph array in Radio France JSON-LD", e);
            }
        }

        if (newsArticle == null) {
            throw new ContentParserException("Missing NewsArticle in Radio France JSON-LD @graph");
        }
        if (radioEpisode == null) {
            throw new ContentParserException("Missing RadioEpisode in Radio France JSON-LD @graph");
        }

        try {
            _title = HtmlHelper.cleanContent(newsArticle.getString("headline"));

            final String description = newsArticle.getString("description");
            if (description == null || description.isEmpty()) {
                throw new ContentParserException("Missing description in Radio France JSON-LD");
            }
            _subtitle = HtmlHelper.cleanContent(description);

            final String datePublished = newsArticle.getString("datePublished");
            _publicationDate = Optional.of(OffsetDateTime.parse(datePublished).toLocalDate());

            final String inLanguage = newsArticle.getString("inLanguage");
            _language = Locale.of(inLanguage.split("-")[0]);

            final JSONArray authorArray = newsArticle.getJSONArray("author");
            final List<AuthorData> authorList = new ArrayList<>(authorArray.length());
            for (int i = 0; i < authorArray.length(); i++) {
                final JSONObject authorObj = authorArray.getJSONObject(i);
                final String authorName = authorObj.getString("name");
                authorList.add(LinkContentParserUtils.parseAuthorName(authorName));
            }
            _authors = authorList;

            final JSONObject mainEntity = radioEpisode.getJSONObject("mainEntity");
            final String durationStr = mainEntity.getString("duration");
            final String cleanedDuration = durationStr.replaceFirst("^P\\d+Y\\d+M", "P");
            _duration = Optional.of(Duration.parse(cleanedDuration));
        } catch (final JSONException e) {
            throw new ContentParserException("Failed to extract data from Radio France JSON-LD", e);
        }

        final ExtractedLinkData linkData = new ExtractedLinkData(_title,
                                                                 new String[] { _subtitle },
                                                                 getUrl(),
                                                                 Optional.empty(),
                                                                 Optional.empty(),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { _language },
                                                                 _duration,
                                                                 _publicationDate);
        final List<ExtractedLinkData> linkList = new ArrayList<>(1);
        linkList.add(linkData);
        _links = linkList;
    }

    /**
     * Determine if the link is managed
     *
     * @param url link
     * @return true if the link is managed
     */
    public static boolean isUrlManaged(final String url) {
        if (!UrlHelper.hasPrefix(url, "https://www.radiofrance.fr/franceinter/podcasts/") &&
            !UrlHelper.hasPrefix(url, "https://www.radiofrance.fr/franceculture/podcasts/")) {
                return false;
            }
        
        // check that this is a podcast (e.g. https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/la-mort-d-un-dictateur-7740763)
        // and not an overview page (e.g. https://www.radiofrance.fr/franceculture/podcasts/serie-devenir-staline) 
        final String urlEnd = url.split("/podcasts/")[1];
        return (urlEnd.contains("/"));
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
        return _publicationDate;
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return _publicationDate;
    }

    @Override
    public Optional<Duration> getDuration() {
        return _duration;
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        return _authors;
    }

    @Override
    public List<AuthorData> getProbableAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<AuthorData> getPossibleAuthors() {
        return Collections.emptyList();
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        return _links;
    }

    @Override
    public Locale getLanguage() {
        return _language;
    }
}
