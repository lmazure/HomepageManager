package data.linkchecker;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;
import utils.StringHelper;

public class YoutubeWatchLinkContentParser {

    private final String _data;
    private Boolean _isPlayable;
    private String _title;
    private String _description;
    private Locale _language;
    private LocalDate _uploadDate;
    private LocalDate _publishDate;
    private Duration _minDuration;
    private Duration _maxDuration;

    public YoutubeWatchLinkContentParser(final String data) {
        _data = data;
    }

    public boolean isPlayable() {

        if (_isPlayable == null) {
            _isPlayable = getPlayable();
        }

        return _isPlayable;
    }

    public String getTitle() {

        if (_title == null) {
            _title = extractText("title");
        }

        return _title;
    }

    public String getDescription() {

        if (_description == null) {
            _description = extractText("shortDescription");
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

    private boolean getPlayable() {
        return _data.contains("\\\"playabilityStatus\\\":{\\\"status\\\":\\\"OK\\\"");
    }

    public Locale getLanguage() {

        if (_language == null) {
            if (_data.contains("\\\"name\\\":{\\\"simpleText\\\":\\\"French (auto-generated)\\\"}")) {
                _language = Locale.FRENCH;
            } else if (_data.contains("\\\"name\\\":{\\\"simpleText\\\":\\\"English (auto-generated)\\\"}")) {
                _language = Locale.ENGLISH;
            } else {
                _language = StringHelper.guessLanguage(getDescription());
            }
        }

        return _language;
    }

    private String extractText(final String str) {

        String text = null;

        final Pattern p = Pattern.compile("\\\\\"" + str + "\\\\\":\\\\\"(.+?)(?<!\\\\\\\\)\\\\\"");
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
            ExitHelper.exit("Failed to extract " + str + " text from YouTube watch page");
        }

        assert(text != null);
        text = text.replaceAll("\\\\\\\\n", "\n")
                   .replaceAll("\\\\\\\\u0026","&")
                   .replaceAll("\\\\u0090","\u0090")
                   .replaceAll("\\\\/","/")
                   .replaceAll("\\\\\\\\\\\\\"","\"");

        return text;
    }

    private LocalDate extractDate(final String str) {
        return LocalDate.parse(extractText(str));
    }

    private void extractDuration() {

        int minDuration = Integer.MAX_VALUE;
        int maxDuration = Integer.MIN_VALUE;

        final Pattern p = Pattern.compile("\\\\\"approxDurationMs\\\\\":\\\\\"(\\d+)\\\\\"");
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
