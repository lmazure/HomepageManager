package data.linkchecker;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.ExitHelper;
import utils.StringHelper;

public class YoutubeChannelUserLinkContentParser {

    private final String _data;
    private Optional<Locale> _language;
    private Optional<String> _errorMessage;

    public YoutubeChannelUserLinkContentParser(final String data) {
        _data = data;
    }

    public Optional<String> getErrorMessage() {
        if (_errorMessage == null) {
            _errorMessage = extractErrorMessage();
        }

        return _errorMessage;
    }

    public Optional<Locale> getLanguage() {
        if (_language == null) {
            final String description = extractDescription();
            _language = StringHelper.guessLanguage(description);
        }

        return _language;
    }

    private Optional<String> extractErrorMessage() {

        final Pattern p = Pattern.compile("\"alerts\":\\[\\{\"alertRenderer\":\\{\"type\":\"ERROR\",\"text\":\\{\"simpleText\":\"([^\\\"]*)\"\\}\\}\\}\\]");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return Optional.of(m.group(1));
        }

        return Optional.empty();
    }

    private String extractDescription() {

        final Pattern p = Pattern.compile("<meta name=\"description\" content=\"([^\"]*)\">");
        final Matcher m = p.matcher(_data);
        if (m.find()) {
            return m.group(1);
        }

        ExitHelper.exit("Failed to find description in YouTube channel page");

        // NOTREACHED
        return null;
    }
}
