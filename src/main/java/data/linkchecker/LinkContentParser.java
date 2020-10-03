package data.linkchecker;

import java.util.Locale;

import utils.StringHelper;

public class LinkContentParser {

    private final String _data;
    private Locale _language;

    public LinkContentParser(final String data) {
        _data = data;
    }

    public Locale getLanguage() {

        if (_language == null) {
            _language = extractLanguage();
        }

        return _language;
    }

    private Locale extractLanguage() {

        final String data = _data.replaceAll("(?i)<SCRIPT[^>]*>.*?</SCRIPT *>", "")
                                  .replaceAll("(?i)<SVG[^>]*.*?</SVG *>", "")
                                  .replaceAll("(?i)<STYLE[^>]*.*?</STYLE *>", "")
                                 .replaceAll("<[^>]*>", "");

        return StringHelper.guessLanguage(data);
    }
}
