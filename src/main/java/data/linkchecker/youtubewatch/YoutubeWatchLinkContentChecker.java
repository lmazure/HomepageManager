package data.linkchecker.youtubewatch;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import data.linkchecker.ContentParserException;
import data.linkchecker.LinkContentCheck;
import data.linkchecker.LinkContentChecker;
import utils.FileSection;
import utils.StringHelper;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;

public class YoutubeWatchLinkContentChecker extends LinkContentChecker {

    private YoutubeWatchLinkContentParser _parser;

    public YoutubeWatchLinkContentChecker(final String url,
                                          final LinkData linkData,
                                          final Optional<ArticleData> articleData,
                                          final FileSection file) {
        super(url, linkData, articleData, file);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {
        _parser = new YoutubeWatchLinkContentParser(getUrl(), data);

        if (!_parser.isPlayable()) {
            return new LinkContentCheck("video is not playable");
        }

        return null;
    }

    @Override
    public LinkContentCheck checkLinkTitle(final String data,
                                           final String title) throws ContentParserException {

        final String effectiveTitle = _parser.getTitle();

        final String diff = StringHelper.compareAndExplainDifference(title, effectiveTitle);
        if (diff != null) {
            return new LinkContentCheck("title \"" +
                                        title +
                                        "\" is not equal to the real title \"" +
                                        effectiveTitle +
                                          "\"\n" +
                                        diff);
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors)
    {
        return null;
    }

    @Override
    public LinkContentCheck checkLinkDuration(final String data,
                                              final Duration expectedDuration) throws ContentParserException {

        final Duration effectiveMinDuration = _parser.getMinDuration().truncatedTo(ChronoUnit.SECONDS);
        final Duration effectiveMaxDuration = _parser.getMaxDuration().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1);

        if ((expectedDuration.compareTo(effectiveMinDuration) < 0) ||
            (expectedDuration.compareTo(effectiveMaxDuration) > 0)) {
            return new LinkContentCheck("expected duration " +
                                        expectedDuration +
                                        " is not in the real duration interval [" +
                                        effectiveMinDuration +
                                        "," +
                                        effectiveMaxDuration +
                                        "]");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkArticleDate(final String data,
                                                final Optional<TemporalAccessor> publicationDate,
                                                final Optional<TemporalAccessor> creationDate) throws ContentParserException
    {

        if (creationDate.isEmpty() && publicationDate.isEmpty()) {
            return new LinkContentCheck("YouTube link with neither creation date not publication date");
        }

        final TemporalAccessor date = publicationDate.isPresent() ? publicationDate.get() : creationDate.get();

        if (!(date instanceof LocalDate)) {
            return new LinkContentCheck("Date without month or day");
       }

        final LocalDate expectedDate = (LocalDate)date;
        final LocalDate effectivePublishDate = _parser.getPublishDateInternal();
        final LocalDate effectiveUploadDate = _parser.getUploadDateInternal();

        if (!expectedDate.equals(effectivePublishDate)) {
            return new LinkContentCheck("expected date " +
                                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectivePublishDate);
       }

       if (!expectedDate.equals(effectiveUploadDate)) {
            return new LinkContentCheck("expected date " +
                                        expectedDate +
                                        " is not equal to the effective upload date " +
                                        effectivePublishDate);
       }

       return null;
    }

    @Override
    protected LinkContentCheck checkLinkLanguages(final String data,
                                                  final Locale[] languages) throws ContentParserException
    {
        final Optional<Locale> language = _parser.getLanguage();

        if (language.isPresent() && !Arrays.asList(languages).contains(language.get())) {
            return new LinkContentCheck("language is \"" + language.get() + "\" but this one is unexpected");
        }

        return null;
    }
}
