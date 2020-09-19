package data.linkchecker;

import utils.StringHelper;

public class LinkContentParser {

    private final String _data;
    private String _language;

    public LinkContentParser(final String data) {
        _data = data;
    }

    public String getLanguage() {

        if (_language == null) {
            _language= extractLanguage();
        }

        return _language;
    }

    private String extractLanguage() {

        final String data = _data.replaceAll("(?i)<SCRIPT[^>]*>.*?</SCRIPT *>", "")
                                  .replaceAll("(?i)<SVG[^>]*.*?</SVG *>", "")
                                  .replaceAll("(?i)<STYLE[^>]*.*?</STYLE *>", "")
                                 .replaceAll("<[^>]*>", "");

        return StringHelper.guessLanguage(data);
    }
}
