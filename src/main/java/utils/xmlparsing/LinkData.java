package utils.xmlparsing;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Optional;

public class LinkData {

    private final String _title;
    private final String _subtitles[];
    private final String _url;
    private final Optional<Status> _status;
    private final Optional<Protection> _protection;
    private final Format _formats[];
    private final Locale _languages[];
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

    public enum Format {
        HTML,
        FLASH,
        FLASH_VIDEO,
        MP3,
        MP4,
        PDF,
        POSTSCRIPT,
        POWERPOINT,
        REALMEDIA,
        TXT,
        WINDOWS_MEDIA_PLAYER,
        WORD
    }
    public LinkData(final String title,
                    final String subtitles[],
                    final String url,
                    final Optional<Status> status,
                    final Optional<Protection> protection,
                    final Format formats[],
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

    public Optional<Status> getStatus() {
        return _status;
    }

    public Optional<Protection> getProtection() {
        return _protection;
    }

    public Format[] getFormats() {
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

    public static Format parseFormat(final String format) {
        if (format.equals("HTML")) return Format.HTML;
        if (format.equals("Flash")) return Format.FLASH;
        if (format.equals("Flash Video")) return Format.FLASH_VIDEO;
        if (format.equals("MP3")) return Format.MP3;
        if (format.equals("MP4")) return Format.MP4;
        if (format.equals("PDF")) return Format.PDF;
        if (format.equals("PostScript")) return Format.POSTSCRIPT;
        if (format.equals("PowerPoint")) return Format.POWERPOINT;
        if (format.equals("RealMedia")) return Format.REALMEDIA;
        if (format.equals("txt")) return Format.TXT;
        if (format.equals("Windows Media Player")) return Format.WINDOWS_MEDIA_PLAYER;
        if (format.equals("Word")) return Format.WORD;
        throw new UnsupportedOperationException("Illegal format value (" + format + ")");
    }

    public static Locale parseLangage(final String langage) {
        if (langage.equals("en")) return Locale.ENGLISH;
        if (langage.equals("fr")) return Locale.FRENCH;
        throw new UnsupportedOperationException("Illegal langage value (" + langage + ")");
    }
}