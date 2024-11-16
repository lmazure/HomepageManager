package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser;

import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.TextParser;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Data extractor for YouTube channels
 */
public class YoutubeChannelUserLinkContentParser extends LinkDataExtractor {
    
    static final String s_sourceName = "YouTube channel page";

    private static final Pattern s_errorMessagePattern = Pattern.compile("\"alerts\":\\[\\{\"alertRenderer\":\\{\"type\":\"ERROR\",\"text\":\\{\"simpleText\":\"([^\\\"]*)\"\\}\\}\\}\\]");
    private static final TextParser s_descriptionParser
        = new TextParser("<meta name=\"description\" content=\"",
                         "\">",
                         s_sourceName,
                         "description");

    private final String _data;
    private Locale _language;
    private Optional<String> _errorMessage;

    /**
     * @param url URL of the link
     * @param data retrieved link data
     * @param retriever cache data retriever
     */
    public YoutubeChannelUserLinkContentParser(final String url,
                                               final String data,
                                               final CachedSiteDataRetriever retriever) {
        super(url, retriever);
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
    @Override
    public Locale getLanguage() throws ContentParserException {
        if (_language == null) {
            final String description = extractDescription();
            final Optional<Locale> lang = StringHelper.guessLanguage(description);
            // we fallback to English if the language cannot be guessed
            _language = lang.orElse(Locale.ENGLISH);
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
        return HtmlHelper.cleanContent(s_descriptionParser.extract(_data));
    }

    @Override
    public String getTitle() throws ContentParserException {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support title");
    }

    @Override
    public Optional<String> getSubtitle() throws ContentParserException {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support subtitle");
    }

    @Override
    public Optional<TemporalAccessor> getDate() throws ContentParserException {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support date");
    }

    @Override
    public List<AuthorData> getSureAuthors() throws ContentParserException {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support authors");
    }

    @Override
    public List<ExtractedLinkData> getLinks() throws ContentParserException {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support links");
    }
}
