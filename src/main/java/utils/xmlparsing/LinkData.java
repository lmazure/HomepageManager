package utils.xmlparsing;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

public class LinkData {

    private final String _title;
    private final String _subtitles[];
    private final String _url;
    private final Optional<Status> _status;
    private final Optional<Protection> _protection;
    private final String _formats[];
    private final String _languages[];
    private final Optional<Duration> _duration;
    private final Optional<TemporalAccessor> _publicationDate;

    public enum Status {
        DEAD,
        OBSOLETE,
        ZOMBIE
    }

    public enum Protection {
        FREE_REGISTRATION,
        PAYED_REGISTRATION
    }

    public LinkData(final String title,
                    final String subtitles[],
                    final String url,
                    final Optional<Status> status,
                    final Optional<Protection> protection,
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

    public Optional<Status> getStatus() {
        return _status;
    }

    public Optional<Protection> getProtection() {
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

    public static Status parseStatus(final String status) {
        if (status.equals("dead")) return Status.DEAD;
        if (status.equals("obsolete")) return Status.OBSOLETE;
        if (status.equals("zombie")) return Status.ZOMBIE;
        throw new UnsupportedOperationException("Illegal status value (" + status + ")");
    }

    public static Protection parseProtection(final String protection) {
        if (protection.equals("free_registration")) return Protection.FREE_REGISTRATION;
        if (protection.equals("payed_registration")) return Protection.PAYED_REGISTRATION;
        throw new UnsupportedOperationException("Illegal protection value (" + protection + ")");
    }
}