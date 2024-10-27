package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.utils.StringHelper;

/**
 * Data extractor for YouTube channels
 */
public class YoutubeChannelUserLinkContentParser {

    private static final Pattern s_errorMessagePattern = Pattern.compile("\"alerts\":\\[\\{\"alertRenderer\":\\{\"type\":\"ERROR\",\"text\":\\{\"simpleText\":\"([^\\\"]*)\"\\}\\}\\}\\]");
    private static final Pattern s_descriptionPattern = Pattern.compile("<meta name=\"description\" content=\"([^\"]*)\">"); 

    private final String _data;
    private Optional<Locale> _language;
    private Optional<String> _errorMessage;

    /**
     * @param data retrieved link data
     */
    public YoutubeChannelUserLinkContentParser(final String data) {
        _data = data;
    }

    /**
     * @return error message
     */
    public Optional<String> getErrorMessage() {
        if (_errorMessage == null) {
            _errorMessage = extractErrorMessage();
        }

        return _errorMessage;
    }

    /**
     * @return language
     * @throws ContentParserException Failure to extract the information
     */
    public Optional<Locale> getLanguage() throws ContentParserException {
        if (_language == null) {
            final String description = extractDescription();
            _language = StringHelper.guessLanguage(description);
        }

        return _language;
    }

    private Optional<String> extractErrorMessage() {

        final Matcher m = s_errorMessagePattern.matcher(_data);
        if (m.find()) {
            return Optional.of(m.group(1));
        }

        return Optional.empty();
    }

    private String extractDescription() throws ContentParserException {

        final Matcher m = s_descriptionPattern.matcher(_data);
        if (m.find()) {
            return m.group(1);
        }

        throw new ContentParserException("Failed to find description in YouTube channel page");
    }
}
