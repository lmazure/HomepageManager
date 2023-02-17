package fr.mazure.homepagemanager.data.linkchecker.youtubewatch;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.SecretRepository;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentChecker;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateArticleTitleCorrection;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.youtube.CachedYoutubeApi;
import fr.mazure.homepagemanager.utils.internet.youtube.YoutubeVideoDto;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
 *
 */
public class YoutubeWatchLinkContentChecker2 extends LinkContentChecker {

    private final YoutubeVideoDto _dto;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public YoutubeWatchLinkContentChecker2(final String url,
                                           final LinkData linkData,
                                           final Optional<ArticleData> articleData,
                                           final FileSection file) {
        super(url, linkData, articleData, file);

        final Path tmpPath = Paths.get("D:\\tmp");
        final CachedYoutubeApi api = new CachedYoutubeApi(SecretRepository.getYoutubeApplicationName(), SecretRepository.getYoutubeApiKey(), "FR", tmpPath);
        final String videoId = url.substring(url.toString().length() - 11);
        _dto = api.getData(videoId);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) {
        if (!_dto.isAllowed()) {
            return new LinkContentCheck("VideoNotPlayable",
                                        "video is not playable in FR",
                                        Optional.empty());
        }

        return null;
    }

    @Override
    public LinkContentCheck checkLinkTitle(final String data,
                                           final String title) {

        final String effectiveTitle = _dto.getTitle();

        if (!title.equals(effectiveTitle)) {
            return new LinkContentCheck("WrongTitle",
                                        "title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                        "\"",
                                        Optional.of(new UpdateArticleTitleCorrection(title, effectiveTitle, getUrl())));
        }

        return null;
    }

    @Override
    public LinkContentCheck checkLinkDuration(final String data,
                                              final Duration expectedDuration) {

        final Duration effectiveDuration = _dto.getDuration();

        if (!expectedDuration.equals(effectiveDuration)) {
            return new LinkContentCheck("WrongDuration",
                                        "expected duration " +
                                        expectedDuration +
                                        " is not in the real duration " +
                                        effectiveDuration,
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate)
    {

        if (creationDate.isEmpty()) {
            return new LinkContentCheck("MissingCreationDate",
                                        "YouTube link with no creation date",
                                        Optional.empty());
        }

        final TemporalAccessor date = publicationDate.isPresent() ? publicationDate.get() : creationDate.get();

        if (!(date instanceof LocalDate)) {
            return new LinkContentCheck("IncorrectDate",
                                        "Date without month or day",
                                        Optional.empty());
       }

        final LocalDate effectivePublicationDate = _dto.getPublicationDate();
        final LocalDate effectiveRecordingDate = _dto.getRecordingDate();
        String errorRecordingDate;
        String errorPublicationDate;

        if (effectiveRecordingDate != null) {
            if (publicationDate.isEmpty()) {
                errorRecordingDate = "video should have the creation date " +
                                      effectiveRecordingDate;
            } else if (!effectiveRecordingDate.equals(creationDate.get())) {
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
                return new LinkContentCheck("WrongDate",
                                            errorRecordingDate + " // " + errorPublicationDate,
                                            Optional.empty());
            }
            return new LinkContentCheck("WrongDate",
                                        errorRecordingDate,
                                        Optional.empty());
        }
        if (errorPublicationDate != null) {
            return new LinkContentCheck("WrongDate",
                                        errorPublicationDate,
                                        Optional.empty());
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
                return new LinkContentCheck("WrongLanguage",
                                            "language is \"" +
                                            language +
                                            "\" but this one is unexpected (verified with API)",
                                            Optional.empty());
            }
        } else {
            final Optional<Locale> lang = StringHelper.guessLanguage(_dto.getDescription());
            if (lang.isPresent() && !Arrays.asList(languages).contains(lang.get())) {
                return new LinkContentCheck("WrongLanguage",
                                            "language is \"" +
                                            language +
                                            "\" but this one is unexpected (verified with guessing)",
                                            Optional.empty());
            }
        }

        return null;
    }
}
