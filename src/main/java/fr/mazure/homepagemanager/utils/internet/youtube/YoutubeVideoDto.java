package fr.mazure.homepagemanager.utils.internet.youtube;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;

/**
 *
 */
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

    /**
     * @param title
     * @param description
     * @param recordingDate
     * @param publicationDate
     * @param duration
     * @param textLanguage
     * @param audioLanguage
     * @param isAllowed
     */
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

    /**
     * @return
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @return
     */
    public String getDescription() {
        return _description;
    }

    /**
     * @return
     */
    public LocalDate getRecordingDate() {
        return _recordingDate;
    }

    /**
     * @return
     */
    public LocalDate getPublicationDate() {
        return _publicationDate;
    }

    /**
     * @return
     */
    public Duration getDuration() {
        return _duration;
    }

    /**
     * @return
     */
    public Locale getTextLanguage() {
        return _textLanguage;
    }

    /**
     * @return
     */
    public Locale getAudioLanguage() {
        return _audioLanguage;
    }

    /**
     * @return
     */
    public boolean isAllowed() {
        return _isAllowed;
    }
}
