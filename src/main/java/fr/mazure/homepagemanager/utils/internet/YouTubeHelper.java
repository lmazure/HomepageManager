/**
 * 
 */
package fr.mazure.homepagemanager.utils.internet;

/**
 * YouTube helper methods
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class YouTubeHelper {
    private static final String CHANNEL_NAME = "Oxide Computer Company";
    private static final String VIDEO_TITLE = "Querying Metrics with OxQL";

    /**
     * return the URL of a video given its channel and title
     *
     * @param channelName Name of the channel
     * @param videoTitle Title of the video
     *
     * @return URL of the video
     * @throws IOException
     */
    public static Optional<String> getVideoURL(final String channelName,
                                               final String videoTitle) throws IOException {
        String encodedChannelName = URLEncoder.encode(channelName, StandardCharsets.UTF_8.toString());
        String encodedVideoTitle = URLEncoder.encode(videoTitle, StandardCharsets.UTF_8.toString());
        String searchURL = "https://www.youtube.com/results?search_query=\"" + encodedVideoTitle + "\"+\"" + encodedChannelName + "\"";

        URL url = new URL(searchURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String videoURL = null;

        while ((inputLine = in.readLine()) != null) {
            Pattern pattern = Pattern.compile("\"url\":\"/watch\\?v=([^\"]+)\\\\u0026");
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                videoURL = "https://www.youtube.com/watch?v=" + matcher.group(1);
                break;
            }
        }
        in.close();

        return Optional.ofNullable(videoURL);
    }
}
