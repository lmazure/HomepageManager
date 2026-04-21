package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.utils.EnvironmentHelper;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.internet.JsonHelper;
import fr.mazure.homepagemanager.utils.internet.UrlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Data extractor for YouTube channels
 */
public class YoutubeChannelUserLinkContentParser extends LinkDataExtractor {

    private static final String s_channelPrefix = "https://www.youtube.com/channel/";
    private static final String s_userPrefix = "https://www.youtube.com/user/";

    private Locale _language;

    /**
     * @param url URL of the link
     * @param retriever cache data retriever
     * @throws ContentParserException Failure to extract the information
     */
    public YoutubeChannelUserLinkContentParser(final String url,
                                               final CachedSiteDataRetriever retriever) throws ContentParserException {
        super(url, retriever);

        try {
            final String apiUrl = buildApiUrl(url);
            final JSONObject payload = retrieveApiPayload(apiUrl);
            final JSONObject item = getChannelItem(payload);
            if (item == null) {
                throw new ContentParserException("YouTube channel/user does not exist for URL \"" + url + "\"");
            }
            final JSONObject snippet = JsonHelper.getAsNode(item, "snippet");
            final String description = snippet.has("description") ? JsonHelper.getAsText(snippet, "description")
                                                                  : "";
            final Optional<Locale> lang = StringHelper.guessLanguage(description);
            _language = lang.orElse(Locale.ENGLISH);
        } catch (final IllegalStateException e) {
            throw new ContentParserException("Unexpected JSON", e);
        }
    }

    /**
     * @return language
     */
    @Override
    public Locale getLanguage() {
        return _language;
    }

    private static String buildApiUrl(final String url) throws ContentParserException {
        if (UrlHelper.hasPrefix(url, s_channelPrefix)) {
            final String channelId = extractIdentifier(url, s_channelPrefix);
            return "https://www.googleapis.com/youtube/v3/channels?part=snippet&id=" +
                   UrlHelper.encodeUrlPart(channelId) +
                   "&key=" +
                   UrlHelper.encodeUrlPart(EnvironmentHelper.getYoutubeApiKet());
        }
        if (UrlHelper.hasPrefix(url, s_userPrefix)) {
            final String userName = extractIdentifier(url, s_userPrefix);
            return "https://www.googleapis.com/youtube/v3/channels?part=snippet&forUsername=" +
                   UrlHelper.encodeUrlPart(userName) +
                   "&key=" +
                   UrlHelper.encodeUrlPart(EnvironmentHelper.getYoutubeApiKet());
        }
        throw new ContentParserException("Unsupported YouTube channel/user URL: \"" + url + "\"");
    }

    private static String extractIdentifier(final String url,
                                            final String prefix) throws ContentParserException {
        String identifier = url.substring(prefix.length());
        final int queryPos = identifier.indexOf('?');
        if (queryPos >= 0) {
            identifier = identifier.substring(0, queryPos);
        }
        if (identifier.endsWith("/")) {
            identifier = identifier.substring(0, identifier.length() - 1);
        }
        final int slashPos = identifier.indexOf('/');
        if (slashPos >= 0) {
            identifier = identifier.substring(0, slashPos);
        }
        if (identifier.isEmpty()) {
            throw new ContentParserException("Cannot extract channel/user identifier from URL: \"" + url + "\"");
        }
        return identifier;
    }

    private JSONObject retrieveApiPayload(final String apiUrl) throws ContentParserException {
        final JSONObject[] payload = new JSONObject[1];
        final ContentParserException[] errors = new ContentParserException[1];

        getRetriever().retrieve(apiUrl,
                                (final FullFetchedLinkData siteData) -> {
                                    if (siteData.dataFileSection().isEmpty()) {
                                        errors[0] = new ContentParserException("Failed to retrieve YouTube Data API payload from \"" + apiUrl + "\"");
                                        return;
                                    }
                                    final String rawPayload = HtmlHelper.slurpFile(siteData.dataFileSection().get());
                                    payload[0] = new JSONObject(rawPayload);
                                },
                                false);

        if (errors[0] != null) {
            throw errors[0];
        }
        if (payload[0] == null) {
            throw new ContentParserException("YouTube Data API payload is empty for \"" + apiUrl + "\"");
        }
        if (payload[0].has("error")) {
            throw new ContentParserException("YouTube Data API returned an error for \"" + apiUrl + "\": " + payload[0].getJSONObject("error").toString());
        }
        return payload[0];
    }

    private static JSONObject getChannelItem(final JSONObject payload) {
        if (!payload.has("items")) {
            return null;
        }
        if (payload.getJSONArray("items").length() == 0) {
            return null;
        }
        return payload.getJSONArray("items").getJSONObject(0);
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support title");
    }

    @Override
    public Optional<String> getSubtitle() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support subtitle");
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support date");
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support authors");
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support links");
    }
}
