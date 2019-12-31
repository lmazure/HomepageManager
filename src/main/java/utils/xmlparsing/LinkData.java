package utils.xmlparsing;

import java.util.Optional;

public class LinkData {

    private final String _title;
    private final Optional<String> _subtitle;
    private final String _url;
    private final Optional<String> _status;
    private final Optional<String> _protection;
    private final String _formats[];
    private final String _languages[];
    private final Optional<DurationData> _duration; 

    public LinkData(final String title,
                    final Optional<String> subtitle, // TODO should be String[]
                    final String url,
                    final Optional<String> status,
                    final Optional<String> protection,
                    final String formats[],
                    final String languages[],
                    final Optional<DurationData> duration) {
        _title = title;
        _subtitle = subtitle;
        _url = url;
        _status = status;
        _protection = protection;
        _formats = formats;
        _languages = languages;
        _duration = duration;
    }

    public String getTitle() {
        return _title;
    }

    public Optional<String> getSubtitle() {
        return _subtitle;
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

    public Optional<DurationData> getDuration() {
        return _duration;
    }
}