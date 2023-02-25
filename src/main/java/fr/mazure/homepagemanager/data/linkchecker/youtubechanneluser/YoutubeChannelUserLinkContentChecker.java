package fr.mazure.homepagemanager.data.linkchecker.youtubechanneluser;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentCheck;
import fr.mazure.homepagemanager.data.linkchecker.LinkContentChecker;
import fr.mazure.homepagemanager.utils.FileSection;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;

/**
*
*/
public class YoutubeChannelUserLinkContentChecker extends LinkContentChecker {

    private YoutubeChannelUserLinkContentParser _parser;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public YoutubeChannelUserLinkContentChecker(final String url,
                                                final LinkData linkData,
                                                final Optional<ArticleData> articleData,
                                                final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) {
        _parser = new YoutubeChannelUserLinkContentParser(data);

        if (_parser.getErrorMessage().isPresent()) {
            return new LinkContentCheck("LinkDataRetrievalFailure",
                                        _parser.getErrorMessage().get(),
                                        Optional.empty());
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  Locale[] languages) throws ContentParserException
    {
        final Optional<Locale> language = _parser.getLanguage();

        if (language.isPresent() && !Arrays.asList(languages).contains(language.get())) {
            return new LinkContentCheck("WrongLanguage",
                                        "language is \"" + language.get() + "\" but this one is unexpected",
                                        Optional.empty());
        }

        return null;
    }
}
