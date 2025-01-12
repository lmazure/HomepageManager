package fr.mazure.homepagemanager.data.linkchecker;

import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.utils.StringHelper;

/**
 * Base class for all the link data parsers
 */
public class LinkContentParser {

    private Optional<Locale> _language;

    /**
     * @param data Data to be parsed
     */
    public LinkContentParser(final String data) {
        _language = StringHelper.guessLanguage(data);
    }

    /**
     * @return Language of the data
     */
    public Optional<Locale> getLanguage() {
        return _language;
    }
}
