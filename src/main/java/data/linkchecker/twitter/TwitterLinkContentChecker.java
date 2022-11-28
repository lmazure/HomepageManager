package data.linkchecker.twitter;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import data.ParameterRepository;
import data.linkchecker.LinkContentCheck;
import data.linkchecker.LinkContentChecker;
import utils.FileSection;
import utils.StringHelper;
import utils.internet.twitter.TwitterApi;
import utils.internet.twitter.TwitterUserDto;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.LinkData;

public class TwitterLinkContentChecker extends LinkContentChecker {

    private final TwitterUserDto _dto;

    public TwitterLinkContentChecker(final String url,
                                     final LinkData linkData,
                                     final Optional<ArticleData> articleData,
                                     final FileSection file) {
        super(url, linkData, articleData, file);
        final TwitterApi api = new TwitterApi(ParameterRepository.getTwitterApiKey(), ParameterRepository.getTwitterApiSecretKey());
        final String userName = url.replace("https://twitter.com/", "");
        _dto = api.getUser(userName);
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages)
    {
        final String description = _dto.getDescription();
        final Optional<Locale> language = StringHelper.guessLanguage(description);

        if (language.isPresent() && !Arrays.asList(languages).contains(language.get())) {
            return new LinkContentCheck("language is \"" + language.get() + "\" but this one is unexpected");
        }

        return null;
    }
}
