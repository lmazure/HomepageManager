package data.linkchecker;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import data.ParameterRepository;
import utils.StringHelper;
import utils.internet.CachedYoutubeApi;
import utils.internet.YoutubeVideoDto;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class YoutubeWatchLinkContentChecker2 extends LinkContentChecker {

    private CachedYoutubeApi _api;
    private YoutubeVideoDto _dto;

    public YoutubeWatchLinkContentChecker2(final URL url,
                                           final LinkData linkData,
                                           final Optional<ArticleData> articleData,
                                           final File file) {
        super(linkData, articleData, file);

        final Path tmpPath = Paths.get("D:\\tmp");
        _api = new CachedYoutubeApi(ParameterRepository.getYoutubeApplicationName(), ParameterRepository.getYoutubeApiKey(), "FR", tmpPath);
        final String videoId = url.toString().substring(url.toString().length() - 11);
        _dto = _api.getData(videoId);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) {
        if (!_dto.isAllowed()) {
            return new LinkContentCheck("video is not playable in FR");
        }

        return null;
    }

    @Override
    public LinkContentCheck checkLinkTitle(final String data,
                                           final String title) {

        final String effectiveTitle = _dto.getTitle();

        if (!title.equals(effectiveTitle)) {
            return new LinkContentCheck("title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                          "\"");
        }

        return null;
    }

    @Override
    public LinkContentCheck checkLinkDuration(final String data,
                                              final Duration expectedDuration) {

        final Duration effectiveDuration = _dto.getDuration();

        if (!expectedDuration.equals(effectiveDuration)) {
            return new LinkContentCheck("expected duration " +
                                        expectedDuration +
                                        " is not in the real duration " +
                                        effectiveDuration);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate)
    {

        if (creationDate.isEmpty()) {
            return new LinkContentCheck("YouTube link with no creation date");
        }

        final TemporalAccessor date = publicationDate.isPresent() ? publicationDate.get() : creationDate.get();

        if (!(date instanceof LocalDate)) {
            return new LinkContentCheck("Date without month or day");
       }

        final LocalDate effectivePublicationDate = _dto.getPublicationDate();
        final LocalDate effectiveRecordingDate = _dto.getRecordingDate();
        String errorRecordingDate;
        String errorPublicationDate;

        if (effectiveRecordingDate != null) {
            if (publicationDate.isEmpty()) {
                errorRecordingDate = "video should have the creation date " +
                                      effectiveRecordingDate;
            } else if (!effectiveRecordingDate.equals(creationDate.get()) ) {
                errorRecordingDate = "video should have the creation date " +
                                     effectiveRecordingDate +
                                     " instead of the indicated " +
                                     creationDate.get();
            } else {
                errorRecordingDate = null;
            }
        } else {
            errorRecordingDate = null;
        }

        if (publicationDate.isPresent()) {
            if (!effectivePublicationDate.equals(publicationDate.get())) {
                errorPublicationDate = "video should have the publication date " +
                                       effectivePublicationDate +
                                       " instead of the indicated " +
                                       publicationDate.get();
            } else {
                errorPublicationDate = null;
            }
        } else {
            if (!effectivePublicationDate.equals(creationDate.get())) {
                errorPublicationDate = "video should have the publication date " +
                                       effectivePublicationDate +
                                       " instead of the indicated " +
                                       creationDate.get();
            } else {
                errorPublicationDate = null;
            }
        }

        if (errorRecordingDate != null) {
            if (errorPublicationDate != null) {
                return new LinkContentCheck(errorRecordingDate + " // " + errorPublicationDate);
            }
            return new LinkContentCheck(errorRecordingDate);
        }
        if (errorPublicationDate != null) {
            return new LinkContentCheck(errorPublicationDate);
        }
        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        final Locale language = _dto.getAudioLanguage();

        if (language != null) {
            if (!Arrays.asList(languages).contains(language)) {
                return new LinkContentCheck("language is \"" +
                                            language +
                                            "\" but this one is unexpected (verified with API)");
            }            
        } else {
            if (!Arrays.asList(languages).contains(StringHelper.guessLanguage(_dto.getDescription()))) {
                return new LinkContentCheck("language is \"" +
                                            language +
                                            "\" but this one is unexpected (verified with guessing)");
            }            
        }

        return null;
    }
}
