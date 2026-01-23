package fr.mazure.homepagemanager.utils.internet;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;

/**
 * Helper methods to manage YouTube videos
 */
public class YouTubeHelper {

    private static final Pattern _pattern = Pattern.compile("\"url\":\"/watch\\?v=([-_0-9a-zA-Z]{11})\\\\");

    private Optional<String> _getVideoURL;

    /**
     * retrieve the URL of a video given its channel and title
     *
     * @param channelName Name of the channel
     * @param videoTitle Title of the video
     * @param retriever data retriever
     *
     * @return URL of the video
     */
    public Optional<String> getVideoURL(final String channelName,
                                        final String videoTitle,
                                        final CachedSiteDataRetriever retriever) {
        _getVideoURL = null;
        final String encodedChannelName = UrlHelper.encodeUrlPart(channelName);
        final String encodedVideoTitle = UrlHelper.encodeUrlPart(videoTitle);
        final String searchURL = "https://www.youtube.com/results?search_query=%22" + encodedVideoTitle + "%22+%22" + encodedChannelName + "%22";

        retriever.retrieve(searchURL, this::consumeYouTubeData, false);

        return _getVideoURL;
    }

    private void consumeYouTubeData(final FullFetchedLinkData siteData) {
        final String payload = HtmlHelper.slurpFile(siteData.dataFileSection().get());

        final String[] lines = payload.split("\n");
        for (final String line : lines) {
            final Matcher matcher = _pattern.matcher(line);
            if (matcher.find()) {
                _getVideoURL = Optional.of("https://www.youtube.com/watch?v=" + matcher.group(1));
                return;
            }
        }

        _getVideoURL = Optional.empty();
    }
}
