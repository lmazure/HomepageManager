package utils.internet;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

public class YoutubeVideoDto {

    private final String _title;
    private final String _description;
    private final Optional<LocalDate> _recordingDate;
    private final LocalDate _publicationDate;
    private final Duration _duration;
    private final Optional<Locale> _textLanguage;
    private final Optional<Locale> _audioLanguage;
    private final boolean _isAllowed;

    public YoutubeVideoDto(final String title,
                           final String description,
                           final Optional<LocalDate> recordingDate,
                           final LocalDate publicationDate,
                           final Duration duration,
                           final Optional<Locale> textLanguage,
                           final Optional<Locale> audioLanguage,
                           final boolean isAllowed) {
        _title = title;
        _description = description;
        _recordingDate = recordingDate;
        _publicationDate = publicationDate;
        _duration = duration;
        _audioLanguage = audioLanguage;
        _textLanguage = textLanguage;
        _isAllowed = isAllowed;
    }

    public String getTitle() {
        return _title;
    }

    public String getDescription() {
        return _description;
    }

    public Optional<LocalDate> getRecordingDate() {
        return _recordingDate;
    }

    public LocalDate getPublicationDate() {
        return _publicationDate;
    }

    public Duration getDuration() {
        return _duration;
    }

    public Optional<Locale> getTextLanguage() {
        return _textLanguage;
    }

    public Optional<Locale> getAudioLanguage() {
        return _audioLanguage;
    }

    public boolean isAllowed() {
        return _isAllowed;
    }
}
