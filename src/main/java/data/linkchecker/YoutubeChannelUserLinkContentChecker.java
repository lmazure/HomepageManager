package data.linkchecker;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class YoutubeChannelUserLinkContentChecker extends LinkContentChecker {

    private YoutubeChannelUserLinkContentParser _parser;

    public YoutubeChannelUserLinkContentChecker(final LinkData linkData,
                                            final Optional<ArticleData> articleData,
                                            final File file) {
        super(linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(String data) {
        _parser = new YoutubeChannelUserLinkContentParser(data);

        if (_parser.getErrorMessage().isPresent()) {
            return new LinkContentCheck(_parser.getErrorMessage().get());
        }

        return null;
    }


    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  Locale[] languages)
    {
        final Locale language = _parser.getLanguage();

        if (!Arrays.asList(languages).contains(language)) {
            return new LinkContentCheck("language is \"" + language + "\" but this one is unexpected");
        }

        return null;
    }
}
