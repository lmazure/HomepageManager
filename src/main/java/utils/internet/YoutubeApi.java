package utils.internet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class YoutubeApi {
    private static final String APPLICATION_NAME = "HomepageManager";
    private static final String API_KEY = "XXX";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    public static void main(String[] args) throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        final YouTube youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                                                  .setApplicationName(APPLICATION_NAME)
                                                  .setYouTubeRequestInitializer(new YouTubeRequestInitializer(API_KEY))
                                                  .build();
        // see https://developers.google.com/youtube/v3/docs/videos
        final YouTube.Videos.List request = youtubeService.videos()
                                                          .list(Arrays.asList("snippet","contentDetails","statistics", "status", "recordingDetails"));
        final VideoListResponse response = request.setId(Arrays.asList("aQo8tYQuWQw","lFEgohhfxOA", "dM_JivN3HvI"))
                                                  .execute();
        for (Video video: response.getItems()) {
            System.out.println("title = " + video.getSnippet().getTitle());
            System.out.println("description = " + video.getSnippet().getDescription());
            System.out.println("publication date = " + video.getSnippet().getPublishedAt());
            System.out.println("recording date = " + video.getRecordingDetails().getRecordingDate());
            System.out.println("duration = " + video.getContentDetails().getDuration());
            /*
             * contentDetails.regionRestriction.allowed[]   list
               A list of region codes that identify countries where the video is viewable. If this property is present and a country is not listed in its value, then the video is blocked from appearing in that country. If this property is present and contains an empty list, the video is blocked in all countries.
               contentDetails.regionRestriction.blocked[]  list
               A list of region codes that identify countries where the video is blocked. If this property is present and a country is not listed in its value, then the video is viewable in that country. If this property is present and contains an empty list, the video is viewable in all countries.
             */
            System.out.println("---------------------------------------------");
        }
        System.out.println(response);
    }
}