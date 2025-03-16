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
import fr.mazure.homepagemanager.utils.ExitHelper;
import fr.mazure.homepagemanager.utils.StringHelper;
import fr.mazure.homepagemanager.utils.internet.HtmlHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Data extractor for YouTube channels
 */
public class YoutubeChannelUserLinkContentParser extends LinkDataExtractor {

    static final String s_sourceName = "YouTube channel page";

    private static final Pattern s_errorMessagePattern = Pattern.compile("\"alerts\":\\[\\{\"alertRenderer\":\\{\"type\":\"ERROR\",\"text\":\\{\"simpleText\":\"([^\\\"]*)\"\\}\\}\\}\\]"); //TODO use TextParser
    private static final TextParser s_descriptionParser
        = new TextParser("<meta name=\"description\" content=\"",
                         "\">",
                         s_sourceName,
                         "description");

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

        _errorMessage = extractErrorMessage(data);

        if (_errorMessage.isPresent()) {
            _language = Locale.ENGLISH;
        } else {
            try {
                final String description = extractDescription(data);
                final Optional<Locale> lang = StringHelper.guessLanguage(description);
                //  we fallback to English if the language cannot be guessed
                _language = lang.orElse(Locale.ENGLISH);
            } catch (ContentParserException e) {
                // TODO should keep the exception escape
                ExitHelper.exit("Failed to extract description", e);
            }
        }
    }

    /**
     * @return error message
     */
    public Optional<String> getErrorMessage() {
        return _errorMessage;
    }

    /**
     * @return language
     */
    @Override
    public Locale getLanguage() {
        return _language;
    }

    private static Optional<String> extractErrorMessage(final String data) {

        final Matcher m = s_errorMessagePattern.matcher(data);
        if (m.find()) {
            return Optional.of(m.group(1));
        }

        return Optional.empty();
    }

    private static String extractDescription(final String data) throws ContentParserException {
        return HtmlHelper.cleanContent(s_descriptionParser.extract(data));
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support title");
    }

    @Override
    public Optional<String> getSubtitle() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support subtitle");
    }

    @Override
    public Optional<TemporalAccessor> getCreationDate() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support date");
    }

    @Override
    public Optional<TemporalAccessor> getPublicationDate() {
        return getCreationDate();
    }

    @Override
    public List<AuthorData> getSureAuthors() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support authors");
    }

    @Override
    public List<ExtractedLinkData> getLinks() {
        throw new UnsupportedOperationException("YoutubeChannelUserLinkContentParser does not support links");
    }
}
