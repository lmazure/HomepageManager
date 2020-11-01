package utils.internet;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;

public class YoutubeVideoDto implements Serializable {

    private static final long serialVersionUID = 8457634280849981665L;

    private final String _title;
    private final String _description;
    private final LocalDate _recordingDate;
    private final LocalDate _publicationDate;
    private final Duration _duration;
    private final Locale _textLanguage;
    private final Locale _audioLanguage;
    private final boolean _isAllowed;

    public YoutubeVideoDto(final String title,
                           final String description,
                           final LocalDate recordingDate,
                           final LocalDate publicationDate,
                           final Duration duration,
                           final Locale textLanguage,
                           final Locale audioLanguage,
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

    public LocalDate getRecordingDate() {
        return _recordingDate;
    }

    public LocalDate getPublicationDate() {
        return _publicationDate;
    }

    public Duration getDuration() {
        return _duration;
    }

    public Locale getTextLanguage() {
        return _textLanguage;
    }

    public Locale getAudioLanguage() {
        return _audioLanguage;
    }

    public boolean isAllowed() {
        return _isAllowed;
    }
}
