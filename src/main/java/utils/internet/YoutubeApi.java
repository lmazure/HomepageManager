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
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    public YoutubeVideoDto getData(final String id) {
        return getData(Arrays.asList(id)).get(0);
    }

    public List<YoutubeVideoDto> getData(final List<String> ids) {
        final VideoListResponse responses  = getVideoInfo(ids);
        // System.out.println(responses);

        final List<YoutubeVideoDto> dtos = new ArrayList<YoutubeVideoDto>();
        for (final Video video: responses.getItems()) {
            dtos.add(buildDto(video));
        }
        return dtos;
    }

    private YoutubeVideoDto buildDto(final Video video) { 

        // see https://developers.google.com/youtube/v3/docs/videos

        final String title = video.getSnippet().getTitle();

        final String description = video.getSnippet().getDescription();

        final String rec = video.getRecordingDetails().getRecordingDate();
        final Optional<LocalDate> recordingDate = (rec == null) ? Optional.empty()
                                                                : Optional.of(ZonedDateTime.parse(rec).toLocalDate());

        final LocalDate publicationDate = ZonedDateTime.parse(video.getSnippet().getPublishedAt()).toLocalDate();

        final Duration duration = Duration.parse(video.getContentDetails().getDuration());

        final Optional<Locale> textLanguage = parseLanguage(video.getSnippet().getDefaultLanguage());

        final Optional<Locale> audioLanguage = parseLanguage(video.getSnippet().getDefaultAudioLanguage());

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

    private VideoListResponse getVideoInfo(final List<String> ids) {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            final YouTube youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                                                      .setApplicationName(_applicationName)
                                                      .setYouTubeRequestInitializer(new YouTubeRequestInitializer(_apiKey))
                                                      .build();
            final YouTube.Videos.List request = youtubeService.videos()
                    .list(Arrays.asList("snippet","contentDetails", "recordingDetails"));
            return request.setId(ids)
                          .execute();
        } catch (final GeneralSecurityException | IOException e) {
            ExitHelper.exit(e);
            // NOT REACHED
            return null;
        }
    }

    private Optional<Locale> parseLanguage(final String lang) {
        if (lang == null) {
            return Optional.empty();
        } else if (lang.equals("fr")) {
            return Optional.of(Locale.FRENCH); 
        } else {
            ExitHelper.exit("language to be implemented");
        }

        // NOTREACHED
        return null;
    }
}