package data.linkchecker;

import java.util.Locale;
import java.util.Optional;

import utils.StringHelper;
import utils.internet.HtmlHelper;

public class LinkContentParser {

    private final String _data;
    private Optional<Locale> _language;

    public LinkContentParser(final String data) {
        _data = HtmlHelper.cleanContent(data);
    }

    public Optional<Locale> getLanguage() {

        if (_language == null) {
            _language = extractLanguage();
        }

        return _language;
    }

    private Optional<Locale> extractLanguage() {
        return StringHelper.guessLanguage(_data);
    }
}
