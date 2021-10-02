package data.linkchecker;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.LinkFormat;
import utils.xmlparsing.LinkProtection;
import utils.xmlparsing.LinkStatus;

public class ExtractedLinkData {

    private final String _title;
    private final String _subtitles[];
    private final String _url;
    private final Optional<LinkStatus> _status;
    private final Optional<LinkProtection> _protection;
    private final LinkFormat _formats[];
    private final Locale _languages[];
    private final Optional<Duration> _duration;
    private final Optional<TemporalAccessor> _publicationDate;

    public ExtractedLinkData(final String title,
                             final String subtitles[],
                             final String url,
                             final Optional<LinkStatus> status,
                             final Optional<LinkProtection> protection,
                             final LinkFormat formats[],
                             final Locale languages[],
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

    public Optional<LinkStatus> getStatus() {
        return _status;
    }

    public Optional<LinkProtection> getProtection() {
        return _protection;
    }

    public LinkFormat[] getFormats() {
        return _formats;
    }

    public Locale[] getLanguages() {
        return _languages;
    }

    public Optional<Duration> getDuration() {
        return _duration;
    }

    public Optional<TemporalAccessor> getPublicationDate() {
        return _publicationDate;
    }
}
