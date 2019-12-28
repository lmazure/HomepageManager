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
    private final Optional<Integer> _durationHour; 
    private final Optional<Integer> _durationMinute; 
    private final Optional<Integer> _durationSecond;


    public LinkData(final String title,
                    final Optional<String> subtitle,
                    final String url,
                    final Optional<String> status,
                    final Optional<String> protection,
                    final String formats[],
                    final String languages[],
                    final Optional<Integer> durationHour, 
                    final Optional<Integer> durationMinute, 
                    final Optional<Integer> durationSecond) {
        _title = title;
        _subtitle = subtitle;
        _url = url;
        _status = status;
        _protection = protection;
        _formats = formats;
        _languages = languages;
        _durationHour = durationHour;
        _durationMinute = durationMinute;
        _durationSecond = durationSecond;
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
     * @return the durationHour
     */
    public Optional<Integer> getDurationHour() {
        return _durationHour;
    }

    /**
     * @return the durationMinute
     */
    public Optional<Integer> getDurationMinute() {
        return _durationMinute;
    }

    /**
     * @return the durationSecond
     */
    public Optional<Integer> getDurationSecond() {
        return _durationSecond;
    }
}