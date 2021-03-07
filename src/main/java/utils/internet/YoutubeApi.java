package utils.internet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import utils.ExitHelper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class YoutubeApi {

    private final String _applicationName;
    private final String _apiKey;
    private final String _referenceRegion;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public YoutubeApi(final String applicationName,
                      final String apiKey,
                      final String referenceRegion) {
        _applicationName = applicationName;
        _apiKey = apiKey;
        _referenceRegion = referenceRegion;
    }

    public YoutubeVideoDto getData(final String videoId) {
        return getData(Arrays.asList(videoId)).get(0);
    }

    public List<YoutubeVideoDto> getData(final List<String> videoIds) {
        final VideoListResponse responses  = getVideoInfo(videoIds);
        /*try {
            System.out.println(JSON_FACTORY.toPrettyString(responses));
        } catch (final IOException e) {
            e.printStackTrace();
        }*/

        final List<YoutubeVideoDto> dtos = new ArrayList<YoutubeVideoDto>();
        for (final Video video: responses.getItems()) {
            dtos.add(buildDto(video));
        }
        return dtos;
    }

    private YoutubeVideoDto buildDto(final Video video) {

        // see https://developers.google.com/youtube/v3/docs/videos
        // duration is incorrect : https://issuetracker.google.com/issues/35178038

        final String title = video.getSnippet().getTitle();

        final String description = video.getSnippet().getDescription();

        final String rec = video.getRecordingDetails().getRecordingDate();
        final LocalDate recordingDate = (rec == null) ? null
                                                      : localizeDate(rec);

        final LocalDate publicationDate = localizeDate(video.getSnippet().getPublishedAt());

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
            final YouTube youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                                                      .setApplicationName(_applicationName)
                                                      .setYouTubeRequestInitializer(new YouTubeRequestInitializer(_apiKey))
                                                      .build();
            final YouTube.Videos.List request = youtubeService.videos()
                                                              .list(Arrays.asList("snippet","contentDetails", "recordingDetails"));
            return request.setId(videoIds)
                          .execute();
        } catch (final GeneralSecurityException | IOException e) {
            ExitHelper.exit(e);
            // NOT REACHED
            return null;
        }
    }

    private Locale parseLanguage(final String lang) {
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

    private static LocalDate localizeDate(final String value) {
        ZonedDateTime zdt = ZonedDateTime.parse(value);
        ZonedDateTime zdttz = zdt.withZoneSameInstant(ZoneId.of(/*"Europe/Paris"*/"America/Los_Angeles"));
        LocalDate localDate = zdttz.toLocalDate();
        return localDate;
    }
}