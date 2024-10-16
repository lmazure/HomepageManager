/**
 * 
 */
package fr.mazure.homepagemanager.utils.internet;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;

/**
 * 
 */
public class YouTubeHelper {

    private static final Pattern _pattern = Pattern.compile("\"url\":\"/watch\\?v=([^\"]+)\\\\u0026");

    /**
     * retrieve the URL of a video given its channel and title
     *
     * @param channelName Name of the channel
     * @param videoTitle Title of the video
     *
     * @return URL of the video
     * @throws IOException Failed to retrieve YouTube payload
     */
    public static Optional<String> getVideoURL(final String channelName,
                                               final String videoTitle) throws IOException {
        final String encodedChannelName = UrlHelper.encodeUrlPart(channelName);
        final String encodedVideoTitle = UrlHelper.encodeUrlPart(videoTitle);
        final String searchURL = "https://www.youtube.com/results?search_query=%22" + encodedVideoTitle + "%22+%22" + encodedChannelName + "%22";

        final String payload = SynchronousSiteDataRetriever.getContent(searchURL, true);
        final String[] lines = payload.split("\n");
            for (String line : lines) {
            final Matcher matcher = _pattern.matcher(line);
            if (matcher.find()) {
                return Optional.of("https://www.youtube.com/watch?v=" + matcher.group(1));
            }
        }

        return Optional.empty();
    }
}
