package data.linkchecker;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import data.ParameterRepository;
import utils.StringHelper;
import utils.internet.TwitterApi;
import utils.internet.TwitterUserDto;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class TwitterLinkContentChecker extends LinkContentChecker {

    private final TwitterUserDto _dto; 

    public TwitterLinkContentChecker(final URL url,
                                     final LinkData linkData,
                                     final Optional<ArticleData> articleData,
                                     final File file) {
        super(linkData, articleData, file);
        final TwitterApi api = new TwitterApi(ParameterRepository.getTwitterApiKey(), ParameterRepository.getTwitterApiSecretKey());
        final String userName = url.toString().replace("https://twitter.com/", "");
        _dto = api.getUser(userName);
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        final String description = _dto.getDescription();
        final Optional<Locale> language = StringHelper.guessLanguage(description);

        if (language.isPresent() && !Arrays.asList(languages).contains(language.get())) {
            return new LinkContentCheck("language is \"" + language + "\" but this one is unexpected");
        }

        return null;
    }
}
