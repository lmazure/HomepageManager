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
                    final Optional<String> subtitle,
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

    /**
     * @return the title
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @return the subtitle
     */
    public Optional<String> getSubtitle() {
        return _subtitle;
    }

    /**
     * @return the URL
     */
    public String getUrl() {
        return _url;
    }

    /**
     * @return the status
     */
    public Optional<String> getStatus() {
        return _status;
    }

    /**
     * @return the protection
     */
    public Optional<String> getProtection() {
        return _protection;
    }

    /**
     * @return the formats
     */
    public String[] getFormats() {
        return _formats;
    }

    /**
     * @return the languages
     */
    public String[] getLanguages() {
        return _languages;
    }

    /**
     * @return the duration
     */
    public Optional<DurationData> getDuration() {
        return _duration;
    }
}