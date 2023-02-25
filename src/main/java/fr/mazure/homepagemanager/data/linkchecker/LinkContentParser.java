package fr.mazure.homepagemanager.data.linkchecker;

import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;

/**
 * Base class for all the link data parsers
 */
public class LinkContentParser {

    private final String _data;
    private Optional<Locale> _language;

    /**
     * @param data Data to be parsed
     */
    public LinkContentParser(final String data) {
        _data = HtmlHelper.cleanContent(data);
    }

    /**
     * @return Language of the data
     */
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
