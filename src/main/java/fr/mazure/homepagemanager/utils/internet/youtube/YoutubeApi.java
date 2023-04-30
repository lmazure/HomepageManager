package fr.mazure.homepagemanager.utils.internet.youtube;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import fr.mazure.homepagemanager.utils.ExitHelper;

/**
 *
 */
public class YoutubeApi {

    private final String _applicationName;
    private final String _apiKey;
    private final String _referenceRegion;
    private static final JsonFactory s_json_factory = JacksonFactory.getDefaultInstance();

    /**
     * @param applicationName Application name
     * @param apiKey API key
     * @param referenceRegion Region used to check for access restriction
     */
    public YoutubeApi(final String applicationName,
                      final String apiKey,
                      final String referenceRegion) {
        _applicationName = applicationName;
        _apiKey = apiKey;
        _referenceRegion = referenceRegion;
    }

    /**
     * @param videoId ID of the video
     * @return Video data
     */
    public YoutubeVideoDto getData(final String videoId) {
        return getData(Arrays.asList(videoId)).get(0);
    }

    /**
     * @param videoIds ID of the videos
     * @return Video data
     */
    public List<YoutubeVideoDto> getData(final List<String> videoIds) {
        final VideoListResponse responses  = getVideoInfo(videoIds);

        final List<YoutubeVideoDto> dtos = new ArrayList<>();
        for (final Video video: responses.getItems()) {
            dtos.add(buildDto(video));
        }
        return dtos;
    }

    private YoutubeVideoDto buildDto(final Video video) {

        // see https://developers.google.com/youtube/v3/docs/videos
        // duration is incorrect : https://issuetracker.google.com/issues/35178038

        // see https://developers.google.com/resources/api-libraries/documentation/youtube/v3/java/latest/com/google/api/services/youtube/YouTube.Videos.html

        final String title = video.getSnippet().getTitle();

        final String description = video.getSnippet().getDescription();

        final DateTime rec = video.getRecordingDetails().getRecordingDate();
        final LocalDate recordingDate = (rec == null) ? null
                                                      : convertYoutubeDateTimeToLocalDate(rec);

        final LocalDate publicationDate = convertYoutubeDateTimeToLocalDate(video.getSnippet().getPublishedAt());

        final Duration duration = Duration.parse(video.getContentDetails().getDuration());

        final Locale textLanguage = parseLanguage(video.getSnippet().getDefaultLanguage());

        final Locale audioLanguage = parseLanguage(video.getSnippet().getDefaultAudioLanguage());

        boolean isAllowed = true;
        if (video.getContentDetails().getRegionRestriction() != null) {
            if (video.getContentDetails().getRegionRestriction().getAllowed() != null) {
                ExitHelper.exit("region restrictions to be implemented");
            }
            if (video.getContentDetails().getRegionRestriction().getBlocked() == null) {
                ExitHelper.exit("unexpected situation");
            }
            isAllowed = !video.getContentDetails().getRegionRestriction().getBlocked().contains(_referenceRegion);
        }

        return new YoutubeVideoDto(title,
                                   description,
                                   recordingDate,
                                   publicationDate,
                                   duration,
                                   textLanguage,
                                   audioLanguage,
                                   isAllowed);
    }

    private VideoListResponse getVideoInfo(final List<String> videoIds) {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            final YouTube youtubeService = new YouTube.Builder(httpTransport, s_json_factory, null)
                                                      .setApplicationName(_applicationName)
                                                      .setYouTubeRequestInitializer(new YouTubeRequestInitializer(_apiKey))
                                                      .build();
            final YouTube.Videos.List request = youtubeService.videos()
                                                              .list("snippet,contentDetails,recordingDetails");
            return request.setId(String.join(",", videoIds))
                          .execute();
        } catch (final GeneralSecurityException | IOException e) {
            ExitHelper.exit(e);
            // NOT REACHED
            return null;
        }
    }

    private static Locale parseLanguage(final String lang) {
        if (lang == null) {
            return null;
        } else if (lang.equals("fr")) {
            return Locale.FRENCH;
        } else if (lang.equals("en")) {
            return Locale.ENGLISH;
        } else if (lang.equals("en-GB")) {
            return Locale.ENGLISH;
        } else if (lang.equals("en-US")) {
            return Locale.ENGLISH;
        } else {
            ExitHelper.exit("language to be implemented (" + lang + ")");
        }

        // NOTREACHED
        return null;
    }

    private static LocalDate convertYoutubeDateTimeToLocalDate(final DateTime dateTime) {
        final Instant instant = Instant.ofEpochMilli(dateTime.getValue()) ;
        final ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Paris")) ;
        return zdt.toLocalDate();
    }
}