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
import data.linkchecker.ExtractorBasedLinkContentChecker;
import data.linkchecker.LinkContentCheck;
import utils.FileSection;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.LinkData;

/**
*
*/
public class YoutubeWatchLinkContentChecker extends ExtractorBasedLinkContentChecker {

    private YoutubeWatchLinkContentParser _parser;

    /**
     * @param url URL of the link to check
     * @param linkData expected link data
     * @param articleData expected article data
     * @param file effective retrieved link data
     */
    public YoutubeWatchLinkContentChecker(final String url,
                                          final LinkData linkData,
                                          final Optional<ArticleData> articleData,
                                          final FileSection file) {
        super(url, linkData, articleData, file, (LinkDataExtractorBuilder)YoutubeWatchLinkContentParser::new);
    }

    @Override
    protected LinkContentCheck checkGlobalData(final String data) throws ContentParserException {
        super.checkGlobalData(data);
        _parser = (YoutubeWatchLinkContentParser)(getParser()); // TODO cleanup this crap
        if (!_parser.isPlayable()) {
            return new LinkContentCheck("VideoNotPlayable",
                                        "video is not playable");
        }

        return null;
    }

    @Override
    protected LinkContentCheck checkLinkAuthors(final String data,
                                                final List<AuthorData> authors)
    {
        return null; //TODO We will have to check YT authors sometime in the future
    }

    @Override
    public LinkContentCheck checkLinkDuration(final String data,
                                              final Duration expectedDuration) throws ContentParserException {

        final Duration effectiveMinDuration = _parser.getMinDuration().truncatedTo(ChronoUnit.SECONDS);
        final Duration effectiveMaxDuration = _parser.getMaxDuration().truncatedTo(ChronoUnit.SECONDS).plusSeconds(1);

        if ((expectedDuration.compareTo(effectiveMinDuration) < 0) ||
            (expectedDuration.compareTo(effectiveMaxDuration) > 0)) {
            return new LinkContentCheck("WrongDuration",
                                        "expected duration " +
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
            return new LinkContentCheck("MissingDate",
                                        "YouTube link with neither creation date not publication date");
        }

        final TemporalAccessor date = publicationDate.isPresent() ? publicationDate.get() : creationDate.get();

        if (!(date instanceof LocalDate)) {
            return new LinkContentCheck("IncorrectDate",
                                        "Date without month or day");
       }

        final LocalDate expectedDate = (LocalDate)date;
        final LocalDate effectivePublishDate = _parser.getPublishDateInternal();
        final LocalDate effectiveUploadDate = _parser.getUploadDateInternal();

        if (!expectedDate.equals(effectivePublishDate)) {
            return new LinkContentCheck("WrongDate",
                                        "expected date " +
                                        expectedDate +
                                        " is not equal to the effective publish date " +
                                        effectivePublishDate);
       }

       if (!expectedDate.equals(effectiveUploadDate)) {
            return new LinkContentCheck("WrongDate",
                                        "expected date " +
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
        if (!Arrays.asList(languages).contains(_parser.getLanguage())) {
            return new LinkContentCheck("WrongLanguage",
                                        "language is \"" +
                                        _parser.getLanguage() +
                                        "\" but this one is unexpected");
        }

        return null;
    }
}
