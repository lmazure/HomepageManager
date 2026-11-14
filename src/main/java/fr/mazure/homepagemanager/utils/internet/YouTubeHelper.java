package fr.mazure.homepagemanager.utils.internet;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.SiteSlurper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.utils.EnvironmentHelper;

/**
 * Helper methods to manage YouTube videos
 */
public class YouTubeHelper {

    /**
     * Retrieve the URL of a video given its channel and title.
     *
     * @param channelName Name of the channel
     * @param videoTitle  Title of the video
     * @param retriever   data retriever
     * @return URL of the video, or empty if not found
     * @throws ContentParserException failed to parse YouTube's answer
     * @throws IllegalStateException if the YOUTUBE_API_KEY environment variable is not set
     */
    public static Optional<String> getVideoURL(final String channelName,
                                               final String videoTitle,
                                               final CachedSiteDataRetriever retriever) throws ContentParserException {
        final String query = URLEncoder.encode(videoTitle + " \"" + channelName + "\"", StandardCharsets.UTF_8);
        final String searchURL = "https://www.googleapis.com/youtube/v3/search" +
                                 "?part=snippet" +
                                 "&type=video" +
                                 "&maxResults=1" +
                                 "&q=" + query +
                                 "&key=" + EnvironmentHelper.getYoutubeApiKey();

        final SiteSlurper sluper = new SiteSlurper(retriever, searchURL);
        final String data = sluper.getContent();

        JSONArray items;
        try {
            items = new JSONObject(data).getJSONArray("items");
        } catch(final JSONException e) {
            throw new ContentParserException("Failed to parse answer of " + searchURL + ": " + data, e);
        }

        if (items.isEmpty()) {
            return Optional.empty();
        }

        final JSONObject item = items.getJSONObject(0);
        final String channelTitle = item.getJSONObject("snippet")
                                        .optString("channelTitle", null);

        if (!channelName.equals(channelTitle)) {
            return Optional.empty();
        }

        final String videoId = item.getJSONObject("id")
                                   .optString("videoId", null);

        return (videoId != null && !videoId.isBlank())  ? Optional.of("https://www.youtube.com/watch?v=" + videoId)
                                                        : Optional.empty();
    }
}
