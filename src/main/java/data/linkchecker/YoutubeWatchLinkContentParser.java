package data.linkchecker;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;
import utils.StringHelper;

public class YoutubeWatchLinkContentParser {

    private final String _data;
    private final boolean _isEscaped;
    private Boolean _isPlayable;
    private String _title;
    private String _description;
    private Optional<Locale> _language;
    private Optional<Locale> _subtitlesLanguage;
    private LocalDate _uploadDate;
    private LocalDate _publishDate;
    private Duration _minDuration;
    private Duration _maxDuration;

    public YoutubeWatchLinkContentParser(final String data) {
        _data = data;
        if (data.contains("ytInitialPlayerResponse =")) {
            _isEscaped = false;
        } else if (data.contains("window[\"ytInitialPlayerResponse\"] =")) {
            _isEscaped = true;
        } else if (data.contains("ytplayer.config = {")) {
            _isEscaped = true;
        } else {
            ExitHelper.exit("Failed to recognize the YouTube answer type");
            // NOT REACHED
            _isEscaped = false;
        }
    }

    public boolean isPlayable() {
        if (_isPlayable == null) {
            _isPlayable = getPlayable();
        }

        return _isPlayable;
    }

    public String getTitle() {
        if (_title == null) {
            _title = extractField("title");
        }

        return _title;
    }

    public String getDescription() {
        if (_description == null) {
            _description = extractField("shortDescription");
        }

        return _description;
    }

    public LocalDate getUploadDate() {
        if (_uploadDate == null) {
            _uploadDate = extractDate("uploadDate");
        }

        return _uploadDate;
    }

    public LocalDate getPublishDate() {
        if (_publishDate == null) {
            _publishDate = extractDate("publishDate");
        }

        return _publishDate;
    }

    public Duration getMinDuration() {
        if (_minDuration == null) {
            extractDuration();
        }

        return _minDuration;
    }

    public Duration getMaxDuration() {
        if (_maxDuration == null) {
            extractDuration();
        }

        return _maxDuration;
    }

    public Optional<Locale> getLanguage() {
        if (_language == null) {
            final Optional<Locale> lang = getSubtitlesLanguage();
            if (lang.isPresent()) {
                _language = lang;
            } else {
                _language = StringHelper.guessLanguage(getDescription());
            }
        }

        return _language;
    }

    public Optional<Locale> getSubtitlesLanguage() {
        final String prefix = _isEscaped ? "\\\"name\\\":{\\\"simpleText\\\":\\\""
                                         : "\"name\":{\"simpleText\":\"";
        final String postfix = _isEscaped ? " (auto-generated)\\\"}"
                                          : " (auto-generated)\"}";
        if (_subtitlesLanguage == null) {
            if (_data.contains(prefix + "French" + postfix)) {
                _subtitlesLanguage = Optional.of(Locale.FRENCH);
            } else if (_data.contains(prefix + "English" + postfix)) {
                _subtitlesLanguage = Optional.of(Locale.ENGLISH);
            } else {
                _subtitlesLanguage = Optional.empty();
            }
        }

        return _subtitlesLanguage;
    }

    private boolean getPlayable() {
        return _isEscaped ? _data.contains("\\\"playabilityStatus\\\":{\\\"status\\\":\\\"OK\\\"")
                          : _data.contains("\"playabilityStatus\":{\"status\":\"OK\"");
    }

    private String extractField(final String str) {
        String text = null;

        final Pattern p = Pattern.compile(_isEscaped ? ("\\\\\"" + str + "\\\\\":\\\\\"(.+?)(?<!\\\\\\\\)\\\\\"")
                                                     : (",\"" + str + "\":\"(.+?)(?<!\\\\)\""));
        final Matcher m = p.matcher(_data);
        while (m.find()) {
            final String t = m.group(1);
            if (text == null) {
                text = t;
            } else {
                if (!text.equals(t)) {
                    ExitHelper.exit("Found different " + str + " texts in YouTube watch page");
                }
            }
        }

        if (text == null) {
            ExitHelper.exit("Failed to extract " + str + " text from YouTube watch page ");
        }

        assert(text != null);
        
        if (_isEscaped) {
            text = text.replaceAll(Pattern.quote("\\\\n"), "\n")
                       .replaceAll(Pattern.quote("\\\\u0026"),"&")
                       .replaceAll(Pattern.quote("\\\\u0090"),"\u0090")
                       .replaceAll(Pattern.quote("\\/"),"/")
                       .replaceAll(Pattern.quote("\\\\\\\""),"\"")
                       .replaceAll(Pattern.quote("\\\\\\'"),"'");
        } else {
            text = text.replaceAll(Pattern.quote("\\n"), "\n")
                       .replaceAll(Pattern.quote("\\u0026"),"&")
                       .replaceAll(Pattern.quote("\\/"),"/")
                       .replaceAll(Pattern.quote("\\\""),"\"")
                       .replaceAll(Pattern.quote("\\\'"),"'");
        }

        return text;
    }

    private LocalDate extractDate(final String str) {
        return LocalDate.parse(extractField(str));
    }

    private void extractDuration() {

        int minDuration = Integer.MAX_VALUE;
        int maxDuration = Integer.MIN_VALUE;

        final Pattern p = _isEscaped ? Pattern.compile("\\\\\"approxDurationMs\\\\\":\\\\\"(\\d+)\\\\\"")
                                     :  Pattern.compile("\"approxDurationMs\":\"(\\d+)\"");
        final Matcher m = p.matcher(_data);
        while (m.find()) {
            final int duration = Integer.parseInt(m.group(1));
            if (duration < minDuration) {
                minDuration = duration;
            }
            if (duration > maxDuration) {
                maxDuration = duration;
            }
        }

        if (minDuration == Integer.MAX_VALUE) {
            ExitHelper.exit("Failed to extract durations from YouTube watch page");
        }

        _minDuration = Duration.ofMillis(minDuration);
        _maxDuration = Duration.ofMillis(maxDuration);
    }
}
