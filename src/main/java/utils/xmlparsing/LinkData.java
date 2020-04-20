package utils.xmlparsing;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class LinkData {

    private final String _title;
    private final String _subtitles[];
    private final String _url;
    private final Optional<String> _status;
    private final Optional<String> _protection;
    private final String _formats[];
    private final String _languages[];
    private final Optional<Duration> _duration;
    private final Optional<TemporalAccessor> _publicationDate;

    public LinkData(final String title,
                    final String subtitles[],
                    final String url,
                    final Optional<String> status,
                    final Optional<String> protection,
                    final String formats[],
                    final String languages[],
                    final Optional<Duration> duration,
                    final Optional<TemporalAccessor> publicationDate) {
        _title = title;
        _subtitles = subtitles;
        _url = url;
        _status = status;
        _protection = protection;
        _formats = formats;
        _languages = languages;
        _duration = duration;
        _publicationDate = publicationDate;
    }

    public String getTitle() {
        return _title;
    }

    public String[] getSubtitles() {
        return _subtitles;
    }

    public String getUrl() {
        return _url;
    }

    public Optional<String> getStatus() {
        return _status;
    }

    public Optional<String> getProtection() {
        return _protection;
    }

    public String[] getFormats() {
        return _formats;
    }

    public String[] getLanguages() {
        return _languages;
    }

    public Optional<Duration> getDuration() {
        return _duration;
    }

    public Optional<TemporalAccessor> getPublicationDate() {
        return _publicationDate;
    }
}