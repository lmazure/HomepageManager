package data.linkchecker;

import java.util.Locale;
import java.util.Optional;

import utils.StringHelper;

public class LinkContentParser {

    private final String _data;
    private Optional<Locale> _language;

    public LinkContentParser(final String data) {
        _data = data;
    }

    public Optional<Locale> getLanguage() {

        if (_language == null) {
            _language = extractLanguage();
        }

        return _language;
    }

    private Optional<Locale> extractLanguage() {

        final String data = _data.replaceAll("(?i)<SCRIPT[^>]*>.*?</SCRIPT *>", "")
                                 .replaceAll("(?i)<SVG[^>]*.*?</SVG *>", "")
                                 .replaceAll("(?i)<STYLE[^>]*.*?</STYLE *>", "")
                                 .replaceAll("<[^>]*>", "");

        return StringHelper.guessLanguage(data);
    }
}
