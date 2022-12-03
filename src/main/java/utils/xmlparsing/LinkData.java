package utils.xmlparsing;

import java.time.Duration;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Optional;

/**
*
*/public class LinkData {

    private final String _title;
    private final String _subtitles[];
    private final String _url;
    private final Optional<LinkStatus> _status;
    private final Optional<LinkProtection> _protection;
    private final LinkFormat _formats[];
    private final Locale _languages[];
    private final Optional<Duration> _duration;
    private final Optional<TemporalAccessor> _publicationDate;

    public LinkData(final String title,
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

    public static LinkStatus parseStatus(final String status) {
        if (status.equals("dead")) {
            return LinkStatus.DEAD;
        }
        if (status.equals("obsolete")) {
            return LinkStatus.OBSOLETE;
        }
        if (status.equals("zombie")) {
            return LinkStatus.ZOMBIE;
        }
        throw new UnsupportedOperationException("Illegal status value (" + status + ")");
    }

    public static LinkProtection parseProtection(final String protection) {
        if (protection.equals("free_registration")) {
            return LinkProtection.FREE_REGISTRATION;
        }
        if (protection.equals("payed_registration")) {
            return LinkProtection.PAYED_REGISTRATION;
        }
        throw new UnsupportedOperationException("Illegal protection value (" + protection + ")");
    }

    public static LinkFormat parseFormat(final String format) {
        if (format.equals("HTML")) {
            return LinkFormat.HTML;
        }
        if (format.equals("Flash")) {
            return LinkFormat.FLASH;
        }
        if (format.equals("Flash Video")) {
            return LinkFormat.FLASH_VIDEO;
        }
        if (format.equals("MP3")) {
            return LinkFormat.MP3;
        }
        if (format.equals("MP4")) {
            return LinkFormat.MP4;
        }
        if (format.equals("PDF")) {
            return LinkFormat.PDF;
        }
        if (format.equals("PostScript")) {
            return LinkFormat.POSTSCRIPT;
        }
        if (format.equals("PowerPoint")) {
            return LinkFormat.POWERPOINT;
        }
        if (format.equals("RealMedia")) {
            return LinkFormat.REALMEDIA;
        }
        if (format.equals("txt")) {
            return LinkFormat.TXT;
        }
        if (format.equals("Windows Media Player")) {
            return LinkFormat.WINDOWS_MEDIA_PLAYER;
        }
        if (format.equals("Word")) {
            return LinkFormat.WORD;
        }
        throw new UnsupportedOperationException("Illegal format value (" + format + ")");
    }

    public static Locale parseLanguage(final String language) {
        if (language.equals("en")) {
            return Locale.ENGLISH;
        }
        if (language.equals("fr")) {
            return Locale.FRENCH;
        }
        if (language.equals("de")) {
            return Locale.GERMAN;
        }
        throw new UnsupportedOperationException("Illegal language value (" + language + ")");
    }
}